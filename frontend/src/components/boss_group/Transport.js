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
        const confirmation = window.confirm(`Are you sure retire this transport?`);

        if (confirmation) {
        await fetch(`http://localhost:8028/api/v1/reactiongroup/transport/${this.state.idTransport}/retire`, {
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
                this.props.onRenew();
            })
            .catch(error => {
                if (error.message ==="404")
                    window.alert("You don't choose position");
            })};
        this.props.renew()
    };

    rehab = async () => {
        const token = localStorage.getItem('jwtToken');
        const putData = {
            id: this.state.idTransport,
        };
        const confirmation = window.confirm(`Are you sure rehabilitate this transport?`);

        if (confirmation) {
        await fetch(`http://localhost:8028/api/v1/reactiongroup/transport/${this.state.idTransport}/rehabilitate`, {
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
                this.props.renew();
            })
            .catch(error => {
                if (error.message ==="404")
                    window.alert("You don't choose position");
            })};
        this.props.renew()
    };

    del = async () => {
        const token = localStorage.getItem('jwtToken');
        const url = `http://localhost:8028/api/v1/reactiongroup/transport/${this.state.idTransport}`;
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
                this.props.renew();
            })
            .catch(error => {
                if (error.message ==="404")
                    window.alert("You don't choose position");
            })};

    };

    render() {
        return ( <div>

                <header className="header-pr">
                    <button className="acc-vis" onClick={this.openModal}>Register new transport</button>
                    <button className="del-vis" onClick={this.retire} >Retire</button>
                    <button className="acc-vis" onClick={this.rehab}>Rehabilitate</button>
                    <button className="acc-vis" onClick={this.openModal_2}>Refuel</button>
                    <button className="acc-vis" onClick={this.del}>Delete transport</button>
                </header>
                <h1 className="car-text">Transport List</h1>
                <TableTransport carList={this.props.trans} idTr={this.updateState}/>
                {this.state.showModal_2 && <Refuel reff={this.state.refuel} amm={false} onClose={this.closeModal_2} idTr={this.state.idTransport} onRenew={this.props.renew}/>}
                {this.state.showModal && <RegTransport onClose={this.closeModal} onRenew={this.props.renew} />}
            </div>
        )
    }
}

export default Transport