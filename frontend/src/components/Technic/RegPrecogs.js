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
        this.setState({ [name]: value });
    };

    handleSubmit = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/precogs/newprecog', {
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
            });

        this.props.onClose();
    };

    render() {
        const { onClose } = this.props;
        const { preCogName, age } = this.state;

        return (
            <div>
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2>Add New Man</h2>
                        <form className="form-tr">
                            <label>
                                PreCog Name:
                                <input type="text" name="preCogName" value={preCogName} onChange={this.handleInputChange} />
                            </label>
                            <br />
                            <label>
                                Age:
                                <input type="number" name="age" value={age} onChange={this.handleInputChange} />
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

export default RegPrecogs;
