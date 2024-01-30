import React from "react";
import TableTransport from "./TableTransport";
import RegTransport from "./RegTransport";
import Refuel from "./Refuel";
class Transport extends React.Component {
    constructor(props) {

        super(props);

        this.state = {
            showModal: false,
            showModal_2: false,
            refuel: false,
            idTransport:0,
        }

    }



    openModal = () => {
        this.setState({ showModal: true });
    };

    closeModal = () => {
        this.setState({showModal: false});
        this.props.renew();
    };



    updateState = (newValue) => {
        this.setState({ idTransport: newValue });
        console.log(this.state.idTransport)
    };





    render() {
        return ( <div>

                <header className="header-pr">
                    <button className="acc-vis" onClick={this.openModal}>Register new transport</button>
                </header>
                <h1 className="car-text">Transport List</h1>
                <TableTransport carList={this.props.trans} idTr={this.updateState} renew={this.props.renew}/>
                {this.state.showModal && <RegTransport onClose={this.closeModal} onRenew={this.props.renew} />}
            </div>
        )
    }
}

export default Transport