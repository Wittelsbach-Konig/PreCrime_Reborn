import React, { Component } from 'react';
import DelPrecogs from "../Technic/DelPrecogs";
import RegPrecogs from "../Technic/RegPrecogs";

class PrecogsList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            showModal_2: false,
            psychics: [],
            selectedPsychic: null,

        };
    }

    openModal = () => {
        this.setState({ showModal: true });
    };

    closeModal = () => {
        this.setState({showModal: false});
        this.fetchPsychics();
    };

    openModal_2 = () => {
        this.setState({ showModal_2: true });
    };

    closeModal_2 = () => {
        this.setState({showModal_2: false});
        this.fetchPsychics();
    };

    componentDidMount() {
        this.fetchPsychics();
    }


    fetchPsychics = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/precogs', {
            method: 'GET', // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
        })
            .then(responses => responses.json())
            .then(data => {
                this.setState({ psychics: data });
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
    };

    handlePsychicChange = (event) => {
        const selectedId = parseInt(event.target.value, 10);
        const selectedPsychic = this.state.psychics.find(psychic => psychic.id === selectedId);
        this.setState({ selectedPsychic });
    };

    render() {
        const { selectedPsychic } = this.state;
        return (<div>
                <div className="psychic-container">
                    <div className="psychic-dropdown-container">
                        <label htmlFor="psychicDropdown" className="ch-pr">Choose Precogs</label>
                        <select id="psychicDropdown" onChange={this.handlePsychicChange}>
                            <option value={null}>Choosing Precogs</option>
                            {this.state.psychics.map(psychic => (
                                <option key={psychic.id} value={psychic.id}>{psychic.preCogName}</option>
                            ))}
                        </select>
                        {this.state.selectedPsychic && (
                            <div className="psychic-info">
                                <h2>Information about precogs {this.state.selectedPsychic.preCogName}</h2>
                                <table>
                                    <tbody>
                                    <tr>
                                        <td className="table-label">ID:</td>
                                        <td>{this.state.selectedPsychic.id}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">Age:</td>
                                        <td>{this.state.selectedPsychic.age}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">Level dofamine:</td>
                                        <td>{this.state.selectedPsychic.dopamineLevel}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">Level serotonin:</td>
                                        <td>{this.state.selectedPsychic.serotoninLevel}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">Stress level:</td>
                                        <td>{this.state.selectedPsychic.stressLevel}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">Date of commision:</td>
                                        <td>{this.state.selectedPsychic.commissionedOn}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">On Working:</td>
                                        <td>{this.state.selectedPsychic.work ? 'Yes' : 'No'}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>
                </div>
                <button className="new-precog" onClick={this.openModal}>New Precog</button>
                {this.state.showModal && <RegPrecogs onClose={this.closeModal} onRenew={this.fetchPsychics} />}
                <button className="del-precog" onClick={this.openModal_2}>Delete Precog</button>
                {this.state.showModal_2 && <DelPrecogs onClose={this.closeModal_2} onRenew={this.fetchPsychics} />}

            </div>
        );
    }
}

export default PrecogsList;
