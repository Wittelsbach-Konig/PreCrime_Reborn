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

        if(name==='maximum_fuel')
        {value>=0 && value<=1000 && this.setState({ [name]: value })}
        else
        {this.setState({ [name]: value })}
    };

    handleKeyDown = (event) => {
        if (event.key === '+' || event.key === '-' || event.key === '.' || event.key === ',') {
            event.preventDefault();
        }
    }

    handleSubmit = () => {
        const token = localStorage.getItem('jwtToken');
        console.log(typeof this.state.maximum_fuel)
        if(this.state.brand!=='' && this.state.model!=='' && this.state.maximum_fuel!==0)
        {
        fetch('api/v1/reactiongroup/transport/new', {
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

        this.props.onClose();}
        else
        {window.alert('input all fields')}
    };



    render() {
        const { onClose } = this.props;
        const { brand, model, maximum_fuel } = this.state;

        return (<div>
                <div className="modal">
            <div className="modal-content-precog">
                    <span className="close" onClick={onClose}>&times;</span>
                    <h2 className="h-style">Add New Transport</h2>
                    <form className="form-tr">
                        <table className="bg-rg">
                            <tbody>
                            <tr>
                                <td className="table-label-pr">Brand:</td>
                                <td className="table-label-edit">
                                    <input maxLength="20"
                                           type="text"
                                           name="brand"
                                           value={brand}
                                           onChange={this.handleInputChange} />
                                </td>
                            </tr>
                            <tr>
                                <td className="table-label-pr">Model:</td>
                                <td className="table-label-edit">
                                    <input maxLength="20"
                                           type="text"
                                           name="model"
                                           value={model}
                                           onChange={this.handleInputChange} />
                                </td>
                            </tr>
                            <tr>
                                <td className="table-label-pr">Maximum Fuel:</td>
                                <td className="table-label-edit">
                                    <input type="number"
                                           name="maximum_fuel"
                                           value={maximum_fuel==='' ? this.setState({maximum_fuel:0}):maximum_fuel}
                                           onChange={this.handleInputChange}
                                           onKeyDown={this.handleKeyDown}/>
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

export default RegTransport;
