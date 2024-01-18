import React, { Component } from 'react';
import '../../css/CrimeCard.css';

class UserCard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isEditing: false,
            message: "",
            selectedStatistic: "Select Statistic", // По умолчанию выбраны данные bossReactGroupStatistic
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
        console.log(this.props.userStat)
    };


    splitStringByUpperCase(str) {
        return str.split(/(?=[A-Z])/).join(' ');
    }

    render() {
        const { userStat, onClose, roles } = this.props;
        const { selectedStatistic } = this.state;

        return (
            <div className="modal">
                <div className="modal-content-user">
                    <span className="close" onClick={onClose}>
                        &times;
                    </span>
                        <h2 className="h-style">User Details and Statistics</h2>

                        {userStat && (<div>
                            <table className="bg-rg">
                                <tbody>
                                <tr>
                                    <td className="table-label-pr">Id:</td>
                                    <td className="table-label-pr">{userStat.userInfo.id}</td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Login:</td>
                                    <td className="table-label-pr">{userStat.userInfo.login}</td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Email:</td>
                                    <td className="table-label-pr">{userStat.userInfo.email}</td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Full Name:</td>
                                    <td className="table-label-pr">{userStat.userInfo.firstName} {userStat.userInfo.lastName}</td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Telegram Id:</td>
                                    <td className="table-label-pr">{userStat.userInfo.telegramId}</td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Select Statistic:</td>
                                    <td className="table-label-edit"><select
                                        value={selectedStatistic}
                                        onChange={(e) => this.handleStatisticChange(e.target.value)}
                                    >
                                        <option> Select Statistic </option>
                                        {roles.includes('REACTIONGROUP') &&
                                        <option className="table-select" value="bossReactGroupStatistic">Boss React Group Statistic</option>
                                        }
                                        {roles.includes('DETECTIVE') &&
                                        <option className="table-select" value="detectiveStatistic">Detective Statistic</option>
                                        }
                                        {roles.includes('TECHNIC') &&
                                        <option className="table-select" value="technicStatistic">Technic Statistic</option>
                                        }
                                        </select></td>
                                </tr>

                                    <td colSpan="2" className='table-label-edit-td'>
                                <h2 className="h-style">{this.splitStringByUpperCase(selectedStatistic)}</h2>
                                    </td>
                                    {userStat.bossReactGroupStatistic && selectedStatistic === "bossReactGroupStatistic" && Object.entries(userStat.bossReactGroupStatistic).map(([key, value]) => (
                                        (key !== 'id') && (<tr>
                                        <td className="table-label-pr" key={key}>{this.splitStringByUpperCase(key)}</td>
                                        <td className="table-label-pr">{value}</td>
                                        </tr>)

                                    ))}
                                    {userStat.detectiveStatistic && selectedStatistic === "detectiveStatistic" && Object.entries(userStat.detectiveStatistic).map(([key, value]) => (
                                        (key !== 'id') &&  (<tr>
                                        <td className="table-label-pr" key={key}>{this.splitStringByUpperCase(key)}</td>
                                        <td className="table-label-pr">{value}</td>
                                        </tr>)
                                    ))}
                                    {userStat.technicStatistic && selectedStatistic === "technicStatistic" && Object.entries(userStat.technicStatistic).map(([key, value]) => (

                                        (key !== 'id') && (<tr>
                                            <td className="table-label-pr" key={key}>{this.splitStringByUpperCase(key)}</td>
                                            <td className="table-label-pr">{value}</td>
                                        </tr>)

                                    ))}

                                </tbody>
                            </table>

                        </div>)}



                </div>
            </div>
        );
    }
}

export default UserCard;
