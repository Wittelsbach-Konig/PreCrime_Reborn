import React from "react";
import TableGroup from "./TableGroup";
import RegMans from "./newMan";
import DelMan from "./DelMan"
import CorMans from "./CorMans"
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
        this.setState({ showModal_1: true });
    };

    closeModal_1 = () => {
        this.setState({showModal_1: false});
        this.props.renew();
    };

    openModal_2 = () => {
        this.setState({ showModal_2: true });
    };

    closeModal_2 = () => {
        this.setState({showModal_2: false});
        this.props.renew();
    };

    showAll = () => {
        this.setState({ showWorking: !this.state.showWorking }, ()=>
        {
            this.props.onChange(!this.state.showWorking)
           // this.props.renew()
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
            const token = localStorage.getItem('jwtToken');


            fetch(`http://localhost:8028/api/v1/reactiongroup/${this.state.idGroup}/statistic`, {
                method: 'GET', // или другой метод
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
            })
                .then(responses => responses.json())
                .then(data => {
                    this.setState({manData:data})
                })
                .catch(error => {
                    console.error('Ошибка при запросе к серверу:', error);
                });
        });
        console.log(this.state.prevId)
    };

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
                <h1 className="car-text">Group List</h1>
                <TableGroup groupList={this.props.group} idTr={this.updateState}/>

                <button className="new-group" onClick={this.openModal}>Register new man</button>
                {this.state.showModal && <RegMans onClose={this.closeModal} onRenew={this.props.renew} shWrk={this.updateWrk}/>}
                <button className="corr-group" onClick={this.openModal_1}>Correct man</button>
                {this.state.showModal_1 && <CorMans onClose={this.closeModal_1} onRenew={this.props.renew}/>}
                <button className="del-man" onClick={this.openModal_2}>Retire man</button>
                {this.state.showModal_2 && <DelMan onClose={this.closeModal_2} onRenew={this.props.renew} />}
                {this.state.showWorking ? <button className="work-man" onClick={this.showAll}>Show all men</button>
                    :<button className="work-man" onClick={this.showAll}>Show working men</button>}

                {manData && (
                    <div className="man-statistic">
                        <h3>Criminal info</h3>
                        <p>ID: {manData.id}</p>
                        <p>Member Name: {manData.memberName}</p>
                        <p>In Operation: {manData.inOperation ? 'Yes':'No'}</p>
                        <p>Criminals Caught: {manData.criminalsCaught}</p>
                        <p>Criminals Escaped: {manData.criminalsEscaped}</p>
                    </div>
                )}
            </div>
        )
    }
}

export default ReactionGroup