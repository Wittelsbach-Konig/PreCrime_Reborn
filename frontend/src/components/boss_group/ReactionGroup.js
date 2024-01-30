import React from "react";
import TableGroup from "./TableGroup";
import RegMans from "./newMan";
import CorMans from "./CorMans"
import close from "../../img/close.png";
import update from "../../img/correct.png"
class ReactionGroup extends React.Component {
    constructor(props) {

        super(props);

        this.state = {
            showModal: false,
            showModal_1: false,
            showModal_2: false,
            showWorking:false,
            idGroup:0,
            manData: null,
            prevId:0,
        }

    }



    openModal = () => {
        this.setState({ showModal: true });
    };

    closeModal = () => {
        this.setState({showModal: false});
        this.props.renew();
    };

    openModal_1 = () => {
        if(!this.state.manData)
        {window.alert('Choose men')}
        else
        {this.setState({ showModal_1: true });}
    };

    closeModal_1 = () => {
        this.setState({showModal_1: false});
        this.props.renew();
    };

    retireMan = () => {

        const putData = {
            id: this.state.idd,
        };
        this.fetch_method("PUT", `${this.state.idGroup}/retire`)

    };

    closeModal_2 = () => {
        this.setState({showModal_2: false});
        this.props.renew();
    };

    showAll = () => {
        this.setState({ showWorking: !this.state.showWorking }, ()=>
        {
            this.props.onChange(!this.state.showWorking)
        });

    };

    componentDidUpdate(prevProps, prevState) {
        const {prevId, idGroup} = this.state
        if (prevId !== idGroup) {
            this.updateStateId();
        }
    }


    updateStateId = () => {
        this.setState({ prevId: this.state.idGroup }, ()=>
        {
            this.fetch_method('GET',`${this.state.idGroup}/statistic`,)
        });
        console.log(this.state.prevId)
    };


    fetch_method =(method, url) =>{
        const token = localStorage.getItem('jwtToken');


        fetch(`api/v1/reactiongroup/${url}`, {
            method: method, // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(responses => responses.json())
            .then(data => {
                if (method === "GET")
                {this.setState({manData:data})}
                this.props.renew()
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
    }

    updateState = (newValue) => {
        this.setState({ idGroup: newValue });
        console.log(this.state.idGroup)
    };

    updateWrk = () => {
        this.setState({ showWorking: true });
    };

    render() {
        const {manData, idGroup, prevId} = this.state
        return ( <div>
                <header className="header-pr">
                    <button className="acc-vis" onClick={this.openModal}>Register new men</button>

                    {this.state.showWorking ? <button className="acc-vis" onClick={this.showAll}>Show all men</button>
                        :<button className="acc-vis" onClick={this.showAll}>Show working men</button>}
                </header>
                <h1 className="car-text">Group List</h1>
                {this.state.manData && this.state.showModal_1 && <CorMans
                    onClose={this.closeModal_1}
                    onRenew={this.props.renew}
                    manData={this.state.manData}
                />}
                {this.state.showModal && <RegMans
                    onClose={this.closeModal}
                    onRenew={this.props.renew}
                    shWrk={this.updateWrk}
                />}
                {manData && (
                    <div className="man-statistic">
                        <table className="bg-rg">
                            <tbody>
                            <tr>
                                <td colSpan="2" className="table-label">{manData.memberName}</td>
                            </tr>
                            <tr>
                                <td className="table-label">ID:</td>
                                <td className="table-label">{manData.id}</td>
                            </tr>
                            <tr>
                                <td className="table-label">In Operation:</td>
                                <td className="table-label">{manData.inOperation ? 'Yes':'No'}</td>
                            </tr>
                            <tr>
                                <td className="table-label">Criminals Caught:</td>
                                <td className="table-label">{manData.criminalsCaught}</td>
                            </tr>
                            <tr>
                                <td className="table-label">Criminals Escaped:</td>
                                <td className="table-label">{manData.criminalsEscaped}</td>
                            </tr>
                            <tr>

                                <td className="table-label">
                                    <button className="fuel-but" onClick={this.openModal_1}>
                                        <img src={update} className="fuel" alt="Кнопка «button»"/>
                                    </button>
                                    <div className="tooltip" id="tooltip">Update</div>
                                </td>
                                <td className="table-label">
                                    <button className="fuel-but" onClick={this.retireMan}>
                                        <img src={close} className="fuel" alt="Кнопка «button»"/>
                                    </button>
                                    <div className="tooltip" id="tooltip">Retire</div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                )}
                <TableGroup groupList={this.props.group} idTr={this.updateState}/>

            </div>
        )
    }
}

export default ReactionGroup