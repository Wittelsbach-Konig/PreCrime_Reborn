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

    showGR = () => {
        this.setState({showReactionGroup: true})
        this.setState({showTransport: false})
        this.setState({showAmmunition: false})
        this.setState({showCriminal:false})
    };

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