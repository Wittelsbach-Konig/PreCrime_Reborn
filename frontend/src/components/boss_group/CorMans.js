import React, { Component } from 'react';

class CorMans extends Component {
    constructor(props) {
        super(props);
        this.state = {
            idd: 0,
            memberName: this.props.manData.memberName,
            telegramId: this.props.manData.telegramId,

        };
    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    };


    handleUpdateManData = () => {
        const { memberName, telegramId } = this.state;

        const telId = parseInt(telegramId, 10);
        const token = localStorage.getItem('jwtToken');
        const url = `api/v1/reactiongroup/${this.props.manData.id}`;

        const requestData = {
            memberName: memberName,
            telegramId: telId,
        };
        if(memberName !== '' && telId !== 0 )
        {
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
        }
        else
        {window.alert("member name shouldn't be empty")}
    };

    render() {
        const { onClose } = this.props;
        const { memberName, telegramId } = this.state;

        return (
            <div>
                <div className="modal">
                    <div className="modal-content-precog">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2 className="h-style">Update Men Data</h2>
                        <form className="form-tr">
                            <table className="bg-rg">
                                <tbody>
                                <tr>
                                    <td className="table-label-pr">Member Name:</td>
                                    <td className="table-label-edit">
                                        <input
                                            type="text"
                                            name="memberName"
                                            value={memberName}
                                            onChange={this.handleInputChange}
                                        />
                                    </td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Telegram ID:</td>
                                    <td className="table-label-edit">
                                        <input
                                            type="text"
                                            name="telegramId"
                                            value={telegramId}
                                            onChange={this.handleInputChange}
                                        />
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <br />
                            <button
                                className="button-tr"
                                type="button"
                                onClick={this.handleUpdateManData}>
                                Update
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

export default CorMans;
