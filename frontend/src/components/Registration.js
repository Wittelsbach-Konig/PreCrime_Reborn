import React from "react";
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
            telegramId: 345678
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
        if (!login || !pass || !confirmPassword || user.roles ===[] || !user.firstName || !user.lastName || !user.email || !user.telegramId)
        {
            window.alert(`Please, input all fields`);

        }
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
            <div className="scroll-bar">
                <h2>Registration</h2>
                <h6>Choosing role:</h6>
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
                <div className="selected-chips-container">

                    <h6>Approved roles:</h6>
                    <ul>
                        {selectedRole.map((chip) => (
                            <a key={chip}>
                                <button onClick={() => this.removeChip(chip)}>{chip}</button>
                            </a>
                        ))}
                    </ul>
                </div>

                        <input
                            placeholder="Firstname"
                            type="text"
                            name="firstName"
                            value={user.firstName}
                            onChange={this.handleInputChange}
                        />

                        <input
                            placeholder="Lastname"
                            type="text"
                            name="lastName"
                            value={user.lastName}
                            onChange={this.handleInputChange}
                        />

                        <input
                            placeholder="Login"
                            type="text"
                            name="login"
                            value={user.login}
                            onChange={this.handleInputChange}
                        />

                        <input
                            placeholder="email"
                            type="email"
                            name="email"
                            value={user.email}
                            onChange={this.handleInputChange}
                        />

                        <input
                            placeholder="password"
                            type="password"
                            name="password"
                            value={user.password}
                            onChange={this.handleInputChange}
                        />

                        <input
                            placeholder="confirmPassword"
                            type="password"
                            name="confirmPassword"
                            value={user.confirmPassword}
                            onChange={this.handleInputChange}
                        />

                <input
                    placeholder="telegramID"
                    type="text"
                    name="telegramID"
                    value={user.telegramId}
                    onChange={this.handleInputChange}
                />
                    <button type="button" onClick={this.handleSubmit}>Registration</button>
                    <button type="button" onClick={this.onReg}>Back</button>
                </div>
        )
    }



}

export default Registration