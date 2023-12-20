import React, { Component } from 'react';
import UserCard from './UserCard'
import "../../css/CrimeTable.css"
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
            <div className="user-content-container">
                <div className="user-table-container">
                    <table className="bg-rg">
                        <thead>
                        <tr>
                            <th className="table-label">Login</th>
                            <th className="table-label">First Name</th>
                            <th className="table-label">Last Name</th>
                            <th className="table-label">Roles</th>
                            <th className="table-label">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {usersList ? (
                            usersList.map((user) => (
                                <tr
                                    key={user.id}
                                    className={user.id === selectedRow ? 'selected-row' : ''}

                                >
                                    <td className="table-label-pr">{user.login}</td>
                                    <td className="table-label-pr">{user.firstName}</td>
                                    <td className="table-label-pr">{user.lastName}</td>
                                    <td className="table-label-pr">{user.roles.join(', ')}</td>
                                    <td className="table-label-edit">
                                        <button onClick={() => this.handleRowClick(user.id)}>
                                            Info
                                        </button>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="5" className="table-label-pr">No data available</td>
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
