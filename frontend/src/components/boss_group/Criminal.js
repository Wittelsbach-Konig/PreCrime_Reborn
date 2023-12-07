import React from "react";

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
        const {onChange, me} = this.props
        const {showReactionGroup, showTransport, showAmmunition, showCriminal} = this.state
        return ( <div>
                Criminal
            </div>
        )
    }
}

export default Criminal