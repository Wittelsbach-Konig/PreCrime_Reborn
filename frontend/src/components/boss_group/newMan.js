import React, { Component } from 'react';

class RegMans extends Component {
    constructor(props) {
        super(props);
        this.state = {
            memberName: '',
            telegramId: 0,
        };
    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    };

    handleSubmit = () => {

        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/reactiongroup/newman', {
            method: 'POST', // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
            body: JSON.stringify({
                id: 0,
                memberName: this.state.memberName,
                telegramId: this.state.telegramId,
            }),
        })
            .then(response => response.json())
            .then(data => {
                this.setState({man: data});
                console.log(data);
                this.props.onRenew()
                this.props.shWrk()
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });

        this.props.onClose();
    };

    render() {
        const { onClose } = this.props;
        const { memberName, telegramId } = this.state;

        return (<div>
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2>Add New Man</h2>
                        <form className="form-tr">
                            <label>
                                Member Name:
                                <input type="text" name="memberName" value={memberName} onChange={this.handleInputChange} />
                            </label>
                            <br />
                            <label>
                                telegram ID:
                                <input type="text" name="telegramId" value={telegramId} onChange={this.handleInputChange} />
                            </label>
                            <br />
                            <button className="button-tr" type="button" onClick={this.handleSubmit}>Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

export default RegMans;
