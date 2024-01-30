import React from "react";
import Header from "../Header"
import ReactionGroup from "./ReactionGroup";
import Ammunition from "./Ammunition";
import CriminalTable from "./CriminalTable";
import TableTransport from "./TableTransport";
import BurgerMenu from "../BurgerMenu";
class main_list_boss extends React.Component {
    constructor(props) {

        super(props);

        this.state = {
            showReactionGroup: false,
            showTransport: false,
            showAmmunition: false,
            showCriminal: false,
            showGroupWork: false,
            transport: [],
            groupList: [],
            ammunition: [],
            criminals: [],
            pullRole: {
                "DETECTIVE": false,
                "TECHNIC": false,
                "AUDITOR": false,
                "REACTIONGROUP": false
            },
        }

    }

    showGR = () => {
        const token = localStorage.getItem('jwtToken');

        this.state.showGroupWork ?
            (fetch('api/v1/reactiongroup/all', {
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
            (fetch('api/v1/reactiongroup/allworking', {
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

    showAm = ()=> {
       const token = localStorage.getItem('jwtToken');
        fetch('api/v1/reactiongroup/supply', {
            method: 'GET', // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
        })
            .then(responses => responses.json())
            .then(data => {
                this.setState({ ammunition: data })
                console.log(data)
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            })

        this.setState({showReactionGroup: false})
        this.setState({showTransport: false})
        this.setState({showAmmunition: true})
        this.setState({showCriminal:false})


    };

    showTr  = () => {


        this.setState({showReactionGroup: false})
        this.setState({showTransport: true})
        this.setState({showAmmunition: false})
        this.setState({showCriminal:false})



    };

    showCr = () => {
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
                                    onChange={this.updateState}
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
                                <TableTransport
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
                                <CriminalTable
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