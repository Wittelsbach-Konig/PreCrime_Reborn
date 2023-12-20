// CrimeForm.js
import React, { Component } from 'react';
import '../../css/CardCreate.css';
import '../../css/CrimeCard.css';
import TableVision from "../visions/TableVision";

class CrimeForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
            selectedVideoUrl: null,
            crimeTime: null,
            criminalName: null,
            victimName: null,
            placeOfCrime: '',
            weapon: '',
            crimeType: '',
            visionId: 0,
            crimeTimeInfo: '',
            criminalNameInfo: '',
            victimNameInfo: '',
            crimeTimeClicked: false,
            criminalNameClicked: false,
            victimNameClicked: false,
        };

    }

    handleRowClick = (index, url) => {
        this.setState({ selectedRow: index }, ()=>{
            this.setState({selectedVideoUrl:url})
            console.log(url)
        });

    };

    handleGetCrimeTime = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/cards/randomDateTime', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(data => {
                this.setState({ crimeTime: data });
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
        this.setState({ crimeTimeClicked: true });
    };

    handleGetCriminalName = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/cards/randomCriminalName', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(data => {
                this.setState({ criminalName: data });
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
        this.setState({ criminalNameClicked: true });
    };

    handleChangePlace = (e) => {
        this.setState({ placeOfCrime: e.target.value });
    }

    handleChangeWeapon = (e) => {
        this.setState({ weapon: e.target.value });
    }

    handleChangeType = (e) => {
        this.setState({ crimeType: e.target.value });
    }

    handleChangeVision = (e) => {
        this.setState({ visionId: e.target.value });
    }

    handleGetVictimName = () => {
        const token = localStorage.getItem('jwtToken');
        fetch('http://localhost:8028/api/v1/cards/randomVictimName', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(data => {
                this.setState({ victimName: data });
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
        this.setState({ victimNameClicked: true });
    };

    handleSubmit = () => {
        const { placeOfCrime, weapon, crimeType, selectedRow } = this.state;
        const parsedDate = new Date(this.state.crimeTime);
        console.log(parsedDate)
        const postData = {
            placeOfCrime: placeOfCrime,
            weapon: weapon,
            crimeType: crimeType,
            visionId: selectedRow,
            crimeTime: parsedDate,
            criminalName: this.state.criminalName,
            victimName: this.state.victimName,
        };
        console.log(postData)
        const token = localStorage.getItem('jwtToken');
       if (placeOfCrime && weapon && crimeType && selectedRow && postData.criminalName && parsedDate && postData.victimName) {
           fetch('http://localhost:8028/api/v1/cards/newcard', {
               method: 'POST',
               headers: {
                   'Content-Type': 'application/json',
                   'Authorization': `Bearer ${token}`,
               },
               body: JSON.stringify(postData),
           })
               .then(response => {
               if (!response.ok) {
               throw new Error(response.status);}
               else{
                   console.log('Данные успешно отправлены:')
                   this.props.onClose()
               }
           })
               .catch(error => {
                   console.log(error.message)
                   if (error.message === "500") {
                       window.alert("This vision is already in use");
                   } else {
                       window.alert("You haven't chosen a vision");
                   }});

       }
       else
       {window.alert("Input all fields")}
    };
    render() {
        const {
         placeOfCrime,
            weapon,
            crimeType,
            visionId,
            crimeTimeClicked,
            criminalNameClicked,
            victimNameClicked,
            selectedRow,
            selectedVideoUrl,
        } = this.state;

        const { onClose, visions } = this.props;
        return (
            <div className="modal">

            <div className="modal-content">
                <span className="close" onClick={onClose}>&times;</span>
                <h2 className="h-style">Create Card</h2>
                <table className="bg-rg">
                    <tbody>
                    <tr>
                        <td className="table-label-edit">Victim Name:</td>
                        <td colSpan="2" className="table-label-edit">{!victimNameClicked && (
                            <button onClick={this.handleGetVictimName}>Get Victim Name</button>
                        )}
                            {victimNameClicked && (
                                <input type="text" value={this.state.victimName} className="form-input" />
                            )}
                        </td>
                    </tr>
                    <tr>
                        <td className="table-label-pr">Criminal Name:</td>
                        <td colSpan="2" className="table-label-edit">
                            {!criminalNameClicked && (
                            <button onClick={this.handleGetCriminalName}>Get Criminal Name</button>
                        )}
                            {criminalNameClicked && (
                                <input type="text" value={this.state.criminalName}  className="form-input" />
                            )}
                        </td>
                    </tr>
                    <tr>
                        <td className="table-label-pr">Crime Time:</td>
                        <td colSpan="2" className="table-label-edit">
                            {!crimeTimeClicked && (
                                <button onClick={this.handleGetCrimeTime} >Get Crime Time</button>
                            )}
                            {crimeTimeClicked && (
                                <input type="text" value={this.state.crimeTime}  className="form-input" />
                            )}
                        </td>
                    </tr>
                    <tr>
                        <td className="table-label-pr">Place of Crime:</td>
                        <td colSpan="2" className="table-label-edit">
                            <input maxLength="40" type="text" name="placeOfCrime" value={placeOfCrime}
                                   onChange={this.handleChangePlace} className="form-input" />
                        </td>
                    </tr>
                    <tr>
                        <td className="table-label-pr">Weapon:</td>
                        <td colSpan="2" className="table-label-edit">
                            <input maxLength="20" type="text" name="weapon" value={weapon}
                                   onChange={this.handleChangeWeapon} className="form-input" />
                        </td>
                    </tr>
                    <tr>
                        <td className="table-label-pr">Crime Type:</td>
                        <td colSpan="2" className="table-label-edit">
                            <select value={this.state.crimeType} onChange={this.handleChangeType}>
                                <option className="table-select" value="" name="crimeType">choose state</option>
                                <option className="table-select" value="INTENTIONAL" name="crimeType">INTENTIONAL</option>
                                <option className="table-select" value="UNINTENTIONAL" name="crimeType">UNINTENTIONAL</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td className="table-label-pr">Visions:</td>
                        <td className="table-label-edit">
                                    <table className="bg-rg-edit">
                                        <tbody>
                                        {visions ? (
                                            visions.map((vision) => (
                                                <tr
                                                    key={vision.id}
                                                    className={vision.id === selectedRow ? 'selected-row' : ''}
                                                    onClick={() => this.handleRowClick(vision.id, vision.videoUrl)}
                                                >
                                                    <td className="table-label-edit-rel"> Vision {vision.id}</td>
                                                </tr>
                                            ))
                                        ) : (
                                            <tr>
                                                <td colSpan="1">No vision data available</td>
                                            </tr>
                                        )}
                                        </tbody>
                                    </table>
                        </td>
                        <td className="table-label-edit">
                            <div className="video-container-card">
                                    {selectedVideoUrl && (

                                        <iframe
                                            title="Vision Video"
                                            src={selectedVideoUrl}
                                            frameBorder="0"
                                            allowFullScreen
                                        ></iframe>)}
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>

                <button type="button-tr" onClick={this.handleSubmit} className="button-edit">Submit</button>

            </div>
            </div>
        );
    }
}

export default CrimeForm;