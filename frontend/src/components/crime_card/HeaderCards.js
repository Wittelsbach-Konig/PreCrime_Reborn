import React from "react";
import CrimeCard from "./crimeCard";
import CreateCard from "./CreateCard";
class Header extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            showCard: false,
        }
    }

    openModal = () => {
        this.props.showCr(true)
    };


    render() {
        return (
            <header className="header">
                <button className="button-logout" onClick={()=>{this.props.isLogged(false)}}>logout</button>
                <div className="button-container">
                    <button onClick={this.openModal}> </button>
                </div>

            </header>
        )
    }


}

export default Header