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
            options: [
                { id: 1, label: 'ALREADY USE' },
                { id: 2, label: 'ACCEPTED' },
            ],
            selectedOptions: [],
        }

    }


    handleVideoClick = (url) => {
        this.setState({ selectedVisionUrl: url });
    };

    showFilter = () => {
        this.setState({ showModal: !this.state.showModal });
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

    handleCheckboxChange = (option) => {
        const { selectedOptions } = this.state;
        const isSelected = selectedOptions.some((selected) => selected.id === option.id);
        const updatedOptions = isSelected
            ? selectedOptions.filter((selected) => selected.id !== option.id)
            : [...selectedOptions, option];

        this.setState({
            selectedOptions: updatedOptions,
        }, ()=>{console.log(selectedOptions)});
        this.setState({showCriminal:false})
    };

    updateState = (newValue) => {
        this.setState({ idGroup: newValue });
        console.log(this.state.idGroup)
    };

    render() {
        const { selectedVisionUrl, showModal } = this.state;
        const { options, selectedOptions } = this.state
        return ( <div>
                <header className="header-pr">
                    {!showModal ? <button className="acc-vis" onClick={this.showFilter}>Filter ↓</button> :
                        <button className="acc-vis" onClick={this.showFilter}>Filter ↑</button>}
                    {showModal &&
                        <div className="multi-select-menu">
                            <ul>
                                {options.map((option) => (
                                    <li key={option.id}>
                                        <label>
                                            <input
                                                type="checkbox"
                                                checked={selectedOptions.some((selected) => selected.id === option.id)}
                                                onChange={() => this.handleCheckboxChange(option)}
                                            />
                                            {option.label === "NOT_CAUGHT" ? 'NOT CAUGHT':option.label}
                                        </label>
                                    </li>
                                ))}
                            </ul>
                        </div>}
                </header>
                <h1 className="car-text">Visions</h1>
                <table className="bg-rg">
                    <tbody>
                    <td className="table-label-pr">
                        <TableVision updateUrl={this.handleVideoClick} updateId={this.updateState} selectedOptions={selectedOptions}/>
                    </td>
                    <td className="table-label-pr">
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
                    </td>

                    </tbody>
                </table>
            </div>


        )
    }
}

export default VisionList