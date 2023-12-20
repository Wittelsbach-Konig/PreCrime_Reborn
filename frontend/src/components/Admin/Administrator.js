import React, { Component } from 'react';
import Header from "../Header";
import BurgerMenu from "../BurgerMenu";
import Profile from "../profile";
import AddVision from "../visions/AddVision";
import Users from "../Auditor/Users";

class Administrator extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            showModal_2: false,
            psychics: [],
            visions:[],
            selectedPsychic: null,
            users:null

        };
    }

    openModal = () => {
        this.setState({ showModal: true });
        this.setState({ showModal_2: false });

    };

    closeModal = () => {
        this.setState({ showModal: false});
        this.setState({ showModal_2: false });
    };
    openModal_2 = () => {
        this.setState({ showModal: false });
        this.setState({ showModal_2: true });
        this.showUsers()
    };

    showUsers = () => {
        this.setState({showUsers: true})
        this.setState({showModal: false});
        const token = localStorage.getItem('jwtToken');

        fetch('http://localhost:8028/api/v1/admin/users', {
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





    render() {
        const { selectedPsychic } = this.state;
        const { onChange, me } = this.props
        return (<div>
                <Header pullRole={this.props.pullRole} isLogged={this.props.isLogged} onChange={onChange} rol={me.roles}/>

                <div className="frame-but">
                    <div className="rectangle-but" >
                        <button className="card-list" onClick={this.openModal}>Add Vision</button>
                        <button className="card-list" onClick={this.openModal_2}>Users</button>
                    </div>
                   </div>
                {this.state.showModal && <AddVision onClose={this.closeModal}/>}


                <div className="frame-2">
                    <div className="rectangle-2" >
                        {this.state.showModal_2 && (<div>
                            <header className="header-pr">
                                <button className="acc-vis" onClick={this.openModal}>Update</button>
                                <button className="del-vis" onClick={this.retire} >Delete</button>
                            </header>
                            <Users usersList={this.state.users}/>
                        </div>)}
                    </div>
                </div>
            </div>
        );
    }
}

export default Administrator;
