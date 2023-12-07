import React from "react";
import Registration from "./Registration";
class AddUser extends React.Component {
    userAdd = {}
    constructor(props) {
        super(props);
        this.state = {
            login: "",
            password: "",
            roles: [],
            isReg: true,
            dataFormReg: []

        }
        this.registration = this.registration.bind(this)
    }

    render() {
        return (
            <form ref={(el)=> this.myForm = el}>
                {this.state.isReg ? (<div>
                    <input placeholder="Login" onChange={(e) => this.setState({login: e.target.value})}/>
                    <input placeholder="Password" onChange={(e) => this.setState({password: e.target.value})}/>
                    <button type="button" onClick={()=> {
                    this.myForm.reset()
                    this.userAdd = {
                    login: this.state.login,
                    password: this.state.password,
                }
                    if(this.props.user)
                    this.userAdd.id = this.props.user.id
                    this.props.onAdd(this.userAdd)}}>
                Login</button>
                    <button type="button" onClick={this.registration}>Registration</button>
                </div>) :
                    (
                        <Registration onReg={this.registration} />
                        )

                }

            </form>
        )
    }

    registration(data){
        this.setState({isReg: !this.state.isReg})
        this.setState({ dataFormReg: data }, () => {
        });

    }
}

export default AddUser