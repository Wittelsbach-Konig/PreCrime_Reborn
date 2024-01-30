import React, { Component } from 'react';
import accept from "../../img/accept.png";
import close from "../../img/close.png";

class TableVision extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
            selectedVideoUrl: null,
            visionList: null,
        };
    }


    handleRowClick = (index, url) => {
        this.setState({ selectedRow: index }, ()=>{
            this.props.updateUrl(url)
            this.props.updateId(index)
            console.log(url)
        });

    };



    componentDidMount() {
        this.fetchVision()
    }

    fetchVision = () => {
        const token = localStorage.getItem('jwtToken');

        const handleApiResponse = async (response, property) => {
            if (response.ok) {
                try {
                    return await response.json();
                } catch (error) {
                    console.error('Error parsing JSON:', error);
                    return null;
                }
            } else {
                console.error(`Error fetching data: ${response.status}`);
                return null;
            }
        };

        Promise.all([
            fetch('api/v1/visions', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
            }).then(response => handleApiResponse(response, 'visionList')),

            fetch('api/v1/visions/used', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
            }).then(response => handleApiResponse(response, 'visionList')),
        ]).then(dataArray => {
            const mergedData = dataArray.reduce((acc, data) => (data ? [...acc, ...data] : acc), []);
            this.setState({ visionList: mergedData });
        }).catch(error => {
            console.error('Error fetching data:', error);
            this.setState({ visionList: null });
        });
    }

    del = () => {
        const token = localStorage.getItem('jwtToken');
        const url = `api/v1/visions/${this.state.selectedRow}`;
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
                    this.fetchVision()
                })
                .catch(error => {
                    console.log(error.message)
                    if (error.message === "500") {
                        window.alert("This vision is already in use");
                    } else {
                        window.alert("You haven't chosen a vision");
                    }
                });
            this.fetchVision()
        }
    }

    acceptVision = () => {
        const token = localStorage.getItem('jwtToken');
            fetch(`api/v1/visions/${this.state.selectedRow}/accept`, {
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
                    this.setState({vision: data}, ()=>{this.fetchVision()});

                })
                .catch(error => {
                    this.fetchVision()
                })
            this.fetchVision()

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
        const { selectedRow, visionList } = this.state;
        const { selectedOptions } = this.props
        return (
            <div className='content-container-vision'>
                <div className="table-container-vision">
                    <table className="bg-rg">
                        <thead>
                        <tr>
                            <th className="table-label-pr">Video</th>
                            <th className="table-label-pr">Accepted</th>
                            <th className="table-label-pr">Already Used</th>
                            <th className="table-label-pr"></th>
                        </tr>
                        </thead>
                        <tbody>
                        {visionList ? (
                            visionList.map((vision) => (
                                ((selectedOptions.length===0 &&
                                        !vision.accepted  &&
                                        !vision.alreadyInUse)
                                ||
                                (selectedOptions.some((option) => option.label.toLowerCase() === 'accepted') &&
                                        vision.accepted  &&
                                        !vision.alreadyInUse &&
                                        selectedOptions.length === 1)
                                ||
                                (selectedOptions.length===2 &&
                                    vision.accepted=== true  &&
                                    vision.alreadyInUse === true
                                ))
                                &&
                                <tr
                                    key={vision.id}
                                    className={vision.id === selectedRow ? 'selected-row' : ''}
                                    onClick={() => this.handleRowClick(vision.id, vision.videoUrl)}
                                >
                                    <td className="table-label-pr">
                                        Vision {vision.id}
                                    </td>
                                    <td className="table-label-pr">{vision.accepted ? 'Yes' : 'No'}</td>
                                    <td className="table-label-pr">{vision.alreadyInUse ? 'Yes': 'No'}</td>
                                    <td className="table-label-pr">
                                        <td className="table-label-pr">
                                            <button className="fuel-but" onClick={()=>{this.handleAction(vision.id,this.acceptVision)}}>
                                                <img src={accept} className="fuel" alt="Кнопка «button»"/>
                                            </button>
                                            <div className="tooltip" id="tooltip">Accept</div>
                                        </td>
                                        <td className="table-label-pr">
                                            <button className="fuel-but" onClick={()=>{this.handleAction(vision.id,this.del)}}>
                                                <img src={close} className="fuel" alt="Кнопка «button»"/>
                                            </button>
                                            <div className="tooltip" id="tooltip">Delete</div>
                                        </td>
                                    </td>
                                </tr>
                            )))
                            :(
                            <tr>
                                <td className="table-label" colSpan="4">No vision data available</td>
                            </tr>)
                        }
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}


export default TableVision;
