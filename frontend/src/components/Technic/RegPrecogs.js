import React, { Component } from 'react';

class RegPrecogs extends Component {
    constructor(props) {
        super(props);
        this.state = {
            preCogName: '',
            age: 0,
        };
    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        (name === "age") && (value>=0) && value<1000 && this.setState({ [name]: value });
        (name === "preCogName") && this.setState({ [name]: value });
    };

    handleSubmit = () => {
        const token = localStorage.getItem('jwtToken');
        if(this.state.preCogName !== '' && this.state.age !== 0 )
        {fetch('api/v1/precogs/newprecog', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify({
                id: 0,
                preCogName: this.state.preCogName,
                age: this.state.age,
            }),
        })
            .then(response => response.json())
            .then(data => {
                this.setState({ man: data });
                console.log(data);
                this.props.onRenew();
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            })
            this.props.onClose();}
        else
        {window.alert("don't input all fields!")}


    };

    handleKeyDown = (event) => {
        if (event.key === '+' || event.key === '-' || event.key === '.' || event.key === ',') {
            event.preventDefault();
        }
    }

    render() {
        const { onClose } = this.props;
        const { preCogName, age } = this.state;

        return (
            <div>
                <div className="modal">
                    <div className="modal-content-precog">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2 className="h-style">Add New Precog</h2>
                        <form className="form-tr">
                            <table className="bg-rg">
                                <tbody>
                                <tr>
                                    <td className="table-label-pr">PreCog Name:</td>
                                    <td className="table-label-edit">
                                        <input
                                            maxLength="20"
                                            type="text"
                                            name="preCogName"
                                            value={preCogName}
                                            onChange={this.handleInputChange}
                                        />
                                    </td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Age:</td>
                                    <td className="table-label-edit">
                                        <input
                                            type="number"
                                            name="age"
                                            value={age===''? this.setState({age:0}):age}
                                            onChange={this.handleInputChange}
                                            onKeyDown={this.handleKeyDown}
                                        />
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <br/>
                            <button className="button-tr"
                                    type="button"
                                    onClick={this.handleSubmit}>
                                Submit
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

export default RegPrecogs;
