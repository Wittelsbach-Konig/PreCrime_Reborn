// CrimeCard.js

import React, { Component } from 'react';
import '../../css/CrimeCard.css';

class CrimeCard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isEditing: false,
            message:"",
            crimeType: this.props.crimeCard.crimeType,
            editedCrimeData: {
                victimName: this.props.crimeCard.victimName,
                criminalName: this.props.crimeCard.criminalName,
                placeOfCrime: this.props.crimeCard.placeOfCrime,
                weapon: this.props.crimeCard.weapon,
                crimeTime: this.props.crimeCard.crimeTime,
                crimeType: this.props.crimeCard.crimeType,
                visionUrl: this.props.crimeCard.visionUrl,
            },
        };
    }

    handleEditClick = () => {
        this.setState({ isEditing: true });
    };

    handleChangeStatus = (event) => {
        const newStatus = event.target.value;
        const confirmation = window.confirm(`Вы уверены, что хотите изменить статус на ${newStatus}?`);

        if (confirmation) {
            this.state.editedCrimeData.crimeType = newStatus
            this.setState({crimeType:newStatus}, ()=>{console.log(this.state.editedCrimeData.crimeType)})
        } else {
            this.setState({ newStatus: '' });
        }
    };

    handleInputChange = (e) => {
        const { role } = this.props
        if (role==="DETECTIVE")
        {
        const { name, value } = e.target;
        this.setState((prevState) => ({
            editedCrimeData: {
                ...prevState.editedCrimeData,
                [name]: value,
            },
        }))}
        else
        {   const { name, value } = e.target;
            this.setState({message:value})}
    };

    handleSaveClick = () => {
        const token = localStorage.getItem('jwtToken');
        const { role } = this.props
        if (role==="DETECTIVE") {
            const url = `http://localhost:8028/api/v1/cards/${this.props.crimeCard.id}`;

            fetch(url, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(this.state.editedCrimeData),
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Ошибка сети или сервера');
                    }
                    console.log('Данные успешно обновлены');
                    this.props.onRenew();
                })
                .catch(error => {
                    console.error('Ошибка при запросе к серверу:', error);
                });

            this.props.onClose();
            this.setState({isEditing: false});
        }
        else
        {
            const FormData = require('form-data');
            const form = new FormData();
            form.append('message', this.state.message);
            console.log(this.state.message)
            fetch(`http://localhost:8028/api/v1/auditor/cards/${this.props.crimeCard.id}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                body: form,
            })
                .then(response => {
                    if (!response.ok) {
                        // Если статус ответа не 2xx (успех), бросаем ошибку
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }

                    // Возвращаем response.text(), так как мы не ожидаем JSON
                    return response.text();
                })
                .then(data => {
                    console.log(data);
                    this.props.onRenew();
                })
                .catch(error => {
                    console.error('Ошибка при запросе к серверу:', error);
                });
            this.props.onClose();
            this.setState({isEditing: false});
        }

    };

    render() {
        const { crimeCard, onClose, role } = this.props;
        const { isEditing, editedCrimeData, message } = this.state;

        return (
            <div className="crime-card">
                <div className="crime-card-content">
                    <span className="close" onClick={onClose}>
                        &times;
                    </span>
                    {isEditing && (role==="AUDITOR") &&
                        ( <div>

                            <h2 className="h-style">Message</h2>
                            <table className="bg-rg">
                                <tbody>
                                <tr>

                                    <td className="table-label-edit">Text message:</td>

                                <td className="table-label-edit">

                                    <textarea
                                    maxLength="2000"
                                    className="message"
                                    type="text"
                                    name="message"
                                    value={message}
                                    onChange={this.handleInputChange}
                                />
                                </td>

                                </tr>
                                </tbody>
                            </table>
                            <button className="button-edit" onClick={this.handleSaveClick}>Send message</button>
                                </div>)}
                    {isEditing && (role==="DETECTIVE") && ( <div>

                            <h2 className="h-style">Edit Card #{crimeCard.id}</h2>
                        <table className="bg-rg">
                            <tbody>
                            <tr>

                                <td className="table-label-pr">Victim Name:</td>
                                <td className="table-label-edit"><input
                                    maxLength="20"
                                    type="text"
                                    name="victimName"
                                    value={editedCrimeData.victimName}
                                    onChange={this.handleInputChange}
                                /></td>
                            </tr>
                            <tr>

                                <td className="table-label-pr">Criminal Name:</td>
                                <td className="table-label-edit"><input
                                    maxLength="20"
                                    type="text"
                                    name="criminalName"
                                    value={editedCrimeData.criminalName}
                                    onChange={this.handleInputChange}
                                /></td>
                            </tr>
                            <tr>

                                <td className="table-label-pr">Place of Crime:</td>
                                <td className="table-label-edit"><input
                                    maxLength="40"
                                    type="text"
                                    name="placeOfCrime"
                                    value={editedCrimeData.placeOfCrime}
                                    onChange={this.handleInputChange}
                                /></td>
                            </tr>
                            <tr>

                                <td className="table-label-pr">Weapon:</td>
                                <td className="table-label-edit"><input
                                    maxLength="20"
                                    type="text"
                                    name="weapon"
                                    value={editedCrimeData.weapon}
                                    onChange={this.handleInputChange}
                                /></td>
                            </tr>
                            <tr>

                                <td className="table-label-pr">Type Crime:</td>
                                <td className="table-label-edit">
                                    <select value={this.state.crimeType} onChange={this.handleChangeStatus}>
                                        <option className="table-select" value="INTENTIONAL" name="crimeType">INTENTIONAL</option>
                                        <option className="table-select" value="UNINTENTIONAL" name="crimeType">UNINTENTIONAL</option>
                                    </select>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                            <button className="button-edit" onClick={this.handleSaveClick}>Save</button>
                        </div>)

                    }
                    {!isEditing &&
                        <div>
                            <h2 className="h-style">Card #{crimeCard.id}</h2>
                            <table className="bg-rg">
                                <tbody>
                                <tr>

                            <td className="table-label-pr">Victim Name:</td>
                            <td className="table-label-pr">{crimeCard.victimName}</td>
                                    </tr>
                                <tr>
                            <td className="table-label-pr">Criminal Name: </td>
                            <td className="table-label-pr">{crimeCard.criminalName}</td>
                                </tr>
                                <tr>
                            <td className="table-label-pr">Place of Crime:</td>
                            <td className="table-label-pr">{crimeCard.placeOfCrime}</td>
                                </tr>
                                    <tr>
                                        <td className="table-label-pr">Weapon:</td>
                                        <td className="table-label-pr">{crimeCard.weapon}</td>
                                    </tr>
                                <tr>
                                    <td className="table-label-pr">Crime Time:</td>
                                    <td className="table-label-pr">{crimeCard.crimeTime}</td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Responsible Detective:</td>
                                    <td className="table-label-pr">{crimeCard.responsibleDetective}</td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Is Criminal Caught:</td>
                                    <td className="table-label-pr">{crimeCard.isCriminalCaught ? 'Yes':'No'}</td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Crime Type:</td>
                                    <td className="table-label-pr">{crimeCard.crimeType}</td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Vision URL:</td>
                                    <td className="table-label-pr">{crimeCard.visionUrl}</td>
                                </tr>
                            </tbody>
                        </table>
                            <button className="button-edit" onClick={this.handleEditClick}>Edit</button>

                        </div>
                    }
                </div>
            </div>
        );
    }
}

export default CrimeCard;
