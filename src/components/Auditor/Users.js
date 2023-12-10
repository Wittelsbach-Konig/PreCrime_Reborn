import React, { Component } from 'react';
import UserCard from './UserCard'

class Users extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
            userId:null,
            isFormOpen:false
        };
    }

    handleRowClick = (id) => {
        this.setState({ selectedRow: id }, ()=>{
            const token = localStorage.getItem('jwtToken');

            fetch(`http://localhost:8028/api/v1/auditor/users/${id}`, {
                method: 'GET', // или другой метод
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
                },
            })
                .then(responses => responses.json())
                .then(data => {
                    this.setState({userId:data})
                    this.setState({isFormOpen: true})
                })
                .catch(error => {
                    console.error('Ошибка при запросе к серверу:', error);
                });
        });

    };

    closeForm = () => {
        this.setState({ isFormOpen: false });
    };


    render() {
        const { usersList } = this.props;
        const { selectedRow } = this.state;

        return (<div>
            {this.state.isFormOpen &&
                (<UserCard onClose={this.closeForm} userStat={this.state.userId}/>)}
            <div className="crime-content-container">
                <div className="crime-table-container">

                    <table>
                        <thead>
                        <tr>
                            <th>Login</th>
                            <th>Email</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Roles</th>
                            <th>Telegram ID</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {usersList ? (
                            usersList.map((user) => (
                                <tr
                                    key={user.id}
                                    className={user.id === selectedRow ? 'selected-row' : ''}

                                >
                                    <td>{user.login}</td>
                                    <td>{user.email}</td>
                                    <td>{user.firstName}</td>
                                    <td>{user.lastName}</td>
                                    <td>{user.roles.join(', ')}</td>
                                    <td>{user.telegramId}</td>
                                    <td>
                                        <button onClick={() => this.handleRowClick(user.id)}>
                                            More Info
                                        </button>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="7">No data available</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
            </div>
        );
    }
}

export default Users;
