import React, { Component } from 'react';
import AppointGroup from "./AppointGroup";

class CriminalTable extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
            showCriminal: false,
            criminalInfo: false,
            showGroup: false,
            showModal:false,
            groupList:null,
            assGroup:null,
            criminalList:null,
            newStatus: '',
            options: [
                { id: 1, label: 'CAUGHT' },
                { id: 2, label: 'NOT_CAUGHT' },
                { id: 3, label: 'ESCAPED' },
            ],
            selectedOptions: [],
        };
    }


    handleRowClick = (index, criminal) => {
        const token = localStorage.getItem('jwtToken');
        this.setState({ selectedRow: index }, ()=>{
            console.log(index)
            this.setState({showCriminal:true})
            this.setState({criminalInfo:criminal})
            criminal.isArrestAssigned &&
            fetch(`api/v1/reactiongroup/criminal/${criminal.id}/assignedgroup`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
                },
            })
                .then(responses => responses.json())
                .then(data => {

                    const memberNames = data.map(item => item.memberName);

                    const resultString = memberNames.join(', ');
                    this.setState({assGroup:resultString})
                    console.log(resultString);
                })
                .catch(error => {
                    console.error('Ошибка при запросе к серверу:', error);
                })
        });

    };

    showFilter = () => {
        this.setState({ showModal: !this.state.showModal });
    };

    closeGroup = () => {
        this.setState({ showGroup: false })
    };

    componentDidMount() {
        this.fetchCriminal()
        this.handleRowClick(this.state.selectedRow,this.state.criminalInfo)
    }

    handleCheckboxChange = (option) => {
        const { selectedOptions } = this.state;
        const isSelected = selectedOptions.some((selected) => selected.id === option.id);
        const updatedOptions = isSelected
            ? selectedOptions.filter((selected) => selected.id !== option.id)
            : [...selectedOptions, option];

        this.setState({
            selectedOptions: updatedOptions,
        }, ()=>{console.log(selectedOptions)});
        this.setState({showCriminal:false})
    };

    getGroup = () => {
        this.setState({ showGroup: true })
        const token = localStorage.getItem('jwtToken');
        fetch('api/v1/reactiongroup/all', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
        })
            .then(responses => responses.json())
            .then(data => {
                this.setState({groupList:data})
                console.log(this.state.groupList)
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            })
    };

    handleChangeStatus = (event) => {
        const newStatus = event.target.value;
        const confirmation = window.confirm(`Вы уверены, что хотите изменить статус на ${newStatus}?`);

        if (confirmation) {
            this.updateStatusOnServer(newStatus);
            this.fetchCriminal()
        } else {
            this.setState({ newStatus: '' });
        }
    };

    updateStatusOnServer = (newStatus) => {
        const {criminalInfo} = this.state
        const token = localStorage.getItem('jwtToken');
        console.log(newStatus)
        fetch(`api/v1/reactiongroup/criminal/${criminalInfo.id}?status=${newStatus}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(responses => responses.json())
            .then(data => {
                console.log(data)
                this.setState({showCriminal:false})
                this.fetchCriminal()

            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
                this.fetchCriminal()
            });
        this.fetchCriminal()
    };

    fetchCriminal = () =>{
        const token = localStorage.getItem('jwtToken');

        const handleApiResponse = async (response, property) => {
            if (response.ok) {
                try {
                    return await response.json();
                } catch (error) {
                    console.error('Error parsing JSON:', error);
                    return null;
                }
            } else {
                console.error(`Error fetching data: ${response.status}`);
                return null;
            }
        };
        Promise.all([
            fetch(`api/v1/reactiongroup/criminal`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
            }).then(response => handleApiResponse(response, 'criminalList')),

            fetch(`api/v1/reactiongroup/caughtcriminal`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
            }).then(response => handleApiResponse(response, 'criminalList')),

            fetch(`api/v1/reactiongroup/escapedcriminal`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
            }).then(response => handleApiResponse(response, 'criminalList')),
        ]).then(dataArray => {
            const mergedData = dataArray.reduce((acc, data) => (data ? [...acc, ...data] : acc), []);
            this.setState({ criminalList: mergedData });
        }).catch(error => {
            console.error('Error fetching data:', error);
            this.setState({ criminalList: null });
        });
    }


    render() {
        const { selectedRow, showCriminal, criminalInfo, newStatus, assGroup, criminalList, showModal } = this.state;
        const { options, selectedOptions } = this.state
        return (
            <div>
                <header className="header-pr">
                    {!showModal ? <button className="acc-vis" onClick={this.showFilter}>Filter ↓</button> :
                        <button className="acc-vis" onClick={this.showFilter}>Filter ↑</button>}
                    {showModal &&
                        <div className="multi-select-menu">
                            <ul>
                                {options.map((option) => (
                                    <li key={option.id}>
                                        <label>
                                            <input
                                                type="checkbox"
                                                checked={selectedOptions.some((selected) => selected.id === option.id)}
                                                onChange={() => this.handleCheckboxChange(option)}
                                            />
                                            {option.label === "NOT_CAUGHT" ? 'NOT CAUGHT':option.label}
                                        </label>
                                    </li>
                                ))}
                            </ul>
                        </div>}
                </header>
                <h1 className="car-text">Criminal info</h1>
                {this.state.showGroup && <AppointGroup onClose={this.closeGroup} groupList={this.state.groupList}
                                                       idCr={this.state.criminalInfo.id} onRenew={this.fetchCriminal}/>}
                {showCriminal && (
                    <div className="criminal-statistic">
                        <table className="bg-rg">
                            <tbody>
                            <tr>
                                <td colSpan="2"
                                    className="table-label-edit-td">
                                    Criminal Info
                                </td>
                            </tr>
                            <tr>
                                <td className="table-label">ID:</td>
                                <td className="table-label">{criminalInfo.id}</td>
                            </tr>
                            <tr>
                                <td className="table-label">Name:</td>
                                <td className="table-label">{criminalInfo.name}</td>
                            </tr>
                            <tr>
                                <td className="table-label">Location:</td>
                                <td className="table-label">{criminalInfo.location}</td>
                            </tr>
                            <tr>
                                <td className="table-label">Weapon:</td>
                                <td className="table-label">{criminalInfo.weapon}</td>
                            </tr>
                            <tr>
                                <td className="table-label">Group assigned</td>
                                {criminalInfo.isArrestAssigned ?
                                    <td className="table-label-prr">{assGroup}</td>:
                                    <td className="table-label-prr">Group is not assigned</td>}
                            </tr>

                            {criminalInfo.isArrestAssigned && criminalInfo.status === 'NOT_CAUGHT' ?
                            <tr>
                                <td className="table-label">Status:</td>
                                <td className="table-label-edit-rel">
                                    <select
                                        value={newStatus}
                                        onChange={this.handleChangeStatus}>
                                        <option value="">Choose state</option>
                                        <option value="CAUGHT">CAUGHT</option>
                                        <option value="ESCAPED">ESCAPED</option>
                                    </select>
                                </td>
                            </tr>:
                            <tr>
                                <td className="table-label">Status:</td>
                                <td className="table-label">
                                    {criminalInfo.status === 'NOT_CAUGHT' ? 'NOT CAUGHT':criminalInfo.status}
                                </td>
                            </tr>}
                            </tbody>
                        </table>
                    </div>
                )}
            <div className="content-container-criminal">
                <div className="table-container-precog">
                    <table className="bg-rg">
                        <thead>
                        <tr>
                            <th className="table-label">Name</th>
                            <th className="table-label">Location</th>
                            <th className="table-label">Weapon</th>
                            <th className="table-label">Group</th>
                        </tr>
                        </thead>
                        <tbody>

                        {criminalList ? (criminalList.map((criminal) => (
                            ((selectedOptions.length===0 &&
                                criminal.status === 'NOT_CAUGHT')   ||
                            (selectedOptions.some((option) => option.label.toLowerCase() === criminal.status.toLowerCase()))) &&
                            <tr
                                key={criminal.id}
                                className={criminal.id === selectedRow ? 'selected-row' : ''}
                                onClick={() => this.handleRowClick(criminal.id, criminal)}
                            >
                                <td className="table-label-edit">{criminal.name}</td>
                                <td className="table-label-edit">{criminal.location}</td>
                                <td className="table-label-edit">{criminal.weapon}</td>
                                {criminal.isArrestAssigned ?
                                    <td className="table-label-edit">Group appointed</td>:
                                    <td className="table-label-edit"><button onClick={this.getGroup}>to appoint</button></td>
                                }
                            </tr>
                        )))
                        : (<tr>
                                <td colSpan="4" className="table-label">No criminal data available</td>
                            </tr>)
                        }
                        </tbody>
                    </table>
                </div>
            </div>

            </div>
        );
    }
}

export default CriminalTable;
