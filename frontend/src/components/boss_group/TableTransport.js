import React, { Component } from 'react';

class TableTransport extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
        };
    }

    handleRowClick = (index) => {
        this.setState({ selectedRow: index }, ()=>{
            this.props.idTr(this.state.selectedRow)
        });
    };



    render() {
        const { carList } = this.props;
        const { selectedRow } = this.state;
        return (

            <div className="content-container-supply">
                <div className="table-container-supply">
            <table className="bg-rg">
                <thead>
                <tr>
                    <th className="table-label">Brand</th>
                    <th className="table-label">Model</th>
                    <th className="table-label">Remaining Fuel</th>
                    <th className="table-label">Maximum Fuel</th>
                    <th className="table-label">Condition</th>
                    <th className="table-label">In Operation</th>
                </tr>
                </thead>
                <tbody>

                {carList ? (carList.map((car) => (
                    <tr key={car.id}
                        className={car.id === selectedRow ? 'selected-row' : ''}
                        onClick={() => this.handleRowClick(car.id)}>
                        <td className="table-label-pr">{car.brand}</td>
                        <td className="table-label-pr">{car.model}</td>
                        <td className="table-label-pr">{car.remaining_fuel}</td>
                        <td className="table-label-pr">{car.maximum_fuel}</td>
                        <td className="table-label-pr">{car.condition}</td>
                        <td className="table-label-pr">{car.inOperation ? 'Yes' : 'No'}</td>
                    </tr>
                ))
                    )
                    :
                    (
                        <tr>
                            <td colSpan="5" className="table-label-pr">No transport data available</td>
                        </tr>
                    )
                }
                </tbody>
            </table>
            </div>
            </div>
        );
    }
}

export default TableTransport;
