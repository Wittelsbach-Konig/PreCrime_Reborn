import React, { Component } from 'react';

class AddVision extends Component {
    constructor(props) {
        super(props);
        this.state = {
            url: '',
            id: 0,
            vision: null,
        };
    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    };

    handleSubmit = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/visions/add', {
            method: 'POST', // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
            body: JSON.stringify({
                id: 0,
                videoUrl: this.state.url,
            }),
        })
            .then(response => response.json())
            .then(data => {
                this.setState({vision: data});
                console.log(data);
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });

        this.props.onClose();
    };

    render() {
        const { onClose } = this.props;
        const { url } = this.state;

        return (<div>
                <div className="modal">
                    <div className="modal-content-user">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2 className="h-style">Add Vision</h2>
                        <form className="form-tr">
                            <table>
                                <tbody>
                                    <tr>
                                        <td className="table-label">
                                            Url:
                                        </td>
                                        <td className="table-label-edit">
                                            <input
                                                type="text"
                                                name="url"
                                                value={url}
                                                onChange={this.handleInputChange}
                                            />
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

export default AddVision;
