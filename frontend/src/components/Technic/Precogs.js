import React, { Component } from 'react';
import Header from "../Header";
import BurgerMenu from "../BurgerMenu";
import Profile from "../profile";
import PrecogsList from "./PrecogsList";
import VisionList from "../visions/VisionList";

class Precogs extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            showModal_2: false,
            psychics: [],
            visions:[],
            selectedPsychic: null,

        };
    }

    openModal = () => {
        this.setState({ showModal: true });
        this.setState({ showModal_2: false });
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

    closeModal = () => {
        this.setState({ showModal: false});
        this.setState({ showModal_2: true });
        this.fetchPsychics();
    };

    openModal_2 = () => {
        this.setState({ showModal_2: true });
        this.setState({showModal: false});
    };

    closeModal_2 = () => {
        this.setState({showModal_2: false});
        this.setState({showModal: false});
        this.fetchPsychics();
    };

    componentDidMount() {
        this.fetchPsychics();
    }


    fetchPsychics = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/precogs', {
            method: 'GET', // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
        })
            .then(responses => responses.json())
            .then(data => {
                this.setState({ psychics: data });
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
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
        const { selectedPsychic } = this.state;
        const { onChange, me } = this.props
        const visibleRoles = ["DETECTIVE", "AUDITOR", "REACTIONGROUP","TECHNIC"];
        const rolesToDisplay = visibleRoles.filter(role => me.roles.includes(role));
        return (<div>
                <Profile me={me}/>
                <Header pullRole={this.props.pullRole} isLogged={this.props.isLogged} onChange={onChange} rol={me.roles}/>
                <BurgerMenu onClose={this.closeBurger} roles={rolesToDisplay} updatePull={this.props.pullRole}/>

            <div className="frame-but">
                <div className="rectangle-but" />
                <button className="check-vision" onClick={this.openModal}>Visions</button>
                {this.state.showModal && <VisionList onClose={this.closeModal} visions={this.state.visions} onRenew={this.openModal}/>}
                <button className="check-precogs" onClick={this.openModal_2}>Precogs</button>
                {this.state.showModal_2 && <PrecogsList onClose={this.closeModal_2} onRenew={this.fetchPsychics} />}
            </div>



                <div className="frame-2">
                    <div className="rectangle-2" />
                </div>
            </div>
        );
    }
}

export default Precogs;
