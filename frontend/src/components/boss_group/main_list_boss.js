import React from "react";
import Header from "../Header"
import Profile from "../profile"
import ReactionGroup from "./ReactionGroup";
import Ammunition from "./Ammunition";
import Criminal from "./Criminal"
import Transport from "./Transport";
import BurgerMenu from "../BurgerMenu";
class main_list_boss extends React.Component {
    constructor(props) {

        super(props);

        this.state = {
            showReactionGroup: false,
            showTransport: false,
            showAmmunition: false,
            showCriminal: false,
            showGroupWork:false,
            transport:[],
            groupList:[],
            ammunition:[],
            criminals:null,
            pullRole: {"DETECTIVE": false,
                "TECHNIC": false,
                "AUDITOR": false,
                "REACTIONGROUP":false},
        }
        this.showAm = this.showAm.bind(this);

    }

    showGR = () => {
        const token = localStorage.getItem('jwtToken');

        this.state.showGroupWork ?
            (fetch('http://localhost:8028/api/v1/reactiongroup/all', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
                },
            })
                .then(responses => responses.json())
                .then(data => {
                    this.setState({groupList:data})
                    console.log(this.state.groupList)
                })
                .catch(error => {
                    console.error('Ошибка при запросе к серверу:', error);
                }))
            :
            (fetch('http://localhost:8028/api/v1/reactiongroup/allworking', {
                method: 'GET', // или другой метод
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
                },
            })
                .then(responses => responses.json())
                .then(data => {
                    this.setState({groupList:data})
                    console.log(this.state.groupList)
                })
                .catch(error => {
                    console.error('Ошибка при запросе к серверу:', error);
                }))


        this.setState({showReactionGroup: true})
        this.setState({showTransport: false})
        this.setState({showAmmunition: false})
        this.setState({showCriminal:false})



    };

    async showAm () {
       const token = localStorage.getItem('jwtToken');
       try {
               const response = await fetch('http://localhost:8028/api/v1/reactiongroup/supply', {
                   method: 'GET',
                   headers: {
                       'Content-Type': 'application/json',
                       'Authorization': `Bearer ${token}`,
                   },
               });
               if (!response.ok) {
                   throw new Error('Network response was not ok');
               }

        const data = await response.json();

        this.setState({ ammunition: data }, () => {
            // После успешного обновления состояния
            console.log(this.state.ammunition);

            this.setState({
                showAmmunition: true,
                showReactionGroup: false,
                showTransport: false,
                showCriminal: false,
            });
        });
        } catch (error) {
        console.error('Ошибка при запросе к серверу:', error);
        }



    };

    showTr  = () => {
        const token = localStorage.getItem('jwtToken');

        fetch('http://localhost:8028/api/v1/reactiongroup/transport', {
            method: 'GET', // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(responses => responses.json())
            .then(data => {
                this.setState({transport:data})
                console.log(this.state.transport)
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });


        this.setState({showReactionGroup: false})
        this.setState({showTransport: true})
        this.setState({showAmmunition: false})
        this.setState({showCriminal:false})



    };

    showCr = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/reactiongroup/criminal', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
        })
            .then(responses => responses.json())
            .then(data => {
                this.setState({criminals:data})
                console.log(this.state.criminals)
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
        this.setState({showReactionGroup: false})
        this.setState({showTransport: false})
        this.setState({showAmmunition: false})
        this.setState({showCriminal:true})

    };

    updateState = (newValue) => {
        this.setState({ showGroupWork: newValue }, ()=>{
            this.showGR()
        });

    };


    render() {
        const {onChange, me} = this.props
        const {showReactionGroup, showTransport, showAmmunition, showCriminal} = this.state
        const visibleRoles = ["DETECTIVE", "AUDITOR", "REACTIONGROUP","TECHNIC"];
        const rolesToDisplay = visibleRoles.filter(role => me.roles.includes(role));
        return ( <div>
                <Header
                    pullRole={this.props.pullRole}
                    isLogged={this.props.isLogged}
                    onChange={onChange}
                    rol={me.roles}
                    me={me}
                />
                <BurgerMenu
                    onClose={this.closeBurger}
                    roles={rolesToDisplay}
                    updatePull={this.props.pullRole}
                />
                <div className="frame-but">
                    <div className="rectangle-but">
                    <button
                        className="card-list"
                        type="button"
                        onClick={this.showAm}>
                        Ammunition
                    </button>
                    <button
                        className="card-list"
                        type="button"
                        onClick={this.showGR}>
                        Reaction Group
                    </button>
                    <button
                        className="card-list"
                        type="button"
                        onClick={this.showTr}>
                        Transport
                    </button>
                    <button
                        className="card-list"
                        type="button"
                        onClick={this.showCr}>
                        Criminal Info
                    </button>
                    </div>
                </div>
                <div className="frame-2">
                    <div className="rectangle-2">
                        {showAmmunition ? (
                            <div>
                                <Ammunition
                                    ammun={this.state.ammunition}
                                    renew={this.showAm}
                                    onChange={this.updateStateAm}
                                />
                            </div>
                        ) : (
                            <div>
                            </div>
                        )}

                        {showReactionGroup ? (
                            <div>
                                <ReactionGroup
                                    group={this.state.groupList}
                                    renew={this.showGR}
                                    onChange={this.updateState}/>
                            </div>
                        ) : (
                            <div>
                            </div>
                        )}

                        {showTransport ? (
                            <div>
                                <Transport
                                    trans={this.state.transport}
                                    renew={this.showTr}
                                />
                            </div>
                        ) : (
                            <div>
                            </div>
                        )}

                        {showCriminal ? (
                            <div>
                                <Criminal
                                    criminals={this.state.criminals}
                                    renew={this.showCr}
                                    onChange={this.updateState}
                                />
                            </div>
                        ) : (
                            <div>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        )
    }
}

export default main_list_boss