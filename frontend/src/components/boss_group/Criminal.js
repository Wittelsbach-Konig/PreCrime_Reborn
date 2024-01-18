import React from "react";
import CriminalTable from "./CriminalTable";

class Criminal extends React.Component {
    constructor(props) {

        super(props);

        this.state = {
            showReactionGroup: false,
            showTransport: false,
            showAmmunition: false,
            showCriminal: false,
        }

    }


    render() {
        const {criminals, renew} = this.props
        const {showReactionGroup, showTransport, showAmmunition, showCriminal} = this.state
        return ( <div>
                <h1 className="car-text">Criminal info</h1>
                <CriminalTable criminalList={criminals} idTr={this.updateState} onRenew={renew}/>


        </div>
        )
    }
}

export default Criminal