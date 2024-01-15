import React, { Component } from 'react';
import FormData from "form-data";
class SupplyTable extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
            data:this.props.supplyList,
            type:'Type',
            amounts: Array.from({ length: this.props.supplyList.length }, () => ''),
            confirmationVisible: false,
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

            const baseUrl = 'api/v1/reactiongroup/supply/filter';

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

    handleInputChange = (e, index) => {
        const { value } = e.target;
        console.log(value)
        this.setState((prevState) => {
            const newAmounts = [...prevState.amounts];
            newAmounts[index] = value;
            return { amounts: newAmounts };
        });
    };

    handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            // При нажатии Enter показываем окно с подтверждением
            this.setState({ confirmationVisible: true });
        }

        if (e.key === '+' || e.key === '-' || e.key === '.' || e.key === ',') {
            e.preventDefault();
        }

    };

    handleConfirmation = () => {
        // Здесь вы можете обработать подтверждение
        console.log('Confirmed:', this.state.amounts);
        console.log(this.state.selectedRow)
        // Скрываем окно с подтверждением и сбрасываем значение amount
        const firstNumber = this.state.amounts.find((element) => {
            const parsedNumber = parseInt(element, 10);
            return !isNaN(parsedNumber);
        });
        console.log(firstNumber)
        this.handleSubmit(firstNumber)
        this.setState({ confirmationVisible: false, amounts: Array.from({ length: this.props.supplyList.length }, () => '') });
    };

    handleCancel = () => {
        // При отмене скрываем окно с подтверждением
        this.setState({ confirmationVisible: false, amounts: Array.from({ length: this.props.supplyList.length }, () => '') });
    };

    handleBlur = () => {
        // Обработка потери фокуса (Blur)
        // Открытие окна
        this.setState({ confirmationVisible: true });
    };


    handleSubmit = (amount) => {
        const token = localStorage.getItem('jwtToken');
        const FormData = require('form-data');
        const form = new FormData();
        form.append('amount', amount);
            fetch(`api/v1/reactiongroup/supply/${this.state.selectedRow}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                body: form,
            })
                .then(response => {
                    if (!response.ok) {
                        // Если статус ответа не 2xx (успех), бросаем ошибку
                        throw new Error(response.status);
                        this.props.onRenew()
                    }
                    this.props.onRenew()
                })
                .catch(error => {
                    if (error.message ==="400")
                        window.alert("Purchases shouldn't be more then max possible amount");
                    if (error.message ==="404")
                        window.alert("You don't choose position");
                });
            this.props.onRenew()
    };

    render() {
        const { supplyList, selectOpt } = this.props;
        const { selectedRow, data, amounts, confirmationVisible } = this.state;
        return (

            <div className="content-container-supply">
                <div className="table-container-supply">
                    <table className="bg-rg">
                        <thead>
                        <tr>
                            <th className="table-label">Name</th>
                            <th className="table-label">Amount</th>
                            <th className="table-label">Max Amount</th>
                            <th className="table-label">Type</th>
                            <th className="table-label">Purchase</th>
                        </tr>
                        </thead>
                        <tbody>

                        {supplyList ? (supplyList.map((supply,supIndex) => (
                                (selectOpt.length===0 ||
                                (selectOpt.some((option) => option.label.toLowerCase() === supply.type.toLowerCase()))) &&
                                <tr
                                    key={supply.id}
                                    className={supply.id === selectedRow ? 'selected-row' : ''}
                                    onClick={() => this.handleRowClick(supply.id)}
                                >
                                    <td className="table-label-pr">{supply.resourceName}</td>
                                    <td className="table-label-pr">{supply.amount}</td>
                                    <td className="table-label-pr">{supply.maxPossibleAmount}</td>
                                    <td className="table-label-pr">{supply.type}</td>
                                        <td className="table-label-edit">
                                            <input
                                                type="number"
                                                inputMode="numeric"
                                                name={`amounts-${supIndex}`}
                                                value={amounts[supIndex]}
                                                onChange={(e) => this.handleInputChange(e, supIndex)}
                                                onKeyDown={this.handleKeyDown}
                                                onBlur={this.handleBlur}
                                            />
                                        </td>
                                </tr>

                        ))):
                            (
                            <tr>
                                <td colSpan="5" className="table-label-pr">No ammunition data available</td>
                            </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div>
                {confirmationVisible && (
                    <div className="modal">
                        <div className="modal-content-precog">
                        <h2 className="h-style">Are you sure you want to confirm the entered value?</h2>
                            <form className="form-tr">
                                <table className="bg-rg">
                                    <tbody>
                                    <tr>
                                        <td className="table-label-edit">
                                        <button type="button" onClick={this.handleConfirmation}>Yes</button>
                                        </td>
                                        <td className="table-label-edit">
                                        <button type="button" onClick={this.handleCancel}>No</button>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form>
                    </div>
                    </div>
                )}
            </div>
        );
    }
}

export default SupplyTable;
