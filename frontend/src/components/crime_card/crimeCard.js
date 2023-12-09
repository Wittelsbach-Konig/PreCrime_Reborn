// CrimeCard.js

import React, { Component } from 'react';
import '../../css/CrimeCard.css';

class CrimeCard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isEditing: false,
            message:"",
            editedCrimeData: {
                victimName: this.props.crimeCard.victimName,
                criminalName: this.props.crimeCard.criminalName,
                placeOfCrime: this.props.crimeCard.placeOfCrime,
                weapon: this.props.crimeCard.weapon,
                crimeTime: this.props.crimeCard.crimeTime,
                crimeType: this.props.crimeCard.crimeType,
                visionId: this.props.crimeCard.visionId,
            },
        };
    }

    handleEditClick = () => {
        this.setState({ isEditing: true });
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

                            <h2>Message</h2>
                            <label>
                                Text message:
                                <input
                                    className="message"
                                    type="text"
                                    name="message"
                                    value={message}
                                    onChange={this.handleInputChange}
                                />
                            </label>
                            <br/>
                            <button onClick={this.handleSaveClick}>Send message</button>
                                </div>)}
                    {isEditing && (role==="DETECTIVE") && ( <div>

                            <h2>Edit Crime Card</h2>
                            <label>
                                Victim Name:
                                <input
                                    type="text"
                                    name="victimName"
                                    value={editedCrimeData.victimName}
                                    onChange={this.handleInputChange}
                                />
                            </label>
                            <br/>
                            <label>
                                Criminal Name:
                                <input
                                    type="text"
                                    name="criminalName"
                                    value={editedCrimeData.criminalName}
                                    onChange={this.handleInputChange}
                                />
                            </label>
                            <br/>
                            <label>
                                Place of Crime:
                                <input
                                    type="text"
                                    name="placeOfCrime"
                                    value={editedCrimeData.placeOfCrime}
                                    onChange={this.handleInputChange}
                                />
                            </label>
                            <br/>
                            <label>
                                Weapon:
                                <input
                                    type="text"
                                    name="weapon"
                                    value={editedCrimeData.weapon}
                                    onChange={this.handleInputChange}
                                />
                            </label>
                            <br/>
                            <label>
                                Type Crime:
                                <input
                                    type="text"
                                    name="crimeType"
                                    value={editedCrimeData.crimeType}
                                    onChange={this.handleInputChange}
                                />
                            </label>
                            <br/>
                            <label>
                                Vision Id:
                                <input
                                    type="text"
                                    name="visionId"
                                    value={editedCrimeData.visionId}
                                    onChange={this.handleInputChange}
                                />
                            </label>
                            <br/>
                            <button onClick={this.handleSaveClick}>Save</button>
                        </div>)

                    }
                    {!isEditing &&
                        <div>
                            {/* Информация о преступлении */}
                            <h2>Crime Details</h2>
                            <p>ID: {crimeCard.id}</p>
                            <p>Victim Name: {crimeCard.victimName}</p>
                            <p>Criminal Name: {crimeCard.criminalName}</p>
                            <p>Place of Crime: {crimeCard.placeOfCrime}</p>
                            <p>Weapon: {crimeCard.weapon}</p>
                            <p>Crime Time: {crimeCard.crimeTime}</p>
                            <p>Responsible Detective: {crimeCard.responsibleDetective}</p>
                            <p>Is Criminal Caught: {crimeCard.isCriminalCaught ? 'Yes':'No'}</p>
                            <p>Crime Type: {crimeCard.crimeType}</p>
                            <p>Vision ID: {crimeCard.visionId}</p>
                            <button onClick={this.handleEditClick}>Edit</button>
                        </div>
                    }
                </div>
            </div>
        );
    }
}

export default CrimeCard;
