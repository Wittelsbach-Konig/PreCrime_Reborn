import React, { Component } from 'react';
import AppointGroup from "./AppointGroup";

class CriminalTable extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
            showCriminal: false,
            criminalInfo: false,
            showGroup: false,
            groupList:null,
            newStatus: ''
        };
    }


    handleRowClick = (index, criminal) => {
        this.setState({ selectedRow: index }, ()=>{
            console.log(index)
            this.setState({showCriminal:true})
            this.setState({criminalInfo:criminal})
        });

    };

    closeGroup = () => {
        this.setState({ showGroup: false })

    };

    getGroup = () => {
        this.setState({ showGroup: true })
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/reactiongroup/all', {
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
            })
    };

    handleChangeStatus = (event) => {
        const newStatus = event.target.value;
        const confirmation = window.confirm(`Вы уверены, что хотите изменить статус на ${newStatus}?`);

        if (confirmation) {
            // Вызовите метод для изменения статуса на сервере
            this.updateStatusOnServer(newStatus);
            this.props.onRenew()
        } else {
            // Если пользователь отказался от изменения статуса, сбросьте выбор в выпадающем меню
            this.setState({ newStatus: '' });
        }
    };

    updateStatusOnServer = (newStatus) => {
        const {criminalInfo} = this.state
        const token = localStorage.getItem('jwtToken');
        console.log(newStatus)
        fetch(`http://localhost:8028/api/v1/reactiongroup/criminal/${criminalInfo.id}?status=${newStatus}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(responses => responses.json())
            .then(data => {
                console.log(data)
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
    };


    render() {
        const { criminalList, onRenew } = this.props;
        const { selectedRow, showCriminal, criminalInfo, newStatus } = this.state;
        return (
            <div>
                {this.state.showGroup && <AppointGroup onClose={this.closeGroup} groupList={this.state.groupList}
                                                       idCr={this.state.criminalInfo.id} onRenew={onRenew}/>}
            <div className="content-container-group">
                <div className="table-container-group">
                    <table>
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Location</th>
                            <th>Weapon</th>
                            <th>Status</th>
                            <th>Assign a group</th>
                        </tr>
                        </thead>
                        <tbody>

                        {criminalList ? (criminalList.map((criminal) => (
                            <tr
                                key={criminal.id}
                                className={criminal.id === selectedRow ? 'selected-row' : ''}
                                onClick={() => this.handleRowClick(criminal.id, criminal)}
                            >
                                <td>{criminal.name}</td>
                                <td>{criminal.location}</td>
                                <td>{criminal.weapon}</td>
                                <td>{criminal.status}</td>
                                <td><button onClick={this.getGroup}>to appoint</button></td>
                            </tr>
                        )))
                        : (<tr>
                                <td colSpan="5">No criminal data available</td>
                            </tr>)
                        }
                        </tbody>
                    </table>
                </div>
            </div>
                {showCriminal && (
                    <div className="man-statistic">
                        <h3>Criminal</h3>
                        <p>ID: {criminalInfo.id}</p>
                        <p>Name: {criminalInfo.name}</p>
                        <p>Location: {criminalInfo.location}</p>
                        <p>Weapon: {criminalInfo.weapon}</p>
                        <p>Status:
                            <select value={newStatus} onChange={this.handleChangeStatus}>
                                <option value="">Выберите статус</option>
                                <option value="CAUGHT">CAUGHT</option>
                                <option value="ESCAPED">ESCAPED</option>
                            </select>
                        </p>
                    </div>
                )}
            </div>
        );
    }
}

export default CriminalTable;
