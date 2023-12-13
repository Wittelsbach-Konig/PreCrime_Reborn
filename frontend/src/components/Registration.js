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
            telegramId: 0
        },
            login: "",
            pass: '',
            selectedRole: [],
            confirmPassword: '',
            passwordsMatch: true,
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

    removeChip = (chip) => {
        this.setState({
            selectedRole: this.state.selectedRole.filter((selectedChip) => selectedChip !== chip),
        });
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
        if (user.roles === [] || !user.login || !user.password)
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
                    console.error('Ошибка при запросе к серверу:', error);
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
                            placeholder="Firstname"
                            type="text"
                            name="firstName"
                            value={user.firstName}
                            onChange={this.handleInputChange}
                        />
                        </div>
                        <div className="input-field">
                        <input
                            placeholder="Lastname"
                            type="text"
                            name="lastName"
                            value={user.lastName}
                            onChange={this.handleInputChange}
                        />
                        </div>
                        <div className="input-field">
                        <input
                            placeholder="Login"
                            type="text"
                            name="login"
                            value={user.login}
                            onChange={this.handleInputChange}
                        />
                        </div>
                        <div className="input-field">
                        <input
                            placeholder="email"
                            type="email"
                            name="email"
                            value={user.email}
                            onChange={this.handleInputChange}
                        />
                        </div>
                        <div className="input-field">
                        <i className="fas fa-user"></i>
                        <input
                            placeholder="password"
                            type="password"
                            name="password"
                            value={user.password}
                            onChange={this.handleInputChange}
                        />
                        </div>
                        <div className="input-field">
                        <input
                            placeholder="confirmPassword"
                            type="password"
                            name="confirmPassword"
                            value={user.confirmPassword}
                            onChange={this.handleInputChange}
                        />
                        </div>

                        <div className="input-field">
                <input
                    placeholder="telegramID"
                    type="text"
                    name="telegramId"
                    value={user.telegramId}
                    onChange={this.handleInputChange}
                />
                                            </div>
                                                    <div className="input-field">
                                                    <label> Role:</label>
                                                    <div className="chip-container">
                                                        {this.chips.map((chip) => (
                                                            <button
                                                                type="button"
                                                                key={chip}
                                                                className={`chip ${selectedRole.includes(chip) ? 'selected' : ''}`}
                                                                onClick={() => this.toggleChip(chip)}
                                                            >
                                                                {chip}
                                                            </button>
                                                        ))}
                                                    </div>
                                                    </div>

                                                    <div className="selected-chips-container">

                                                        <label>Choosing roles:</label>
                                                        <ul>
                                                            {selectedRole.map((chip) => (
                                                                <a key={chip}>
                                                                    <button onClick={() => this.removeChip(chip)}>{chip}</button>
                                                                </a>
                                                            ))}
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                        <button className="sumbit" onClick={this.handleSubmit}>
                            <span className="btnText">Submit</span>
                            <i className="uil uil-navigator"></i>
                        </button>
                    <div className="text sign-up-text" >Already have an account? <label
                        htmlFor="flip" onClick={this.onReg}>Login now</label>
                    </div>

                                        </div>
                                    </form>
                                </div>
              </div>
        )
    }



}

export default Registration