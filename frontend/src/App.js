import React, {useState} from "react";
import Header from "./components/Header"
import Users from "./components/Users";
import Registration from "./components/Registration";
import Detective from "./components/Detective/DetectiveMain";
import ListBoss from "./components/boss_group/main_list_boss"
import Precogs from "./components/Technic/Precogs"

import Image from "./components/image";
import logo from "./img/logo.png"
import axios from "axios";
import Administrator from "./components/Admin/Administrator";
import Auditor from "./components/Auditor/Auditor";


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
            //const data = await axios.get('http://localhost:8028/api/v1/me')

            if (response.data.accessToken) {
                localStorage.setItem('jwtToken', response.data.accessToken);
                console.log('Token:', response.data.accessToken);
                // Отправка запроса с токеном в заголовке Authorization
                fetch('http://localhost:8028/api/v1/me', {
                    method: 'GET', // или другой метод
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${response.data.accessToken}`, // Добавляем токен в заголовок
                    },
                })
                    .then(responses => responses.json())
                    .then(data => {
                        this.state.me = data;
                        console.log(this.state.me)
                        this.chekRole(this.state.pullRole)
                    })
                    .catch(error => {
                        console.error('Ошибка при запросе к серверу:', error);
                    });
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
        const rolesToDisplay = ["DETECTIVE", "TECHNIC", "AUDITOR", "REACTIONGROUP"];
        rolesToDisplay.map((lrole) => {
            pullRole[lrole] = false
        })

    };

    updateRole = (newValue) => {
        this.setState({ pullRole: newValue });
    };

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
                return(<aside>
                    <form>
                        <Registration onReg={this.registration} />
                    </form>
                </aside>)
            }
            else
            {
                return(

                    <aside>
                        <form onSubmit={this.handleSubmit}>
                        <h2>Login</h2>
                            <input
                                placeholder="Login"
                                type="text"
                                name="login"
                                value={log.login}
                                onChange={this.handleInputChange}
                            />

                            <input
                                placeholder="Password"
                                type="text"
                                name="password"
                                value={log.password}
                                onChange={this.handleInputChange}
                            />
                            <button type="button" onClick={this.handleSubmit}>Enter</button>
                            <button type="button" onClick={this.registration}>Registration</button>
                        </form>
                    </aside>

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

    chekRole(role){
        const { me } = this.state
        const key = me.roles[0]
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