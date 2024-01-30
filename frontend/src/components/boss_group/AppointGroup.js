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

        const grr = selectedRows.length !== 0 ? (selectedRows.length === 1 ? [...selectedRows] : selectedRows) : [];
        console.log(grr)
        if (selectedRows.length !== 0) {
            fetch(`api/v1/reactiongroup/criminal/${idCr}`, {
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
                    this.props.onClose();
                })
                .catch(error => {
                    console.error('Ошибка при запросе к серверу:', error);
                });

            this.props.onClose();
        }
        else
        {
            window.alert("choose men")
        }
        this.props.onClose();
    };


    render() {
        const { groupList, onClose } = this.props;
        const { selectedRows } = this.state;

        return (<div className="modal">
                    <div className="modal-content-appoint">
                        <span className="close" onClick={onClose}>&times;</span>
                        <form className="form-content">
                                <table className="bg-rg">
                                    <thead>
                                    <tr>
                                        <th className="table-label">ID</th>
                                        <th className="table-label">Member Name</th>
                                        <th className="table-label">Telegram ID</th>
                                        <th className="table-label">in Operation</th>
                                    </tr>
                                    </thead>
                                    <tbody>

                                    {groupList ? (groupList.map((group) => (
                                            group.inOperation &&
                                                    <tr
                                                        key={group.id}
                                                        className={this.state.selectedRows.includes(group.id) ? 'selected-row' : ''}
                                                        onClick={() => this.handleRowsClick(group.id)}
                                                    >
                                                        <td className="table-label-edit">{group.id}</td>
                                                        <td className="table-label-edit">{group.memberName}</td>
                                                        <td className="table-label-edit">{group.telegramId}</td>
                                                        <td className="table-label-edit">{group.inOperation ? 'Yes' : 'No'}</td>
                                                    </tr>

                                    )))
                                    : (
                                            <tr>
                                                <td className="table-label-edit"
                                                    colSpan="4">No group data available</td>
                                            </tr>
                                        )}
                                    </tbody>
                                </table>
                            <br/>
                                <button type="button" className="button-tr" onClick={this.appointing}>Appoint Group</button>
                        </form>
                        </div>
                    </div>
        );
    }
}

export default AppointGroup;
