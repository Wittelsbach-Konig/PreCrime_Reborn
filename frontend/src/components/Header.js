import React from "react";
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
            }
        }
    }

    render() {
        const keys = Object.keys(this.props.pullRole);
        const {onChange} = this.props
        return (
            <header className="header">
                <button className="button-logout" onClick={()=>{this.props.isLogged(false)}}>logout</button>
                <div className="button-container">
                    {this.props.rol.map((role) => (
                        <button>{role}</button>
                            ))}

                        </div>

                        </header>
                        )
                    }


}

export default Header