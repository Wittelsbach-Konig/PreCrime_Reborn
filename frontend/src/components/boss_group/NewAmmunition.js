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

    handleKeyDown = (event) => {
        if (event.key === '+' || event.key === '-' || event.key === '.' || event.key === ',') {
            event.preventDefault();
        }
    }
    handleInputChange = (event) => {
        const { name, value } = event.target;
        if(name === 'amount' || name === 'maxPossibleAmount')
        {if (value>=0 && value <=10000)
          {this.setState({ [name]: value })}}
        else
          {this.setState({ [name]: value })};
    };

    handleSubmit = () => {
        const token = localStorage.getItem('jwtToken');
        if (this.state.resourceName && this.state.maxPossibleAmount!==0 && this.state.type!=='')
        {
            if (this.state.maxPossibleAmount>this.state.amount)
            {
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

        this.props.onClose();}
        else
            {
                window.alert('Max amount should be more then amount')
            }}
        else
        {
            window.alert('Input all fields')
        }
    };

    handleChangeType = (e) => {
        console.log(e.target.value)
        this.setState({ type: e.target.value });
    }

    render() {
        const { onClose } = this.props;
        const { resourceName, amount, maxPossibleAmount, type } = this.state;

        return (<div>
                <div className="modal">
                    <div className="modal-content-precog">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2 className="h-style">Supply Ammunition</h2>
                        <form className="form-tr">
                            <table className="bg-rg">
                                <tbody>
                                <tr>
                                    <td className="table-label-pr">Name Resource:</td>
                                    <td className="table-label-edit">
                                        <input
                                            maxLength="25"
                                            type="text"
                                            name="resourceName"
                                            value={resourceName}
                                            onChange={this.handleInputChange}
                                        />
                                    </td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Amount:</td>
                                    <td className="table-label-edit">
                                        <input
                                            type="number"
                                            name="amount"
                                            value={amount===''? this.setState({amount:0}):amount}
                                            onChange={this.handleInputChange}
                                            onKeyDown={this.handleKeyDown}
                                        />
                                    </td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Max Amount:</td>
                                    <td className="table-label-edit">
                                        <input
                                            type="number"
                                            name="maxPossibleAmount"
                                            value={maxPossibleAmount===''? this.setState({maxPossibleAmount:0}):maxPossibleAmount}
                                            onChange={this.handleInputChange}
                                            onKeyDown={this.handleKeyDown}
                                        />
                                    </td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Type Resource:</td>
                                    <td className="table-label-edit">
                                        <select value={this.state.crimeType} onChange={this.handleChangeType}>
                                            <option className="table-select" value="" name="crimeType">choose type</option>
                                            <option className="table-select" value="AMMUNITION" name="crimeType">AMMUNITION</option>
                                            <option className="table-select" value="WEAPON" name="crimeType">WEAPON</option>
                                            <option className="table-select" value="GADGET" name="crimeType">GADGET</option>
                                            <option className="table-select" value="FUEL" name="crimeType">FUEL</option>
                                        </select>
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

export default NewAmmunition;
