import React, { Component } from 'react';

class TableGroup extends Component {
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
        const { groupList } = this.props;
        const { selectedRow } = this.state;
        return (

            <div className="content-container-group">
                <div className="table-container-group">
                    <table className="bg-rg">
                        <thead>
                        <tr>
                            <th className="table-label">Member Name</th>
                            <th className="table-label">Telegram ID</th>
                            <th className="table-label">in Operation</th>
                        </tr>
                        </thead>
                        <tbody>

                        {groupList ? (groupList.map((group) => (
                            <tr
                                key={group.id}
                                className={group.id === selectedRow ? 'selected-row' : ''}
                                onClick={() => this.handleRowClick(group.id)}
                            >
                                <td className="table-label-pr">{group.memberName}</td>
                                <td className="table-label-pr">{group.telegramId}</td>
                                <td className="table-label-pr">{group.inOperation ? 'Yes' : 'No'}</td>
                            </tr>
                        )))
                        :
                            (<tr>
                                <td colSpan="3" className="table-label-pr">No data available</td>
                            </tr>)}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default TableGroup;
