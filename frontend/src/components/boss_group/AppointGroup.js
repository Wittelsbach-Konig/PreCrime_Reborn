import React, { Component } from 'react';

class AppointGroup extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRows: [],
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
            }, ()=>{console.log(id)});
        }


    };

    appointing = () => {
        const token = localStorage.getItem('jwtToken');
        const { selectedRows } = this.state
        const { idCr } = this.props

        const grr = selectedRows.length !== 0 ? (selectedRows.length === 1 ? [selectedRows] : selectedRows) : [];
        console.log(grr)
        fetch(`http://localhost:8028/api/v1/reactiongroup/criminal/${idCr}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
            body: JSON.stringify(grr),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }

                return response.text();
            })
            .then(data => {
                console.log(data);
                this.props.onRenew();
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });

        this.props.onClose();


    };


    render() {
        const { groupList, onClose } = this.props;
        const { selectedRows } = this.state;

        return (<div>
                <div className="overlay"></div>
                    <div className="modal-container">
                        <span className="close" onClick={onClose}>&times;</span>
                        <div className="content-container-group-cr">
                            <div className="table-container-group-cr">
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

                                    {groupList ? (groupList.map((group) => (
                                        <tr
                                            key={group.id}
                                            className={this.state.selectedRows.includes(group.id) ? 'selected-row' : ''}
                                            onClick={() => this.handleRowsClick(group.id)}
                                        >
                                            <td>{group.id}</td>
                                            <td>{group.memberName}</td>
                                            <td>{group.telegramId}</td>
                                            <td>{group.inOperation ? 'Yes' : 'No'}</td>
                                        </tr>
                                    )))
                                    : (
                                            <tr>
                                                <td colSpan="4">No group data available</td>
                                            </tr>
                                        )}
                                    </tbody>
                                </table>
                                <div className="modal-buttons">
                                    <button onClick={this.appointing}>Appoint Group</button>
                                </div>
                            </div>
                        </div>

                        </div>
                    </div>
        );
    }
}

export default AppointGroup;
