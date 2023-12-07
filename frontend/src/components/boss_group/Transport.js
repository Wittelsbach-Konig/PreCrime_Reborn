import React from "react";
import TableTransport from "./TableTransport";
import RegTransport from "./RegTransport";
import Refuel from "./Refuel";
class Transport extends React.Component {
    constructor(props) {

        super(props);

        this.state = {
            showModal: false,
            showModal_2: false,
            refuel: false,
            idTransport:0,
        }

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

    updateState = (newValue) => {
        this.setState({ idTransport: newValue });
        console.log(this.state.idTransport)
    };

    retire = async () => {
        const token = localStorage.getItem('jwtToken');
        const putData = {
            id: this.state.idTransport,
        };

        await fetch(`http://localhost:8028/api/v1/reactiongroup/transport/${this.state.idTransport}/retire`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(putData),
        })
            .then(response => response.json())
            .then(updatedData => {
                console.log('Данные успешно обновлены:', updatedData);
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
        this.props.renew()
    };

    rehab = async () => {
        const token = localStorage.getItem('jwtToken');
        const putData = {
            id: this.state.idTransport,
        };

        await fetch(`http://localhost:8028/api/v1/reactiongroup/transport/${this.state.idTransport}/rehabilitate`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(putData),
        })
            .then(response => response.json())
            .then(updatedData => {
                console.log('Данные успешно обновлены:', updatedData);
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
        this.props.renew()
    };

    del = async () => {
        const token = localStorage.getItem('jwtToken');
        const url = `http://localhost:8028/api/v1/reactiongroup/transport/${this.state.idTransport}`;

        fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Ошибка сети или сервера');
                }
                console.log('Данные успешно удалены');
                this.props.renew()
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });

    };

    render() {
        return ( <div>
                <h1 className="car-text">Transport List</h1>
                <TableTransport carList={this.props.trans} idTr={this.updateState}/>

                <button className="new-transport" onClick={this.openModal}>Register new transport</button>
                {this.state.showModal && <RegTransport onClose={this.closeModal} onRenew={this.props.renew} />}
                <button className="ret-transport" onClick={this.retire} >Retire</button>
                <button className="reh-transport" onClick={this.rehab}>Rehabilitate</button>
                <button className="ref-transport" onClick={this.openModal_2}>Refuel</button>
                {this.state.showModal_2 && <Refuel reff={this.state.refuel} amm={false} onClose={this.closeModal_2} idTr={this.state.idTransport} onRenew={this.props.renew}/>}
                <button className="del-transport" onClick={this.del}>Delete transport</button>
            </div>
        )
    }
}

export default Transport