import React, { Component } from 'react';
import UpdatePrecogs from "./UpdatePrecogs";
import RegPrecogs from "../Technic/RegPrecogs";
import FormData from "form-data";

class PrecogsList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            showModal_2: false,
            psychics: null,
            selectedPsychic: null,
            serotonin: 0,
            dopamine: 0,
            depressant: 0,
            url:''

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
        const token = localStorage.getItem('jwtToken');
        this.setState({showModal_2: false});
        this.fetchRenew(`http://localhost:8028/api/v1/precogs/${this.state.selectedPsychic.id}`,token, 'GET');
    };

    componentDidMount() {
        this.fetchPsychics();
    }


    fetchPsychics = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/precogs', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
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

    handleNumberChange = (fieldName, event) => {
        this.setState({ [fieldName]: parseInt(event.target.value, 10) });
    };

    handleButtonClick = (fieldName) => {
        const {url} = this.state
        const token = localStorage.getItem('jwtToken');
        const FormData = require('form-data');
        const form = new FormData();
        form.append(`amount`, this.state[fieldName]);
            if(fieldName==="serotonin")
            {this.fetchChange(token,`http://localhost:8028/api/v1/precogs/${this.state.selectedPsychic.id}/enterserotonin`,form)}
            if(fieldName==="dopamine")
            { this.fetchChange(token,`http://localhost:8028/api/v1/precogs/${this.state.selectedPsychic.id}/enterdopamine`,form)}
            if(fieldName==="depressant")
            { this.fetchChange(token,`http://localhost:8028/api/v1/precogs/${this.state.selectedPsychic.id}/enterdepressant`,form)}





    };

    fetchChange = (token, url, form) =>
    {
        console.log(url)
        fetch(url, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
            body: form,
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }

                // Возвращаем response.text(), так как мы не ожидаем JSON
                return response.text();
            })
            .then(data => {
                console.log(data);
                this.fetchRenew(`http://localhost:8028/api/v1/precogs/${this.state.selectedPsychic.id}`,token, 'GET')
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });

    }

    fetchRetire = () =>
    {
        const token = localStorage.getItem('jwtToken');
        this.fetchRenew(`http://localhost:8028/api/v1/precogs/${this.state.selectedPsychic.id}/retire`,token, 'POST')

    }

    fetchRehabilitate = () =>
    {
        const token = localStorage.getItem('jwtToken');
        this.fetchRenew(`http://localhost:8028/api/v1/precogs/${this.state.selectedPsychic.id}/rehabilitate`,token, 'POST')

    }

    fetchDel = () =>
    {
        const token = localStorage.getItem('jwtToken');
        this.fetchRenew(`http://localhost:8028/api/v1/precogs/${this.state.selectedPsychic.id}`,token, 'DELETE')

    }

    fetchRenew = (url, token, method) =>
    {
        console.log(url)
        fetch(url, {
            method: `${method}`,
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }

                // Возвращаем response.text(), так как мы не ожидаем JSON
                if (method==="DELETE"){
                    return response.text();
                }
                else {
                    return response.json();
                }
            })
            .then(data => {
                console.log(data);
                if (method==="DELETE")
                {this.setState({selectedPsychic:null});}
                else {this.setState({selectedPsychic:data});}
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });

    }

    render() {
        const { selectedPsychic, psychics } = this.state;
        return (<div>
                <div className="psychic-container">
                    <div className="psychic-dropdown-container">
                        <label htmlFor="psychicDropdown" className="ch-pr">Choose Precogs</label>
                        <select id="psychicDropdown" onChange={this.handlePsychicChange}>
                            <option value={null}>Choosing Precogs</option>
                            {psychics && this.state.psychics.map(psychic => (
                                <option key={psychic.id} value={psychic.id}>{psychic.preCogName}</option>
                            ))}
                        </select>
                        {selectedPsychic && this.state.selectedPsychic && (
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
                                        <td className="table-label">Level dopamine:</td>
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
                                        <td className="table-label">Date of comission:</td>
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
                {this.state.showModal_2 && <UpdatePrecogs onClose={this.closeModal_2} pre={this.state.selectedPsychic} />}
            {this.state.selectedPsychic && (<div><button className="ret-precog" onClick={this.fetchRetire}>Retire precog</button>
                <button className="reh-precog" onClick={this.fetchRehabilitate}>Rehabilitate</button>
                <button className="del-precog" onClick={this.fetchDel}>Delete Precog</button>
                <button className="upd-precog" onClick={this.openModal_2}>Update Info Precog</button></div>)}

                {this.state.selectedPsychic && ( <div className="containerStyle">
                    <div>
                        <h2>Prescribe Medications</h2>
                        <label htmlFor="serotonin">Serotonin:</label>
                        <input
                            type="number"
                            id="serotonin"
                            className="inputStyle"
                            value={this.state.serotonin}
                            onChange={(event) => this.handleNumberChange('serotonin', event)}
                        />
                        <button className="buttonStyle"
                                onClick={() => this.handleButtonClick('serotonin')}>Enter</button>
                    </div>

                    <div>
                        <label htmlFor="dopamine">Dopamine:</label>
                        <input
                            type="number"
                            id="dopamine"
                            className="inputStyle"
                            value={this.state.dopamine}
                            onChange={(event) => this.handleNumberChange('dopamine', event)}
                        />
                        <button className="buttonStyle"
                                onClick={() => this.handleButtonClick('dopamine')}>Enter</button>
                    </div>
                    <div>
                        <label htmlFor="depressant">Depressant:</label>
                        <input
                            type="number"
                            className="inputStyle"
                            id="depressant"
                            value={this.state.depressant}
                            onChange={(event) => this.handleNumberChange('depressant', event)}
                        />
                        <button className="buttonStyle"
                                onClick={() => this.handleButtonClick('depressant')}>Enter</button>
                    </div>
                </div>)}

            </div>
        );
    }
}

export default PrecogsList;
