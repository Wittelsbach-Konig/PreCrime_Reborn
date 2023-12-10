import React, { Component } from 'react';

class TableGroup extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
        };
    }

    handleRowsClick = (id) => {
        const { selectedRows } = this.state;
        const isSelected = selectedRows.includes(id);

        if (isSelected) {

            this.setState({
                selectedRows: selectedRows.filter(selectedId => selectedId !== id),
            });
        } else {

            this.setState({
                selectedRows: [...selectedRows, id],
            });
        }

    };

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
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Member Name</th>
                            <th>Telegram ID</th>
                            <th>in Operation</th>
                        </tr>
                        </thead>
                        <tbody>

                        {groupList.map((group) => (
                            <tr
                                key={group.id}
                                className={group.id === selectedRow ? 'selected-row' : ''}
                                onClick={() => this.handleRowClick(group.id)}
                            >
                                <td>{group.id}</td>
                                <td>{group.memberName}</td>
                                <td>{group.telegramId}</td>
                                <td>{group.inOperation ? 'Yes' : 'No'}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default TableGroup;
