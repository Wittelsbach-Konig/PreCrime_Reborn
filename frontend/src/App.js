import React, {useState} from "react";

import Registration from "./components/Registration";
import Detective from "./components/Detective/DetectiveMain";
import ListBoss from "./components/boss_group/main_list_boss"
import Precogs from "./components/Technic/Precogs"

import axios from "axios";
import Administrator from "./components/Admin/Administrator";
import Auditor from "./components/Auditor/Auditor";

import "./css/Login.css"


class App extends React.Component  {
    constructor(props) {

        super(props);

        this.state = {
            users: [],
            log:{
                login: "",
                password: ""
            },
            me:{
              email:'',
              firstName: '',
              id:0,
              lastName: '',
              login:'',
              roles:[],
              telegramId: 0
            },
            isLoggedIn: false,
            isReg: true,
            checkList: false,
            currentRole: "",
            adm:false,
            pullRole: {"DETECTIVE": false,
                "TECHNIC": false,
                "AUDITOR": false,
                "REACTIONGROUP":false}

        }
        //const [isShown, setIsShown] = useState(false);
        //const [value, valueChange] = useState(0)
        this.addUser = this.addUser.bind(this)
        this.registration = this.registration.bind(this)
        this.chekRole = this.chekRole.bind(this)
    }

    handleInputChange = (e) => {
        const { name, value } = e.target;
        this.setState((prevState) => ({
            log: {
                ...prevState.log,
                [name]: value,
            },
        }));
    };

    handleRoleChange = (e) => {
        const { name, value } = e;
        if (name === "ADMIN")
        {this.setState({adm:true})}
        else {
            this.setState((prevState) => ({
                pullRole: {
                    ...prevState.pullRole,
                    [name]: value,
                },
            }));
        }
    };

    handleSubmit = async (e) => {
        e.preventDefault();

        // Проверка логина и пароля
        console.log(this.state.log)

        try {
            const response = await axios.post('http://localhost:8028/api/v1/auth/login', this.state.log);
            console.log('Ответ сервера:', response.data);

            if (response.data.accessToken) {
                localStorage.setItem('jwtToken', response.data.accessToken);
                console.log('Token:', response.data.accessToken);
                this.fetchRole(response.data.accessToken)
            }
        } catch (error) {
            console.error('Ошибка при запросе:', error);
        }
    };



    handleChange = (newValue) => {
        this.setState({currentRole:newValue});
    };
    updateState = (newValue) => {
        const {pullRole} = this.state
        this.setState({ isLoggedIn: newValue });
        this.setState({adm:false})
        this.setState({isReg:true})
        localStorage.setItem('jwtToken', null)
        const rolesToDisplay = ["DETECTIVE", "TECHNIC", "AUDITOR", "REACTIONGROUP"];
        rolesToDisplay.map((lrole) => {
            pullRole[lrole] = false
        })

    };

    updateRole = (newValue) => {
        this.setState({ pullRole: newValue });
    };

    async componentDidMount() {
        const jwtToken = localStorage.getItem('jwtToken');

        if (jwtToken) {
            await this.setRoles();
        }
    }

    async setRoles() {
        const rolesToDisplay = ["DETECTIVE", "TECHNIC", "AUDITOR", "REACTIONGROUP"];
        const curRole = localStorage.getItem('curRole');
        const { pullRole } = this.state;

        rolesToDisplay.forEach((lrole) => {
            pullRole[lrole] = lrole === curRole;
        });

            // Если ни одна роль не совпала, устанавливаем значение по умолчанию
            if (!rolesToDisplay.includes(curRole)) {
                this.setState({isLoggedIn: false});
                console.log(this.state.isReg)
            } else {
                //this.setState({ isLoggedIn: true });

                await this.fetchRole();
            }
        console.log(this.state.isReg)
        console.log(this.state.isLoggedIn)
        console.log(pullRole);
    }

    async fetchRole() {
        const jwtToken = localStorage.getItem('jwtToken');

        try {
            const response = await fetch('http://localhost:8028/api/v1/me', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${jwtToken}`,
                },
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const data = await response.json();
            this.setState({ me: data }, () => {
                console.log(this.state.me);
                this.chekRole(this.state.pullRole);
            });

        } catch (error) {
            console.error('Ошибка при запросе к серверу:', error);
        }
    }



    render() {
        const {pullRole, log, me, adm} = this.state;
        if (this.state.isLoggedIn)
        {
            if (adm)
            {
                return (<div>
                    <Administrator pullRole={this.updateRole} isLogged={this.updateState}
                              onChange={this.handleRoleChange} me={me}/>
                </div>)
            }
            else {
                if (pullRole.REACTIONGROUP) {
                    return (<div>
                        <ListBoss pullRole={this.updateRole} isLogged={this.updateState}
                                  onChange={this.handleRoleChange} me={me}/>
                    </div>)

                }
                if (pullRole.DETECTIVE) {
                    return (<div>
                        <Detective pullRole={this.updateRole} isLogged={this.updateState}
                                   onChange={this.handleRoleChange} me={me}/>
                    </div>)

                }
                if (pullRole.TECHNIC) {
                    return (<div>
                        <Precogs pullRole={this.updateRole} isLogged={this.updateState} onChange={this.handleRoleChange}
                                 me={me}/>
                    </div>)

                }
                if (pullRole.AUDITOR) {
                    return (<div>
                        <Auditor pullRole={this.updateRole} isLogged={this.updateState} onChange={this.handleRoleChange}
                        me={me}/>
                    </div>)

                }
            }
        }
        else
        {
            if (!this.state.isReg){
                return(<div>
                        <Registration onReg={this.registration} />
                </div>)
            }
            else
            {
                return(
                    <div className="local-body">
                    <div className="container">
                        <div className="cover"></div>

                                <div className="forms">
                                    <div className="form-content">

                                <div className="login-form">
                                    <div className="title">Login</div>
                                    <form action="#">
                                        <div className="input-boxes">
                                            <div className="input-box">
                                                <i className="fas fa-envelope"></i>
                            <input
                                placeholder="Login"
                                type="text"
                                name="login"
                                value={log.login}
                                onChange={this.handleInputChange}
                                required
                            />
                                            </div>
                                            <div className="input-box">
                                                <i className="fas fa-lock"></i>
                            <input
                                placeholder="Password"
                                type="password"
                                name="password"
                                value={log.password}
                                onChange={this.handleInputChange}
                            />
                                            </div>
                                            <div className="button input-box">
                                            <button type="button" onClick={this.handleSubmit}>Submit</button>
                                                {//<button type="button" onClick={this.registration}>Registration</button>
                                                }
                                            </div>
                                            <div className="text sign-up-text">Don't have an account? <label
                                                htmlFor="flip" onClick={this.registration}>Signup now</label></div>

                                        </div>
                                    </form>
                                </div>
                            </div>
                    </div>
                    </div>
                    </div>
                    )

        }
    }
    }
    addUser(user){
        const id = this.state.users.length + 1
        this.setState({users: [...this.state.users, {id, ...user}]}, ()=>{console.log(this.state.users)})

    }

    registration(data) {
        this.setState({isReg: !this.state.isReg})

        this.setState({dataFormReg: data}, () => {
        })

    };

    async chekRole(){
        const { me } = this.state
        if(me)
        {
            this.setState({isLoggedIn:!this.state.isLoggedIn})
            const e = {name: me.roles[0], value: true}
            this.handleRoleChange(e)
            console.log(this.state.pullRole)
        }
    }

}


export default App