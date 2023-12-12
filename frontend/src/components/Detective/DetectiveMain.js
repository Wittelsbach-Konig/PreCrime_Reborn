import React from "react";
import Header from "../Header";
import Profile from "../profile";
import ListCards from "../crime_card/ListCards";
import BurgerMenu from "../BurgerMenu";
import HeaderCards from "../crime_card/HeaderCards";
import CrimeForm from "../crime_card/CreateCard";
class DetectiveMain extends React.Component {
    constructor(props) {

        super(props);
        this.state = {
            showModal: false,
            showBurger: false,
            showCard:false,
            cards:null,
            visions:null
        }

    }

    openModal = () => {
        this.setState({ showModal: true });
        const token = localStorage.getItem('jwtToken');

        fetch('http://localhost:8028/api/v1/cards', {
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

    openBurger = () => {
        this.setState({ showBurger: true });
    };

    closeBurger = () => {
        this.setState({showBurger: false});
        this.props.renew();
    };

    fetchVisions = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/visions', {
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
                <Header isLogged={this.props.isLogged}/>
                <BurgerMenu onClose={this.closeBurger} roles={rolesToDisplay} updatePull={this.props.pullRole}/>
                {!this.state.showModal && (<div>



                </div>)
                }


                {this.state.showCard && (
                    <div>
                        <CrimeForm onClose={this.CloseCarted} visions={this.state.visions}/>
                    </div>
                )}

                {this.state.showModal && (
                    <div>
                        <h1 className="card-text">Card List</h1>
                    <ListCards onClose={this.closeModal} crimeList={this.state.cards} onRenew={this.openModal} role={"DETECTIVE"}/>
                    </div>
                )}

            <div className="frame-but">
                <div className="rectangle-but" />
                <button className="card-list" onClick={this.openModal}>Open List Card</button>
                <button onClick={()=>{this.CreateCarted(true)}} className="create-card">Create Crime Card</button>
            </div>
                <div className="frame-2">
                    <div className="rectangle-2" />


                </div>
                </div>
        )
    }
}

export default DetectiveMain