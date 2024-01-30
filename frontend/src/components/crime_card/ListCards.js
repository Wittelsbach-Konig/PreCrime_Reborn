// Import React and other necessary modules
import React, { Component } from 'react';
import CrimeCard from "./crimeCard";
import "../../css/CrimeTable.css"
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";


class ListCards extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
            clickCrime: null,
            isFormOpen: false,
            startDate: new Date("01-01-2023 00:00:00"),
            endDate: new Date("01-01-2032 00:00:00"),
            showModal: false,
        };
    }

    handleRowClick = (index) => {
        this.setState({ selectedRow: index }, () => {
            this.setState({ clickCrime: this.props.crimeList.find((crime) => crime.id === index) });
        });
    };

    openModal = () => {
        this.setState({showModal: !this.state.showModal});
    }

    handleRowDoubleClick = (index) => {
        this.handleRowClick(index);
        this.setState({ isFormOpen: true });
    };

    closeForm = () => {
        this.setState({ isFormOpen: false });
    };

    handleDateChange = (date, type) => {
        this.setState({
            [type === "start" ? "startDate" : "endDate"]: date,
        }, ()=>{this.filterDataByDate()});
    };

    filterDataByDate = () => {
        const { startDate, endDate } = this.state;
        if (new Date('01-01-2022 00:00:00')>=new Date(startDate))
        {
            console.log("Сомнительно, ну окэй")
        }
        else
        {
            console.log("Ты что то перепутал")
        }
        console.log("Filtered data:", startDate, endDate);
    };

    render() {
        const { crimeList, role } = this.props;
        const { selectedRow, clickCrime, isFormOpen } = this.state;
        const { startDate, endDate, showModal } = this.state;

        return (<div>
                <header className='header-pr'>
                    {!showModal ? <button className="acc-vis" onClick={this.openModal}>Filter ↓</button> :
                        <button className="acc-vis" onClick={this.openModal}>Filter ↑</button>}
                    {showModal &&
                        <div>
                            <div className="date_pick_1">
                            <h5 className="h-style">Date create</h5>
                            </div>
                        <div className="date_pick">
                            {/* Компоненты выбора даты и времени для начальной и конечной даты */}
                            <DatePicker
                                selected={startDate}
                                onChange={(date) => this.handleDateChange(date, "start")}
                                showTimeSelect
                                dateFormat="dd-MM-yyyy h:mm:ss"
                            />
                        </div>
                        <div className="date_pick">
                            <DatePicker
                                selected={endDate}
                                onChange={(date) => this.handleDateChange(date, "end")}
                                showTimeSelect
                                dateFormat="dd-MM-yyyy h:mm:ss"
                            />
                        </div>
                        </div>
                    }
                </header>
            <div className="crime-content-container">
                    <h2 className="h-style">Card List</h2>
                <div className="crime-table-container">
                    <table className="bg-rg">
                        <thead>
                        <tr>
                            <th className="table-label">Victim Name</th>
                            <th className="table-label">Criminal Name</th>
                            <th className="table-label">Weapon</th>
                            <th className="table-label">Creation Date</th>
                            {role === 'AUDITOR' &&
                            <th className="table-label">Detective</th>
                            }
                        </tr>
                        </thead>
                        <tbody>
                        {crimeList && crimeList.length > 0 ? (
                            crimeList.map((crime) => (
                                 ((new Date(crime.creationDate) <= endDate && new Date(crime.creationDate) >= startDate) &&
                                    <tr
                                        key={crime.id}
                                        className={crime.id === selectedRow ? 'selected-row' : ''}
                                        onClick={() => this.handleRowClick(crime.id)}
                                        onDoubleClick={() => this.handleRowDoubleClick(crime.id)}
                                    >
                                        <td className="table-label-pr">{crime.victimName}</td>
                                        <td className="table-label-pr">{crime.criminalName}</td>
                                        <td className="table-label-pr">{crime.weapon}</td>
                                        <td className="table-label-pr">{crime.creationDate}</td>
                                        {role === 'AUDITOR' && (
                                            <td className="table-label-pr">{crime.responsibleDetective}</td>
                                        )}
                                    </tr>
                                ))))
                                :(
                            <tr>
                                <td colSpan="5">No crime data available</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
                {clickCrime && isFormOpen && (
                    <div className="crime-card-container">
                        <CrimeCard crimeCard={clickCrime} onClose={this.closeForm} onRenew={this.props.onRenew} role={this.props.role}/>
                    </div>
                )}
            </div>
            </div>
        );
    }
}

// Экспорт компонента
export default ListCards;
