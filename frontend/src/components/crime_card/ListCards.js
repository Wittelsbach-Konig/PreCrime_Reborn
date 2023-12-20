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
                    <table className="bg-rg">
                        <thead>
                        <tr>
                            <th className="table-label">Victim Name</th>
                            <th className="table-label">Criminal Name</th>
                            <th className="table-label">Weapon</th>
                            <th className="table-label">Crime Type</th>
                            <th className="table-label">Detective</th>
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
                                    <td className="table-label">{crime.victimName}</td>
                                    <td className="table-label">{crime.criminalName}</td>
                                    <td className="table-label">{crime.weapon}</td>
                                    <td className="table-label">{crime.crimeType}</td>
                                    <td className="table-label">{crime.responsibleDetective}</td>
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
