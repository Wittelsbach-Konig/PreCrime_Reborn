import React, { Component } from 'react';

class Refuel extends Component {
    constructor(props) {
        super(props);
        this.state = {
            amount: 0,
            idTr:0,
        };

    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    };

    handleSubmit = () => {
        const token = localStorage.getItem('jwtToken');
        const FormData = require('form-data');
        const form = new FormData();
        form.append('amount', this.state.amount);
        if (this.props.reff)
        {fetch(`http://localhost:8028/api/v1/reactiongroup/transport/${this.props.idTr}/refuel`, {
            method: 'PUT',
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
        }

        if (this.props.amm)
        {
            fetch(`http://localhost:8028/api/v1/reactiongroup/supply/${this.props.idTr}`, {
                method: 'PUT',
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
        }

        this.props.onClose();
    };

    render() {
        const { onClose } = this.props;
        const { amount } = this.state;

        return (<div>
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2>Refuel Transport</h2>
                        <form className="form-tr">
                            <label>
                                Amount:
                                <input type="number" name="amount" value={amount} onChange={this.handleInputChange} />
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

export default Refuel;
