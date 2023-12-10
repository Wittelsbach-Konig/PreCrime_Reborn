import React, { Component } from 'react';
import '../../css/CrimeCard.css';

class UserCard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isEditing: false,
            message: "",
            selectedStatistic: "bossReactGroupStatistic", // По умолчанию выбраны данные bossReactGroupStatistic
            statistics: {
                bossReactGroupStatistic: {
                    id: 0,
                    arresstAppointed: 0,
                    ammoOrdered: 0,
                    weaponOrdered: 0,
                    gadgetOrdered: 0,
                    fuelOrdered: 0,
                    fuelSpent: 0
                },
                detectiveStatistic: {
                    id: 0,
                    investigationCount: 0,
                    errorsInCards: 0
                },
                technicStatistic: {
                    id: 0,
                    visionsAccepted: 0,
                    visionsRejected: 0,
                    dopamineEntered: 0,
                    serotoninEntered: 0,
                    depressantEntered: 0
                }
            },
        };
    }

    handleEditClick = () => {
        this.setState({ isEditing: true });
    };

    handleInputChange = (e) => {
        const { name, value } = e.target;
        this.setState((prevState) => ({
            editedCrimeData: {
                ...prevState.editedCrimeData,
                [name]: value,
            },
        }));
    };

    handleStatisticChange = (selectedStatistic) => {
        this.setState({ selectedStatistic });
    };

    renderStatistics = () => {
        const { selectedStatistic, statistics } = this.state;
        const { userStat } = this.props;

        return (
            <div>
                <h2>{selectedStatistic}</h2>
                {userStat.bossReactGroupStatistic && selectedStatistic === "bossReactGroupStatistic" && Object.entries(userStat.bossReactGroupStatistic).map(([key, value]) => (
                    <p key={key}>{key}: {value}</p>
                ))}
                {userStat.detectiveStatistic && selectedStatistic === "detectiveStatistic" && Object.entries(userStat.detectiveStatistic).map(([key, value]) => (
                    <p key={key}>{key}: {value}</p>
                ))}
                {userStat.technicStatistic && selectedStatistic === "technicStatistic" && Object.entries(userStat.technicStatistic).map(([key, value]) => (
                    <p key={key}>{key}: {value}</p>
                ))}
            </div>
        );
    };


    render() {
        const { userStat, onClose, role } = this.props;
        const { isEditing, editedCrimeData, selectedStatistic, message } = this.state;

        return (
            <div className="modal">
                <div className="modal-content">
                    <span className="close" onClick={onClose}>
                        &times;
                    </span>

                    <div>
                        <h2>User Details and Statistics</h2>
                        {userStat && (<div><p>ID: {userStat.userInfo.id}</p>
                            <p>LOGIN: {userStat.userInfo.login}</p>
                    <p>EMAIL: {userStat.userInfo.email}</p>
                    <p>FULL NAME: {userStat.userInfo.firstName} {userStat.lastName}</p>
                            <p>TELEGRAM ID: {userStat.userInfo.telegramId}</p></div>)}

                    </div>


                    <div>
                        {/* Выпадающий список для выбора статистики */}
                        <label>Select Statistic:</label>
                        <select
                            value={selectedStatistic}
                            onChange={(e) => this.handleStatisticChange(e.target.value)}
                        >
                            <option value="bossReactGroupStatistic">Boss React Group Statistic</option>
                            <option value="detectiveStatistic">Detective Statistic</option>
                            <option value="technicStatistic">Technic Statistic</option>
                        </select>

                        {/* Отображение выбранной статистики */}
                        {this.renderStatistics()}
                    </div>

                </div>
            </div>
        );
    }
}

export default UserCard;
