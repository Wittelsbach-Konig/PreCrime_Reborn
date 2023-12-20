import React from "react";
import "../css/Reg_style.css"
class Registration extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            user: {
            login: "",
            password: "",
            confirmPassword: "",
            email: "",
            firstName: "",
            lastName: "",
            roles: [],
            telegramId: ''
        },
            login: "",
            pass: '',
            selectedRole: [],
            confirmPassword: '',
            passwordsMatch: true
        }
        this.chips = ['DETECTIVE', 'TECHNIC', 'AUDITOR', 'REACTIONGROUP'];
    }

    handleInputChange = (e) => {
        const { name, value } = e.target;
        this.setState((prevState) => ({
            user: {
                ...prevState.user,
                [name]: value,
            },
        }));
    };



    toggleChip = (chip) => {
        const { selectedRole, user } = this.state;
        if (selectedRole.includes(chip)) {
            this.setState({
                selectedRole: selectedRole.filter((selectedChip) => selectedChip !== chip),
            });
        } else {
            this.setState({
                selectedRole: [...selectedRole, chip],
            });
        }
    };


    onReg = () => {
        const { selectedRole, pass, login  } = this.state;
        const data =  [login,pass,selectedRole]
        this.props.onReg({data});
    };

    handleSubmit = async (e) => {
        e.preventDefault();
        const {login,
                pass,
                confirmPassword,
                user
        } = this.state
        this.state.user.roles = this.state.selectedRole
        console.log('Отправленные данные:', this.state.user);
        if (this.state.selectedRole.length===0 || !user.login || !user.password || !user.confirmPassword || !user.firstName
        || !user.lastName || !user.telegramId)
        {window.alert("There's no good idea)")}
        else {
            fetch('http://localhost:8028/api/v1/auth/registration', {
                method: 'POST', // или другой метод
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer`
                },
                body: JSON.stringify(this.state.user),
            })
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    this.props.onReg()
                })
                .catch(error => {
                });
        }
    };

    render() {
        const { user } = this.state;
        const { selectedRole, pass, confirmPassword, passwordsMatch  } = this.state;
        return (
              <div className="local-body">
                <div className="container">
                                    <header>Registration</header>

                                    <form action="#">
                                        <div className="form first">
                                            <div className="details personal">
                                                <span className="title">Personal Details</span>
                                                <div className="fields">
                        <div className="input-field">
                        <input
                            maxLength="20"
                            placeholder="Firstname"
                            type="text"
                            name="firstName"
                            value={user.firstName}
                            onChange={this.handleInputChange}
                            required
                        />
                        </div>
                        <div className="input-field">
                        <input
                            maxLength="20"
                            placeholder="Lastname"
                            type="text"
                            name="lastName"
                            value={user.lastName}
                            onChange={this.handleInputChange}
                            required
                        />
                        </div>
                        <div className="input-field">
                        <input
                            maxLength="30"
                            placeholder="Login"
                            type="text"
                            name="login"
                            value={user.login}
                            onChange={this.handleInputChange}
                            required
                        />
                        </div>
                        <div className="input-field">
                        <input
                            maxLength="30"
                            placeholder="Email"
                            type="email"
                            name="email"
                            value={user.email}
                            onChange={this.handleInputChange}
                            required
                        />
                        </div>
                        <div className="input-field">
                        <i className="fas fa-user"></i>
                        <input
                            maxLength="25"
                            placeholder="Password"
                            type="password"
                            name="password"
                            value={user.password}
                            onChange={this.handleInputChange}
                            required
                        />
                        </div>
                        <div className="input-field">
                        <input
                            maxLength="25"
                            placeholder="Confirm password"
                            type="password"
                            name="confirmPassword"
                            value={user.confirmPassword}
                            onChange={this.handleInputChange}
                            required
                        />
                        </div>

                        </div>
                                                <div className="input-field">

                                                    <input
                                                        maxLength="40"
                                                        className="telegram-input"
                                                        placeholder="Telegram ID"
                                                        type="text"
                                                        name="telegramId"
                                                        value={user.telegramId}
                                                        onChange={this.handleInputChange}
                                                    />
                                                </div>
                                                <label className="text-roles">Roles</label>
                                                        <div className="chip-container">
                                                            {this.chips.map((chip) => (
                                                                <label key={chip} className={`chip ${selectedRole && selectedRole.includes(chip) ? 'selected' : ''}`}>
                                                                    {chip}
                                                                    <input
                                                                        type="checkbox"
                                                                        checked={selectedRole.includes(chip)}
                                                                        onChange={() => this.toggleChip(chip)}
                                                                    />

                                                                </label>

                                                            ))}

                                                </div>
                                            </div>
                        <div className="sumbit">
                            <button onClick={this.handleSubmit}>
                            <span className="btnText">Submit</span>
                            <i className="uil uil-navigator"></i>
                        </button>
                        </div>
                                            <div className="already-log">
                    <div className="text sign-up-text" >Already have an account? <label
                        htmlFor="flip" className="Login-now" onClick={this.onReg}>Login now</label>
                    </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
              </div>
        )
    }



}

export default Registration