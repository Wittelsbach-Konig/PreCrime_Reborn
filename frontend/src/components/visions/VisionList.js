import React from "react";
import Modal from 'react-modal';
import TableVision from "./TableVision";
import RegMans from "../boss_group/newMan";
import AddVision from "./AddVision";
class VisionList extends React.Component {
    constructor(props) {

        super(props);

        this.state = {
            showModal: false,
            showModal_1: false,
            showModal_2: false,
            showWorking:false,
            idGroup:0,
            manData: null,
            prevId:0,
            isModalOpen: false, // состояние модального окна
            selectedVisionUrl: null, // URL выбранного видео
        }

    }


    handleVideoClick = (url) => {
        this.setState({ selectedVisionUrl: url });
    };

    openModal = () => {
        this.setState({ showModal: true });
    };

    componentDidUpdate(prevProps, prevState) {
        const {prevId, idGroup} = this.state
        if (prevId !== idGroup) {
            this.updateStateId();
        }
    }


    updateStateId = () => {
        this.setState({ prevId: this.state.idGroup})
        console.log(this.state.prevId)
    };


    del = async () => {
        const token = localStorage.getItem('jwtToken');
        const url = `http://localhost:8028/api/v1/visions/${this.state.idGroup}`;
        const confirmation = window.confirm(`Are you sure delete this vision?`);

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
                        throw new Error(response.status);
                    }
                    console.log('Данные успешно удалены');
                    this.props.onRenew()
                })
                .catch(error => {
                    console.log(error.message)
                    if (error.message === "500") {
                        window.alert("This vision is already in use");
                    } else {
                        window.alert("You haven't chosen a vision");
                    }
                });
            this.props.onRenew()
        }
    }

    acceptVision = async () => {
        const token = localStorage.getItem('jwtToken');
        (this.state.idGroup !== 0) ? (
        fetch(`http://localhost:8028/api/v1/visions/${this.state.idGroup}/accept`, {
            method: 'POST', // или другой метод
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
            },
            body: JSON.stringify({
            }),
        })
            .then(response => response.json())
            .then(data => {
                this.setState({vision: data}, ()=>{this.props.onRenew()});

            })
            .catch(error => {

            })) : window.alert("you don't choosing vision")


    };

    updateState = (newValue) => {
        this.setState({ idGroup: newValue });
        console.log(this.state.idGroup)
    };

    updateWrk = () => {
        this.setState({ showWorking: true });
    };

    render() {
        const { manData, idGroup, prevId, isModalOpen, selectedVisionUrl } = this.state;
        const {visions, onRenew} = this.props
        return ( <div>
                <TableVision visionList={visions} updateUrl={this.handleVideoClick} updateId={this.updateState}/>
                <header className="header-pr">
                <button className="acc-vis" onClick={this.acceptVision}>Accept Vision</button>
                <button className="del-vis" onClick={this.del}>Delete Vision</button>
                </header>
                <h1 className="car-text">Visions</h1>
                {selectedVisionUrl && (
                <div className="video-container">
                    <iframe
                        title="Vision Video"
                        width="900"
                        height="800"
                        src={selectedVisionUrl}
                        frameBorder="0"
                        allowFullScreen
                    ></iframe>
                </div>
                )

                }
            </div>

        )
    }
}

export default VisionList