// Import React and other necessary modules
import React, { Component } from 'react';
import CrimeCard from "./crimeCard";
import "../../css/CrimeTable.css"
class ListCards extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
            clickCrime: null,
            isFormOpen: false,
        };
    }

    handleRowClick = (index) => {
        this.setState({ selectedRow: index }, () => {
            this.setState({ clickCrime: this.props.crimeList.find((crime) => crime.id === index) });
        });
    };

    handleRowDoubleClick = (index) => {
        this.handleRowClick(index);
        this.setState({ isFormOpen: true });
    };

    closeForm = () => {
        this.setState({ isFormOpen: false });
    };

    render() {
        const { crimeList } = this.props;
        const { selectedRow, clickCrime, isFormOpen } = this.state;
        const selectedCrime = crimeList && crimeList.length > 0
            ? crimeList.find((crime) => crime.id === selectedRow)
            : null;

        return (
            <div className="crime-content-container">
                <div className="crime-table-container">
                    <table>
                        <thead>
                        <tr>
                            <th>Victim Name</th>
                            <th>Criminal Name</th>
                            <th>Weapon</th>
                            <th>Crime Type</th>
                            <th>Responsible Detective</th>
                        </tr>
                        </thead>
                        <tbody>
                        {crimeList && crimeList.length > 0 ? (
                            crimeList.map((crime) => (
                                <tr
                                    key={crime.id}
                                    className={crime.id === selectedRow ? 'selected-row' : ''}
                                    onClick={() => this.handleRowClick(crime.id)}
                                    onDoubleClick={() => this.handleRowDoubleClick(crime.id)}
                                >
                                    <td>{crime.victimName}</td>
                                    <td>{crime.criminalName}</td>
                                    <td>{crime.weapon}</td>
                                    <td>{crime.crimeType}</td>
                                    <td>{crime.responsibleDetective}</td>
                                </tr>
                            ))
                        ) : (
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
        );
    }
}

// Экспорт компонента
export default ListCards;
