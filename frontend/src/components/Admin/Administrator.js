import React, { Component } from 'react';
import Header from "../Header";
import BurgerMenu from "../BurgerMenu";
import Profile from "../profile";
import AddVision from "../visions/AddVision";

class Administrator extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            showModal_2: false,
            psychics: [],
            visions:[],
            selectedPsychic: null,

        };
    }

    openModal = () => {
        this.setState({ showModal: true });
        this.setState({ showModal_2: false });

    };

    closeModal = () => {
        this.setState({ showModal: false});
        this.setState({ showModal_2: true });
    };





    render() {
        const { selectedPsychic } = this.state;
        const { onChange, me } = this.props
        return (<div>
                <Profile me={me}/>
                <Header pullRole={this.props.pullRole} isLogged={this.props.isLogged} onChange={onChange} rol={me.roles}/>

                <div className="frame-but">
                    <div className="rectangle-but" />
                    <button className="add-vis" onClick={this.openModal}>Add Vision</button>
                    {this.state.showModal && <AddVision onClose={this.closeModal} shWrk={this.updateWrk}/>}
                   </div>



                <div className="frame-2">
                    <div className="rectangle-2" />
                </div>
            </div>
        );
    }
}

export default Administrator;
