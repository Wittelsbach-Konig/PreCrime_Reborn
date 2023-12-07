import React, { Component } from 'react';

class RegTransport extends Component {
    constructor(props) {
        super(props);
        this.state = {
            brand: '',
            model: '',
            maximum_fuel: 0,
        };
    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    };

    handleSubmit = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/reactiongroup/transport/new', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
            body: JSON.stringify({
                id: 0,
                brand: this.state.brand,
                model: this.state.model,
                maximum_fuel: this.state.maximum_fuel
            }),
        })
            .then(response => response.json())
            .then(data => {
                this.setState({transport: data});
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
        const { brand, model, maximum_fuel } = this.state;

        return (<div>
                <div className="modal">
            <div className="modal-content">
                    <span className="close" onClick={onClose}>&times;</span>
                    <h2>Add New Transport</h2>
                    <form className="form-tr">
                        <label>
                            Brand:
                            <input type="text" name="brand" value={brand} onChange={this.handleInputChange} />
                        </label>
                        <br />
                        <label>
                            Model:
                            <input type="text" name="model" value={model} onChange={this.handleInputChange} />
                        </label>
                        <br />
                        <label>
                            Maximum Fuel:
                            <input type="number" name="maximum_fuel" value={maximum_fuel} onChange={this.handleInputChange} />
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

export default RegTransport;
