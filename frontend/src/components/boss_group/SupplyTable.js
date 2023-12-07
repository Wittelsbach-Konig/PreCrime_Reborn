import React, { Component } from 'react';

class SupplyTable extends Component {
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
        const { supplyList } = this.props;
        const { selectedRow } = this.state;
        return (

            <div className="content-container-supply">
                <div className="table-container-supply">
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Amount</th>
                            <th>Max Amount</th>
                            <th>Type</th>
                        </tr>
                        </thead>
                        <tbody>

                        {supplyList ? (supplyList.map((supply) => (
                            <tr
                                key={supply.id}
                                className={supply.id === selectedRow ? 'selected-row' : ''}
                                onClick={() => this.handleRowClick(supply.id)}
                            >
                                <td>{supply.id}</td>
                                <td>{supply.resourceName}</td>
                                <td>{supply.amount}</td>
                                <td>{supply.maxPossibleAmount}</td>
                                <td>{supply.type}</td>
                            </tr>
                        ))):
                            (
                            <tr>
                                <td colSpan="5">No ammunition data available</td>
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

export default SupplyTable;
