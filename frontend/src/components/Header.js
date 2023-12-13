import React from "react";
import Profile from "../components/profile"
class Header extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            me:{
                email:'',
                firstName: '',
                id:0,
                lastName: '',
                login:'',
                roles:[],
                telegramId: 0
            },
            showProf:false,

        }
    }

    openModal = () => {
        this.setState({showProf: !this.state.showProf});
    };


    render() {
        const {onChange, me} = this.props
        const { showProf } = this.state
        return (
            <div>
            <header className="header">
                <button className="button-logout" onClick={()=>{this.props.isLogged(false)}}>logout</button>
                <div className="button-container">
                    <button className="button-profile" onClick={()=>{this.openModal()}}>profile</button>
                        </div>

                        </header>
                    {showProf && <Profile me={me}/>}
            </div>
                        )
                    }


}

export default Header