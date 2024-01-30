import React, { Component } from 'react';
import "../../css/Profile.css"
import fuel from "../../img/fuel.png"
import retire from "../../img/retire-tr.png"
import close from "../../img/close.png"
import rehab from "../../img/rehab.png"
import Refuel from "./Refuel";
import RegTransport from "./RegTransport";
class TableTransport extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
            showModal_2: false,
            showModal: false,
            refuel: false,
            carList: null,

        };
    }

    openModal = () => {
        this.setState({ showModal: true });
    };

    closeModal = () => {
        this.setState({showModal: false});
        this.props.renew();
    };

    openModal_2 = () => {
        this.setState({ showModal_2: true });
        this.setState({ refuel: true });
    };

    closeModal_2 = () => {
        this.setState({ showModal_2: false});
        this.setState({ refuel: false });
        this.props.renew();
    };

    componentDidMount() {
        this.fetchTransport()
    }

    fetchTransport = () => {
        const token = localStorage.getItem('jwtToken');

        fetch('api/v1/reactiongroup/transport', {
            method: 'GET', // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(responses => responses.json())
            .then(data => {
                this.setState({carList:data})
                console.log(this.state.carList)
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
                this.setState({carList:null})
            });
    }

    del = () => {
        const token = localStorage.getItem('jwtToken');
        const url = `api/v1/reactiongroup/transport/${this.state.selectedRow}`;
        const confirmation = window.confirm(`Are you sure delete this transport?`);

        if (confirmation) {
            fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
            })
                .then(response => {
                    if (!response.ok) {
                        // Если статус ответа не 2xx (успех), бросаем ошибку
                        throw new Error(response.status);
                    }
                    this.fetchTransport()
                })
                .catch(error => {
                    if (error.message ==="404")
                        window.alert("You don't choose position");
                })};

    };

    retire = () => {
        const token = localStorage.getItem('jwtToken');
        const putData = {
            id: this.state.idTransport,
        };
        const confirmation = window.confirm(`Are you sure retire this transport?`);

        if (confirmation) {
             fetch(`api/v1/reactiongroup/transport/${this.state.selectedRow}/retire`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(putData),
            })
                .then(response => {
                    if (!response.ok) {
                        // Если статус ответа не 2xx (успех), бросаем ошибку
                        throw new Error(response.status);
                    }
                    this.fetchTransport()
                })
                .catch(error => {
                    if (error.message ==="404")
                        window.alert("You don't choose position");
                })};
        this.fetchTransport()
    };

    rehab = () => {
        const token = localStorage.getItem('jwtToken');
        const putData = {
            id: this.state.idTransport,
        };
        const confirmation = window.confirm(`Are you sure rehabilitate this transport?`);

        if (confirmation) {
             fetch(`api/v1/reactiongroup/transport/${this.state.selectedRow}/rehabilitate`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(putData),
            })
                .then(response => {
                    if (!response.ok) {
                        // Если статус ответа не 2xx (успех), бросаем ошибку
                        throw new Error(response.status);
                    }
                    this.fetchTransport()
                })
                .catch(error => {
                    if (error.message ==="404")
                        window.alert("You don't choose position");
                })};
        this.fetchTransport()
    };

    handleAction = (carId, actionFunction) => {
        if (carId !== this.state.selectedRow) {
            this.setState({ selectedRow: carId }, () => {
                console.log(this.state.selectedRow);
                actionFunction(carId);
            });
        } else {
            actionFunction(carId);
        }
    }

    render() {
        const { selectedRow, carList } = this.state;
        return (<div>
            <header className="header-pr">
                <button className="acc-vis" onClick={this.openModal}>Register new transport</button>
            </header>
                <h1 className="car-text">Transport List</h1>
            <div className="content-container-supply">
                <div className="table-container-supply">
            <table className="bg-rg">
                <thead>
                <tr>
                    <th className="table-label-pr">Brand</th>
                    <th className="table-label-pr">Model</th>
                    <th className="table-label-pr">Remaining Fuel</th>
                    <th className="table-label-pr">Maximum Fuel</th>
                    <th className="table-label-pr">Condition</th>
                    <th className="table-label-pr">In Operation</th>
                    <th className="table-label-pr"></th>
                </tr>
                </thead>
                <tbody>

                {carList ? (carList.map((car) => (
                            <tr
                                key={car.id}
                                className={car.id === selectedRow ? 'selected-row' : ''}
                                onClick={() => {
                                    console.log(car.id);
                                    this.setState({ selectedRow: car.id }, () => {
                                        console.log(this.state.selectedRow);
                                    });
                                }}
                            >
                        <td className="table-label-pr">{car.brand}</td>
                        <td className="table-label-pr">{car.model}</td>
                        <td className="table-label-pr">{car.remaining_fuel}</td>
                        <td className="table-label-pr">{car.maximum_fuel}</td>
                        <td className="table-label-pr">{car.condition}</td>
                        <td className="table-label-pr">{car.inOperation ? 'Yes' : 'No'}</td>
                        <td className="table-label-pr">
                            {car.inOperation ?
                            <td className="table-label-pr">
                                <button className="fuel-but" onClick={() => this.handleAction(car.id, this.retire)}>
                                    <img src={retire} className="fuel" alt="Кнопка «button»"/>
                                </button>
                                <div className="tooltip" id="tooltip">Retire</div>
                            </td>
                            :
                            <td className="table-label-pr">
                                <button className="fuel-but" onClick={() => this.handleAction(car.id, this.rehab)}>
                                    <img src={rehab} className="fuel" alt="Кнопка «button»"/>
                                </button>
                                <div className="tooltip" id="tooltip">Rehabilitate</div>
                            </td>}
                            <td className="table-label-pr">
                                <button className="fuel-but" onClick={this.openModal_2}>
                                    <img src={fuel} className="fuel" alt="Кнопка «button»"/>
                                </button>
                                <div className="tooltip" id="tooltip">Refuel</div>
                            </td>
                            <td className="table-label-pr">
                                <button className="fuel-but" onClick={() => this.handleAction(car.id, this.del)}>
                                    <img src={close} className="fuel" alt="Кнопка «button»"/>
                                </button>
                                    <div className="tooltip" id="tooltip">Delete</div>
                            </td>
                        </td>
                    </tr>
                ))
                    )
                    :
                    (
                        <tr>
                            <td colSpan="7" className="table-label-pr">No transport data available</td>
                        </tr>
                    )
                }
                </tbody>
            </table>
            </div>
                {this.state.showModal_2 && <Refuel reff={this.state.refuel} amm={false} onClose={this.closeModal_2} idTr={this.state.selectedRow} onRenew={this.fetchTransport}/>}
                {this.state.showModal && <RegTransport onClose={this.closeModal} onRenew={this.fetchTransport} />}
            </div>
            </div>
        );
    }
}

export default TableTransport;
