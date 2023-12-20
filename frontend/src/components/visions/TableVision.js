import React, { Component } from 'react';

class TableVision extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRow: null,
            selectedVideoUrl: null,
        };
    }


    handleRowClick = (index, url) => {
        this.setState({ selectedRow: index }, ()=>{
            this.props.updateUrl(url)
            this.props.updateId(index)
            console.log(url)
        });

    };

    render() {
        const { visionList } = this.props;
        const { selectedRow, selectedVideoUrl } = this.state;

        return (
            <div className="content-container-vision">
                <div className="table-container-vision">
                    <table className="bg-rg">
                        <thead>
                        <tr>
                            <th className="table-label">Video</th>
                            <th className="table-label">Accepted</th>
                        </tr>
                        </thead>
                        <tbody>
                        {visionList ? (
                            visionList.map((vision) => (
                                <tr
                                    key={vision.id}
                                    className={vision.id === selectedRow ? 'selected-row' : ''}
                                    onClick={() => this.handleRowClick(vision.id, vision.videoUrl)}
                                >
                                    <td className="table-label">
                                        Vision {vision.id}
                                    </td>
                                    <td className="table-label">{vision.accepted ? 'Yes' : 'No'}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="2">No vision data available</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}


export default TableVision;
