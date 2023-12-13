import React from "react";
import Header from "../Header";
import ListCards from "../crime_card/ListCards";
import BurgerMenu from "../BurgerMenu";
import Users from "./Users"
class DetectiveMain extends React.Component {
    constructor(props) {

        super(props);
        this.state = {
            showModal: false,
            showBurger: false,
            showUsers:false,
            cards:null,
            users:null
        }

    }

    openModal = () => {
        this.setState({ showModal: true });
        this.setState({ showUsers: false});
        const token = localStorage.getItem('jwtToken');

        fetch('http://localhost:8028/api/v1/auditor/cards', {
            method: 'GET', // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
        })
            .then(responses => responses.json())
            .then(data => {
                this.setState({cards:data})
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });

    };

    closeModal = () => {
        this.setState({showModal: false});
        this.props.renew();
    };

    showUsers = () => {
        this.setState({showUsers: true})
        this.setState({showModal: false});
        const token = localStorage.getItem('jwtToken');

        fetch('http://localhost:8028/api/v1/auditor/users', {
            method: 'GET', // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
        })
            .then(responses => responses.json())
            .then(data => {
                this.setState({users:data})
                console.log(this.state.users)
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });

    }

    closeUsers = () => {
        this.setState({showUsers: false})
    }

    openBurger = () => {
        this.setState({ showBurger: true });
    };

    closeBurger = () => {
        this.setState({showBurger: false});
        this.props.renew();
    };

    showAm = () => {

    };


    render() {
        const {onChange, me} = this.props
        const visibleRoles = ["DETECTIVE", "AUDITOR", "REACTIONGROUP","TECHNIC"];
        const rolesToDisplay = visibleRoles.filter(role => me.roles.includes(role));
        return (<div>
                <Header isLogged={this.props.isLogged} me={me}/>
                <BurgerMenu onClose={this.closeBurger} roles={rolesToDisplay} updatePull={this.props.pullRole}/>
                {!this.state.showModal && (<div>



                </div>)
                }

                {this.state.showUsers && (
                    <div>
                        <h1 className="card-text">User List</h1>
                        <Users onClose={this.closeUsers} usersList={this.state.users} onRenew={this.showUsers} role={"AUDITOR"}/>
                    </div>
                )}

                {this.state.showModal && (
                    <div>
                        <h1 className="card-text">Card List</h1>
                        <ListCards onClose={this.closeModal} crimeList={this.state.cards} onRenew={this.openModal} role={"AUDITOR"}/>
                    </div>
                )}

                <div className="frame-but">
                    <div className="rectangle-but" />
                    <button className="card-list" onClick={this.openModal}>Cards List</button>
                    <button onClick={()=>{this.showUsers()}} className="create-card">Users</button>
                </div>
                <div className="frame-2">
                    <div className="rectangle-2" />


                </div>
            </div>
        )
    }
}

export default DetectiveMain