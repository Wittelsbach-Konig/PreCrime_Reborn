import React from "react";
import SupplyTable from "./SupplyTable";
import NewAmmunition from "./NewAmmunition";
import Slider from 'rc-slider';
import 'rc-slider/assets/index.css';

class Ammunition extends React.Component {
    constructor(props) {

        super(props);

        this.state = {

            showReactionGroup: false,
            showTransport: false,
            showAmmunition: false,
            showCriminal: false,
            showModal: false,
            showModal_2: false,
            amountAmm: false,
            idAmm:0,
            isOpen: false,
            options: [
                { id: 1, label: 'AMMUNITION' },
                { id: 2, label: 'WEAPON' },
                { id: 3, label: 'GADGET' },
                { id: 4, label: 'FUEL' },
            ],
            selectedOptions: [],
            numericValue: [0, 10000], // Диапазон числовых данных
        }

    }


    updateState = (newValue) => {
        this.setState({ idAmm: newValue });
        console.log(this.state.idTransport)
    };

    openModal = () => {
        this.setState({ showModal: true });
    };

    closeModal = () => {
        this.setState({showModal: false});
        this.props.renew();
    };


    handleCheckboxChange = (option) => {
        const { selectedOptions } = this.state;

        // Проверяем, была ли опция выбрана или нет
        const isSelected = selectedOptions.some((selected) => selected.id === option.id);

        // Если опция была выбрана, убираем её из массива выбранных
        // В противном случае, добавляем в массив выбранных
        const updatedOptions = isSelected
            ? selectedOptions.filter((selected) => selected.id !== option.id)
            : [...selectedOptions, option];

        this.setState({
            selectedOptions: updatedOptions,
        }, ()=>{console.log(selectedOptions)});

    };

    filterOpen = () => {
        this.setState({isOpen: !this.state.isOpen})
    }

    handleNumericChange = (value) => {
        this.setState({
            numericValue: value,
        });
        // Здесь вы можете выполнить дополнительные действия при изменении числового значения
    };


    render() {
        const {renew, ammun} = this.props
        const {isOpen, options, selectedOptions} = this.state
        return ( <div>

                <header className="header-pr">
                    <button className="acc-vis" onClick={this.openModal}>Add new resource</button>

                    {!isOpen ? <button className="acc-vis" onClick={this.filterOpen}>Filter ↓</button> :
                        <button className="acc-vis" onClick={this.filterOpen}>Filter ↑</button>}
                    {isOpen &&
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
                                        {option.label}
                                    </label>
                                </li>
                            ))}
                        </ul>
                    </div>}
                </header>
                <h1 className="car-text">Ammunition List</h1>
            {!this.state.showModal && <SupplyTable supplyList={ammun}
                                                   idTr={this.updateState}
                                                   onRenew={renew}
                                                   selectOpt={selectedOptions}/>}

                {this.state.showModal && <NewAmmunition onClose={this.closeModal} renew={renew}/>}
                </div>
        )
    }
}

export default Ammunition