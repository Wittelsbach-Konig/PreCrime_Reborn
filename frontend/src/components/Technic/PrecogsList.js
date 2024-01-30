import React, { Component } from 'react';
import UpdatePrecogs from "./UpdatePrecogs";
import RegPrecogs from "../Technic/RegPrecogs";
import FormData from "form-data";
import update from "../../img/correct.png";
import close from "../../img/close.png";
import retire from "../../img/retirePr.png"
import rehab from "../../img/rehabPr.png"
class PrecogsList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            showModal_2: false,
            psychics: null,
            selectedPsychic: null,
            serotoninLevel: 0,
            dopamineLevel: 0,
            stressLevel: 0,
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
        this.fetchRenew(`api/v1/precogs/${this.state.selectedPsychic.id}`,token, 'GET');
    };

    componentDidMount() {
        this.fetchPsychics();
    }


    fetchPsychics = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('api/v1/precogs', {
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
                data!==null && this.setState({ selectedPsychic: data[0] });
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
        const medic = fieldName==="stressLevel" ? this.state.selectedPsychic[fieldName]:(100 - this.state.selectedPsychic[fieldName])
        console.log(medic)
        parseInt(event.target.value, 10) <= medic &&
        parseInt(event.target.value, 10) >= 0 &&
        this.setState({ [fieldName]: parseInt(event.target.value, 10) });
    };

    handleButtonClick = (fieldName) => {
        const {url} = this.state
        const token = localStorage.getItem('jwtToken');
        const FormData = require('form-data');
        const form = new FormData();
        form.append(`amount`, this.state[fieldName]);
        console.log(this.state.selectedPsychic.id)
            if(fieldName==="serotoninLevel")
            {this.fetchChange(token,`api/v1/precogs/${this.state.selectedPsychic.id}/enterserotonin`,form)}
            if(fieldName==="dopamineLevel")
            { this.fetchChange(token,`api/v1/precogs/${this.state.selectedPsychic.id}/enterdopamine`,form)}
            if(fieldName==="stressLevel")
            { this.fetchChange(token,`api/v1/precogs/${this.state.selectedPsychic.id}/enterdepressant`,form)}





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
                this.fetchRenew(`api/v1/precogs/${this.state.selectedPsychic.id}`,token, 'GET')
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });

    }

    fetchRetire = () =>
    {
        const token = localStorage.getItem('jwtToken');
        this.fetchRenew(`api/v1/precogs/${this.state.selectedPsychic.id}/retire`,token, 'POST')

    }

    fetchRehabilitate = () =>
    {
        const token = localStorage.getItem('jwtToken');
        this.fetchRenew(`api/v1/precogs/${this.state.selectedPsychic.id}/rehabilitate`,token, 'POST')

    }

    fetchDel = () =>
    {
        const token = localStorage.getItem('jwtToken');
        const confirmation = window.confirm(`Are you sure delete this precog?`);

        if (confirmation) {
        this.fetchRenew(`api/v1/precogs/${this.state.selectedPsychic.id}`,token, 'DELETE')}

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
        return (<div className="psyc">
                <header className="header-pr">
                    {this.state.showModal && <RegPrecogs onClose={this.closeModal} onRenew={this.fetchPsychics} />}
                    {this.state.showModal_2 && <UpdatePrecogs onClose={this.closeModal_2} pre={this.state.selectedPsychic} />}
                    <button className="new-precog" onClick={this.openModal}>New Precog</button>
                </header>
                <div className="psychic-container">
                    <div className="psychic-dropdown-container">
                        <label htmlFor="psychicDropdown" className="ch-pr">Precogs</label>
                        <select id="psychicDropdown" onChange={this.handlePsychicChange}>

                            {psychics ? this.state.psychics.map(psychic => (
                                <option key={psychic.id} value={psychic.id}>{psychic.preCogName}</option>
                            ))
                            : <option value={null}>Registration precog</option>}
                        </select>
                        {selectedPsychic && this.state.selectedPsychic && (
                            <div className="psychic-info">
                                <h2 className="name-precog">{this.state.selectedPsychic.preCogName}</h2>
                                <table className="bg-rg">
                                    <tbody>
                                    <tr>
                                        <td className="table-label">ID:</td>
                                        <td className="table-label">{this.state.selectedPsychic.id}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">Age:</td>
                                        <td className="table-label">{this.state.selectedPsychic.age}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">Level dopamine:</td>
                                        <td className="table-label">{this.state.selectedPsychic.dopamineLevel}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">Level serotonin:</td>
                                        <td className="table-label">{this.state.selectedPsychic.serotoninLevel}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">Stress level:</td>
                                        <td className="table-label">{this.state.selectedPsychic.stressLevel}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">Date of comission:</td>
                                        <td className="table-label">{this.state.selectedPsychic.commissionedOn}</td>
                                    </tr>
                                    <tr>
                                        <td className="table-label">On Working:</td>
                                        <td className="table-label">{this.state.selectedPsychic.work ? 'Yes' : 'No'}</td>
                                    </tr>
                                    <tr className="special">
                                        <td className="table-label" colSpan='2'>
                                        {selectedPsychic.work ?
                                            <td className="table-label-pr">
                                                <button className="fuel-but" onClick={this.fetchRetire}>
                                                    <img src={retire} className="fuel" alt="Кнопка «button»"/>
                                                </button>
                                                <div className="tooltip" id="tooltip">Retire</div>
                                            </td>
                                            :
                                            <td className="table-label-pr">
                                                <button className="fuel-but" onClick={this.fetchRehabilitate}>
                                                    <img src={rehab} className="fuel" alt="Кнопка «button»"/>
                                                </button>
                                                <div className="tooltip" id="tooltip">Rehabilitate</div>
                                            </td>}
                                        <td className="table-label-pr">
                                            <button className="fuel-but" onClick={this.openModal_2}>
                                                <img src={update} className="fuel" alt="Кнопка «button»"/>
                                            </button>
                                            <div className="tooltip" id="tooltip">Update</div>
                                        </td>
                                        <td className="table-label-pr">
                                            <button className="fuel-but" onClick={this.fetchDel}>
                                                <img src={close} className="fuel" alt="Кнопка «button»"/>
                                            </button>
                                            <div className="tooltip" id="tooltip">Delete</div>
                                        </td>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>
                </div>

                {this.state.selectedPsychic && (
                    <div className="draggableContainer">
                    <div className="containerStyle">
                        <div className="row">
                            <label htmlFor="serotoninLevel" className="labelStyle">Serotonin:</label>
                            <input
                                type="number"
                                id="serotoninLevel"
                                className="inputStyle"
                                value={this.state.serotoninLevel}
                                onChange={(event) => this.handleNumberChange('serotoninLevel', event)}
                            />
                            <button className="buttonStyle" onClick={() => this.handleButtonClick('serotoninLevel')}>Enter</button>
                        </div>

                        <div className="row">
                            <label htmlFor="dopamineLevel" className="labelStyle">Dopamine:</label>
                            <input
                                type="number"
                                id="dopamineLevel"
                                className="inputStyle"
                                value={this.state.dopamineLevel}
                                onChange={(event) => this.handleNumberChange('dopamineLevel', event)}
                            />
                            <button className="buttonStyle" onClick={() => this.handleButtonClick('dopamineLevel')}>Enter</button>
                        </div>

                        <div className="row">
                            <label htmlFor="stressLevel" className="labelStyle">Depressant:</label>
                            <input
                                type="number"
                                id="stressLevel"
                                className="inputStyle"
                                value={this.state.stressLevel}
                                onChange={(event) => this.handleNumberChange('stressLevel', event)}
                            />
                            <button className="buttonStyle" onClick={() => this.handleButtonClick('stressLevel')}>Enter</button>
                        </div>
                    </div>
                    </div>)}

            </div>
        );
    }
}

export default PrecogsList;
