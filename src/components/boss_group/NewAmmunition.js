import React, { Component } from 'react';

class NewAmmunition extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: 0,
            resourceName: "",
            amount: 0,
            maxPossibleAmount: 0,
            type: "",
            supply:[]
        };
    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    };

    handleSubmit = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/reactiongroup/supply/new', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
            body: JSON.stringify({
                id: 0,
                resourceName: this.state.resourceName,
                amount: this.state.amount,
                maxPossibleAmount: this.state.maxPossibleAmount,
                type: this.state.type
            }),
        })
            .then(response => response.json())
            .then(data => {
                this.setState({supply: data});
                console.log(data);
                this.props.onRenew();
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });

        this.props.onClose();
    };

    render() {
        const { onClose } = this.props;
        const { resourceName, amount, maxPossibleAmount, type } = this.state;

        return (<div>
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2>Supply Ammunition</h2>
                        <form className="form-tr">
                            <label>
                                Name Resource:
                                <input type="text" name="resourceName" value={resourceName} onChange={this.handleInputChange} />
                            </label>
                            <br />
                            <label>
                                Amount:
                                <input type="number" name="amount" value={amount} onChange={this.handleInputChange} />
                            </label>
                            <br />
                            <label>
                                Max Amount:
                                <input type="number" name="maxPossibleAmount" value={maxPossibleAmount} onChange={this.handleInputChange} />
                            </label>
                            <br />
                            <label>
                                Type Resource:
                                <input type="text" name="type" value={type} onChange={this.handleInputChange} />
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

export default NewAmmunition;
