import React, { Component } from 'react';

class UpdatePrecogs extends Component {
    constructor(props) {
        super(props);
        this.state = {
            idd: 0,
            preCogName:this.props.pre.preCogName,
            age:this.props.pre.age
        };
    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        (name === "age") && (value>=0) && value<1000 && this.setState({ [name]: value });
        (name === "preCogName") && this.setState({ [name]: value });
    };

    handleSubmit = () => {
        const token = localStorage.getItem('jwtToken');
        const url = `http://localhost:8028/api/v1/precogs/${this.props.pre.id}`;
        if(this.state.preCogName !== '' && this.state.age !== 0 )
        {fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body:JSON.stringify({
                preCogName:this.state.preCogName,
                age: this.state.age
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Ошибка сети или сервера');
                }
                console.log('Данные успешно обновлены');
                this.props.onClose();
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            })}
        else
        {
            window.alert("don't input all fields")
        }

    };

    render() {
        const { onClose, pre } = this.props;
        const { idd, preCogName, age } = this.state;

        return (<div>
                <div className="modal">
                    <div className="modal-content-precog">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2 className="h-style">Update Precog</h2>
                        <form className="form-tr">
                            <table className="bg-rg">
                                <tbody>
                                <tr>
                                    <td className="table-label-pr">PreCog Name:</td>
                                    <td className="table-label-pr">
                                        <input maxLength="20" type="text" name="preCogName" value={preCogName} onChange={this.handleInputChange} />
                                    </td>
                                </tr>
                                <tr>
                                    <td className="table-label-pr">Age:</td>
                                    <td className="table-label-pr">
                                        <input type="number" name="age" value={age} onChange={this.handleInputChange} />
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <br/>
                            <button className="button-tr" type="button" onClick={this.handleSubmit}>Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

export default UpdatePrecogs;
