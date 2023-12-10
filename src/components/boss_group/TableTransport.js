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

            <div className="content-container">
            <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Brand</th>
                    <th>Model</th>
                    <th>Remaining Fuel</th>
                    <th>Maximum Fuel</th>
                    <th>Condition</th>
                    <th>In Operation</th>
                </tr>
                </thead>
                <tbody>

                {carList ? (carList.map((car) => (
                    <tr key={car.id}
                        className={car.id === selectedRow ? 'selected-row' : ''}
                        onClick={() => this.handleRowClick(car.id)}>
                        <td>{car.id}</td>
                        <td>{car.brand}</td>
                        <td>{car.model}</td>
                        <td>{car.remaining_fuel}</td>
                        <td>{car.maximum_fuel}</td>
                        <td>{car.condition}</td>
                        <td>{car.inOperation ? 'Yes' : 'No'}</td>
                    </tr>
                ))
                    )
                    :
                    (
                        <tr>
                            <td colSpan="5">No transport data available</td>
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
