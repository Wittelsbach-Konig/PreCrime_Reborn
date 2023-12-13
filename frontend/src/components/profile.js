import React from "react";
import "../css/Profile.css"
class Profile extends React.Component {
    constructor(props) {

        super(props);
        this.state = {
        }


    }


    render() {
        const {me} = this.props
        const firstName = me ? me.firstName : "N/A";
        const lastName = me ? me.lastName : "N/A";
        const email = me ? me.email : "N/A";
        return (
            <div className="body-prof">
            <div className="wrapper">
                <div className="img-area">
                    <div className="inner-area">
                    </div>
                </div>
                <div className="icon arrow"><i className="fas fa-arrow-left"></i></div>
                <div className="icon dots"><i className="fas fa-ellipsis-v"></i></div>
                <div className="name">{firstName} {lastName}</div>
                <div className="about">{email}</div>
                <div className="buttons">
                </div>

            </div>
            </div>
        )
    }
}

export default Profile