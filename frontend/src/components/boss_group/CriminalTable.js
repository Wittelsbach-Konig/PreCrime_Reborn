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
            this.updateStatusOnServer(newStatus);
            this.props.onRenew()
        } else {
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
                {showCriminal && (
                    <div className="criminal-statistic">
                        <table className="bg-rg">
                            <tbody>
                            <tr>
                                <td colSpan="2"
                                    className="table-label">
                                    Criminal Info
                                </td>
                            </tr>
                            <tr>
                                <td className="table-label">ID:</td>
                                <td className="table-label">{criminalInfo.id}</td>
                            </tr>
                            <tr>
                                <td className="table-label">Name:</td>
                                <td className="table-label">{criminalInfo.name}</td>
                            </tr>
                            <tr>
                                <td className="table-label">Location:</td>
                                <td className="table-label">{criminalInfo.location}</td>
                            </tr>
                            <tr>
                                <td className="table-label">Weapon:</td>
                                <td className="table-label">{criminalInfo.weapon}</td>
                            </tr>
                            <tr>
                                <td className="table-label">Status:</td>
                                <td className="table-label-edit-rel">
                                    <select
                                        value={newStatus}
                                        onChange={this.handleChangeStatus}>
                                        <option value="">Choose state</option>
                                        <option value="CAUGHT">CAUGHT</option>
                                        <option value="ESCAPED">ESCAPED</option>
                                    </select>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                )}
            <div className="content-container-criminal">
                <div className="table-container-precog">
                    <table className="bg-rg">
                        <thead>
                        <tr>
                            <th className="table-label">Name</th>
                            <th className="table-label">Location</th>
                            <th className="table-label">Weapon</th>
                            <th className="table-label">Group</th>
                        </tr>
                        </thead>
                        <tbody>

                        {criminalList ? (criminalList.map((criminal) => (
                            <tr
                                key={criminal.id}
                                className={criminal.id === selectedRow ? 'selected-row' : ''}
                                onClick={() => this.handleRowClick(criminal.id, criminal)}
                            >
                                <td className="table-label-edit">{criminal.name}</td>
                                <td className="table-label-edit">{criminal.location}</td>
                                <td className="table-label-edit">{criminal.weapon}</td>
                                <td className="table-label-edit"><button onClick={this.getGroup}>to appoint</button></td>
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

            </div>
        );
    }
}

export default CriminalTable;
