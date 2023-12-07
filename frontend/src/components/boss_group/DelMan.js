import React, { Component } from 'react';

class DelMan extends Component {
    constructor(props) {
        super(props);
        this.state = {
            idd: 0,
        };
    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    };

    handleSubmit = () => {
        const token = localStorage.getItem('jwtToken');

        const putData = {
            id: this.state.idd,
        };

        fetch(`http://localhost:8028/api/v1/reactiongroup/${this.state.idd}/retire`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(putData),
        })
            .then(response => response.json())
            .then(updatedData => {
                console.log('Данные успешно обновлены:', updatedData);
                this.props.onRenew()
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });


        this.props.onClose();
    };

    render() {
        const { onClose } = this.props;
        const { idd } = this.state;

        return (<div>
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2>Delete Man</h2>
                        <form className="form-tr">
                            <label>
                                Enter id for man delete:
                                <input type="text" name="idd" value={idd} onChange={this.handleInputChange} />
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

export default DelMan;
