import React from "react";
import SupplyTable from "./SupplyTable";
import NewAmmunition from "./NewAmmunition";
import Refuel from "./Refuel";

class Ammunition extends React.Component {
    constructor(props) {

        super(props);

        this.state = {

            showReactionGroup: false,
            showTransport: false,
            showAmmunition: false,
            showCriminal: false,
            showModal: false,
            showModal_2: false,
            amountAmm: false,
            idAmm:0
        }

    }


    updateState = (newValue) => {
        this.setState({ idAmm: newValue });
        console.log(this.state.idTransport)
    };

    openModal = () => {
        this.setState({ showModal: true });
    };

    closeModal = () => {
        this.setState({showModal: false});
        this.props.renew();
    };

    openModal_2 = () => {
        this.setState({ showModal_2: true });
        this.setState({ amountAmm:true});
    };

    closeModal_2 = () => {
        this.setState({showModal_2: false});
        this.setState({amountAmm: false});
        this.props.renew();
    };




    render() {
        const {onChange, me, ammun} = this.props
        const {showReactionGroup, showTransport, showAmmunition, showCriminal} = this.state
        return ( <div>

                <header className="header-pr">
                    <button className="acc-vis" onClick={this.openModal}>Supply Ammunition</button>
                    <button className="del-vis" onClick={this.openModal_2}>Amount</button>
                </header>
                <h1 className="car-text">Ammunition List</h1>
                <SupplyTable supplyList={ammun} idTr={this.updateState} />

                {this.state.showModal && <NewAmmunition onClose={this.closeModal} onRenew={this.props.renew} />}

                {this.state.showModal_2 && <Refuel reff={false} amm={this.state.amountAmm} onClose={this.closeModal_2} idTr={this.state.idAmm} onRenew={this.props.renew}/>}
            </div>
        )
    }
}

export default Ammunition