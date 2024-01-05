import React from "react";
import Profile from "../components/profile"
import { gsap } from 'gsap'
import "../css/gsap_style.css"
class Header extends React.Component {
    constructor(props) {
        super(props);
        this.circle = React.createRef();
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

    componentDidMount() {
        gsap.to('.circle', { rotation: "-=360",
            repeat: -1,
            repeatDelay: 1});
    }


    render() {
        const {onChange, me} = this.props
        const { showProf } = this.state
        return (
            <div>
            <header className="header">
                <button className="button-logout" onClick={()=>{this.props.isLogged(false)}}>logout</button>
                <div className="button-container">
                    <button className="button-profile" onClick={()=>{this.openModal()}}>profile</button>
                    {//<div className="circle gradient-green" ref={this.circle}>REF</div>
                    }
                        </div>

                        </header>
                    {showProf && <Profile me={me}/>}


            </div>
                        )
                    }


}

export default Header