import React, { Component } from 'react';
class SupplyTable extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
            data:this.props.supplyList,
            type:'Type'
        };
    }

    handleRowClick = (index) => {
        this.setState({ selectedRow: index }, ()=>{
            this.props.idTr(this.state.selectedRow)
        });

    };

    handleChangeType = (e) => {
        this.setState({ type: e.target.value },()=>{

            console.log(this.state.type)
            const token = localStorage.getItem('jwtToken');

            const baseUrl = 'http://localhost:8028/api/v1/reactiongroup/supply/filter';

            const types = [this.state.type];

            const queryString = `types=${types.join(',')}`;
            const url = `${baseUrl}?${queryString}`;
            if(this.state.type==='Type'){
                this.setState({data:this.props.supplyList})
                // this.props.onRenew();
            }
            else {
                fetch(url,{
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`,
                    },
                    body:this.state.type
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        // Обрабатываем полученные данные
                        console.log(data);
                    })
                    .catch(error => {
                        console.error('There has been a problem with your fetch operation:', error);
                    });
            }
            ;
        });

    }

    render() {
        const { supplyList } = this.props;
        const { selectedRow, data } = this.state;
        return (

            <div className="content-container-supply">
                <div className="table-container-supply">
                    <table className="bg-rg">
                        <thead>
                        <tr>
                            <th className="table-label">Name</th>
                            <th className="table-label">Amount</th>
                            <th className="table-label">Max Amount</th>
                            <th className="table-label">
                                {
                                 //   Type
                                }
                                (<select
                                    value={this.state.data.crimeType}
                                    onChange={this.handleChangeType}>
                                    <option className="table-select" value="" name="crimeType">Type</option>
                                    <option className="table-select" value="AMMUNITION" name="crimeType">AMMUNITION</option>
                                    <option className="table-select" value="WEAPON" name="crimeType">WEAPON</option>
                                    <option className="table-select" value="GADGET" name="crimeType">GADGET</option>
                                    <option className="table-select" value="FUEL" name="crimeType">FUEL</option>
                                </select>)
                            </th>
                        </tr>
                        </thead>
                        <tbody>

                        {data ? (data.map((supply) => (
                            <tr
                                key={supply.id}
                                className={supply.id === selectedRow ? 'selected-row' : ''}
                                onClick={() => this.handleRowClick(supply.id)}
                            >
                                <td className="table-label-pr">{supply.resourceName}</td>
                                <td className="table-label-pr">{supply.amount}</td>
                                <td className="table-label-pr">{supply.maxPossibleAmount}</td>
                                <td className="table-label-pr">{supply.type}</td>
                            </tr>
                        ))):
                            (
                            <tr>
                                <td colSpan="4" className="table-label-pr">No ammunition data available</td>
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
