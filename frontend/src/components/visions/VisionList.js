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

    openVision = () => {
        this.setState({ isModalOpen: true });
    };

    closeVision = () => {
        this.setState({ isModalOpen: false });
    };

    handleVideoClick = (url) => {
        this.setState({ selectedVisionUrl: url });
    };

    openModal = () => {
        this.setState({ showModal: true });
    };

    closeModal = () => {
        this.setState({showModal: false});
    };

    openModal_1 = () => {
        this.setState({ showModal_1: true });
    };

    closeModal_1 = () => {
        this.setState({showModal_1: false});
        this.props.renew();
    };

    openModal_2 = () => {
        this.setState({ showModal_2: true });
        console.log(this.props.visions)
    };

    closeModal_2 = () => {
        this.setState({showModal_2: false});
        this.props.renew();
    };

    showAll = () => {
        this.setState({ showWorking: !this.state.showWorking }, ()=>
        {
            this.props.onChange(!this.state.showWorking)
            // this.props.renew()
        });

    };

    componentDidUpdate(prevProps, prevState) {
        const {prevId, idGroup} = this.state
        if (prevId !== idGroup) {
            this.updateStateId();
        }
    }


    updateStateId = () => {
        this.setState({ prevId: this.state.idGroup }, ()=>
        {
            const token = localStorage.getItem('jwtToken');


            fetch(`http://localhost:8028/api/v1/reactiongroup/${this.state.idGroup}/statistic`, {
                method: 'GET', // или другой метод
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`, // Добавляем токен в заголовок
                },
            })
                .then(responses => responses.json())
                .then(data => {
                    this.setState({manData:data})
                })
                .catch(error => {
                    console.error('Ошибка при запросе к серверу:', error);
                });
        });
        console.log(this.state.prevId)
    };


    del = async () => {
        const token = localStorage.getItem('jwtToken');
        const url = `http://localhost:8028/api/v1/visions/${this.state.idGroup}`;

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
                this.props.onRenew()
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });
    }

    acceptVision = () => {
        const token = localStorage.getItem('jwtToken');
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
                this.setState({vision: data});
                this.props.onRenew()
            })
            .catch(error => {
                console.error('Ошибка при запросе к серверу:', error);
            });

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
                <h1 className="car-text">Visions</h1>
                <TableVision visionList={visions} updateUrl={this.handleVideoClick} updateId={this.updateState}/>
                <button className="acc-vis" onClick={this.acceptVision}>Accept Vision</button>
                <button className="del-vis" onClick={this.del}>Delete Vision</button>

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