// CrimeForm.js
import React, { Component } from 'react';
import '../../css/CardCreate.css';

class CrimeForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
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
        const { placeOfCrime, weapon, crimeType, visionId } = this.state;
        const parsedDate = new Date(this.state.crimeTime);
        console.log(parsedDate)
        const postData = {
            placeOfCrime,
            weapon,
            crimeType,
            visionId,
            crimeTime: parsedDate,
            criminalName: this.state.criminalName,
            victimName: this.state.victimName,
        };

        const token = localStorage.getItem('jwtToken');

        fetch('http://localhost:8028/api/v1/cards/newcard', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(postData),
        })
            .then(response => response.json())
            .then(responseData => {
                console.log('Данные успешно отправлены:', responseData);

            })
            .catch(error => console.error('Ошибка при отправке данных:', error));
        this.props.onClose()
    };
    render() {
        const {
         placeOfCrime,
            weapon,
            crimeType,
            visionId,
            crimeTimeClicked,
            criminalNameClicked,
            victimNameClicked
        } = this.state;

        const { onClose } = this.props;
        return (
            <div className="modal">

            <div className="modal-content">
                <span className="close" onClick={onClose}>&times;</span>
                {!crimeTimeClicked && (
                    <button onClick={this.handleGetCrimeTime} className="form-button-cc">Get Crime Time</button>
                )}
                {!criminalNameClicked && (
                    <button onClick={this.handleGetCriminalName} className="form-button-cc">Get Criminal Name</button>
                )}
                {!victimNameClicked && (
                    <button onClick={this.handleGetVictimName} className="form-button-cc">Get Victim Name</button>
                )}
                {crimeTimeClicked && (
                    <label className="form-label-cc">Crime Time</label>
                )}
                {criminalNameClicked && (
                    <label  className="form-label-cc">Criminal Name</label>
                )}
                {victimNameClicked && (
                    <label  className="form-label-cc">Victim Name</label>
                )}
                <div className="crime-info">
                    <input type="text" value={this.state.crimeTime}  className="form-input" />
                    <input type="text" value={this.state.criminalName}  className="form-input" />
                    <input type="text" value={this.state.victimName} className="form-input" />
                </div>
                <form>

                    <label>
                        Place of Crime:
                        <input type="text" name="placeOfCrime" value={placeOfCrime} onChange={this.handleChangePlace} className="form-input" />
                    </label>
                    <br />
                    <label>
                        Weapon:
                        <input type="text" name="weapon" value={weapon} onChange={this.handleChangeWeapon} className="form-input" />
                    </label>
                    <br />
                    <label>
                        Crime Type:
                        <input type="text" name="crimeType" value={crimeType} onChange={this.handleChangeType} className="form-input" />
                    </label>
                    <br />
                    <label>
                        Vision ID:
                        <input type="number" name="visionId" value={visionId} onChange={this.handleChangeVision} className="form-input" />
                    </label>
                    <br />
                    <button type="button-tr" onClick={this.handleSubmit} className="form-button">Submit</button>
                </form>
            </div>
            </div>
        );
    }
}

export default CrimeForm;