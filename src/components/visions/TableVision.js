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
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Video</th>
                            <th>Accepted</th>
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
                                    <td>{vision.id}</td>
                                    <td>
                                        Vision {vision.id}
                                    </td>
                                    <td>{vision.accepted ? 'Yes' : 'No'}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="3">No vision data available</td>
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
