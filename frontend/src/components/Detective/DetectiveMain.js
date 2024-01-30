import React from "react";
import Header from "../Header";
import ListCards from "../crime_card/ListCards";
import BurgerMenu from "../BurgerMenu";
import CrimeForm from "../crime_card/CreateCard";
import Criminals from "./Criminals";

class DetectiveMain extends React.Component {
    constructor(props) {

        super(props);
        this.state = {
            showModal: false,
            showModal_2:false,
            showBurger: false,
            showCard:false,
            cards:null,
            visions:null
        }

    }

    openModal = () => {
        this.setState({ showModal: true });
        this.setState({ showModal_2: false })
        const token = localStorage.getItem('jwtToken');

        fetch('api/v1/cards', {
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

    CreateCarted = (e) => {
        this.setState({showCard: e})
        this.fetchVisions()
    }

    CloseCarted = () => {
        this.setState({showCard: false})
    }


    openCriminals = () => {
        this.setState({showModal_2: true})
        this.setState({showModal: false})
    }

    closeBurger = () => {
        this.setState({showBurger: false});
        this.props.renew();
    };

    fetchVisions = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('api/v1/visions', {
            method: 'GET', // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
        })
            .then(responses => responses.json())
            .then(data => {
                this.setState({ visions: data });
                console.log(this.state.visions)
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
    };




    render() {
        const {onChange, me} = this.props
        const visibleRoles = ["DETECTIVE", "AUDITOR", "REACTIONGROUP","TECHNIC"];
        const rolesToDisplay = visibleRoles.filter(role => me.roles.includes(role));
        return (<div>
                <Header isLogged={this.props.isLogged} me={me}/>
                <BurgerMenu onClose={this.closeBurger} roles={rolesToDisplay} updatePull={this.props.pullRole}/>

                {this.state.showCard && (
                    <div>
                        <CrimeForm onClose={this.CloseCarted} visions={this.state.visions}/>
                    </div>
                )}




                <div className="frame-2">
                    <div className="rectangle-2">
                        {this.state.showModal && (
                                <ListCards onClose={this.closeModal}
                                           crimeList={this.state.cards}
                                           onRenew={this.openModal}
                                           role={"DETECTIVE"}/>
                        )}
                        {this.state.showModal_2 && (
                            <div>
                                <Criminals onClose={this.closeModal}
                                           crimeList={this.state.cards}
                                           onRenew={this.openModal}
                                           role={"DETECTIVE"}/>
                            </div>
                        )}
                    </div>
                </div>
                <div className="frame-but">
                    <div className="rectangle-but" >
                        <button className="card-list" onClick={this.openModal}>Card List</button>
                        <button onClick={()=>{this.CreateCarted(true)}} className="create-card">Create Crime Card</button>
                        <button className="card-list" onClick={this.openCriminals}>Criminals</button>
                    </div>
                </div>
                </div>
        )
    }
}

export default DetectiveMain