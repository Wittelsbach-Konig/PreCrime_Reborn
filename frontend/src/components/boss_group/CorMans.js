import React, { Component } from 'react';
import FormData from "form-data";

class CorMans extends Component {
    constructor(props) {
        super(props);
        this.state = {
            idd: 0,
            manData: null, // Данные о человеке
            memberName: '',
            telegramId: 0,

        };
    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    };

    handleGetManData = () => {
        const token = localStorage.getItem('jwtToken');
        const { idd } = this.state;
        const url = `http://localhost:8028/api/v1/reactiongroup/${idd}`;

        fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
        })
            .then(responses => responses.json())
            .then(data => {
                this.setState({manData:data})
                console.log(this.state.manData)
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
    };

    handleUpdateManData = () => {
        const { idd, memberName, telegramId } = this.state;

        const intValue = parseInt(idd, 10);
        const telId = parseInt(telegramId, 10);
        const token = localStorage.getItem('jwtToken');
        const url = `http://localhost:8028/api/v1/reactiongroup/${intValue}`;

        const requestData = {
            memberName: memberName,
            telegramId: telId,
        };

        fetch(url, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestData),
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
    };

    render() {
        const { onClose } = this.props;
        const { idd, manData, memberName, telegramId } = this.state;

        return (
            <div>
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2>Manage Man</h2>
                        <form className="form-tr">
                            <label>
                                Enter id for man management:
                                <input type="text" name="idd" value={idd} onChange={this.handleInputChange} />
                            </label>
                            <br />
                            <button className="button-tr" type="button" onClick={this.handleGetManData}>
                                Get Man Data
                            </button>

                            {manData && (
                                <div>
                                    <h3>Man Details</h3>
                                    <p>ID: {manData.id}</p>
                                    <p>Member Name: {manData.memberName}</p>
                                    <p>Telegram ID: {manData.telegramId}</p>
                                </div>
                            )}

                            <h3>Update Man Data</h3>
                            <label>
                                Member Name:
                                <input
                                    type="text"
                                    name="memberName"
                                    value={memberName}
                                    onChange={this.handleInputChange}
                                />
                            </label>
                            <br />
                            <label>
                                Telegram ID:
                                <input
                                    type="text"
                                    name="telegramId"
                                    value={telegramId}
                                    onChange={this.handleInputChange}
                                />
                            </label>
                            <br />
                            <button className="button-tr" type="button" onClick={this.handleUpdateManData}>
                                Update Man Data
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

export default CorMans;
