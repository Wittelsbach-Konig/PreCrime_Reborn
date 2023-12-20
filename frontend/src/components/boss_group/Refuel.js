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
        if(value>=0 && value<=10000)
        {this.setState({ [name]: value })};
    };

    handleKeyDown = (event) => {
        if (event.key === '+' || event.key === '-' || event.key === '.' || event.key === ',') {
            event.preventDefault();
        }
    }

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
                    throw new Error(response.status);
                }
                this.props.onRenew();
            })
            .catch(error => {
                if (error.message ==="400")
                    window.alert("Purchases shouldn't be more then max possible amount");
                if (error.message ==="404")
                    window.alert("You don't choose position");
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
                        throw new Error(response.status);
                    }
                    this.props.onRenew();
                })
                .catch(error => {
                    if (error.message ==="400")
                        window.alert("Purchases shouldn't be more then max possible amount");
                    if (error.message ==="404")
                        window.alert("You don't choose position");
                });
            this.props.onRenew();
        }

        this.props.onClose();
    };

    render() {
        const { onClose } = this.props;
        const { amount } = this.state;

        return (<div>
                <div className="modal">
                    <div className="modal-content-precog">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2 className="h-style">Purchase</h2>
                        <form className="form-tr">
                            <table className="bg-rg">
                                <tbody>
                                <tr>
                                    <td className="table-label-pr">Amount:</td>
                                    <td className="table-label-edit">
                                        <input
                                            type="number"
                                            name="amount"
                                            value={amount === '' ? this.setState({amount:0}):amount}
                                            onChange={this.handleInputChange}
                                            onKeyDown={this.handleKeyDown}
                                        />
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <button className="button-tr" type="button" onClick={this.handleSubmit}>Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

export default Refuel;
