import React from "react";
class profile extends React.Component {
    constructor(props) {

        super(props);
        this.state = {
            me:null,
        }


    }


    render() {
        const {me} = this.props
        const firstName = me ? me.firstName : "N/A";
        const lastName = me ? me.lastName : "N/A";
        const email = me ? me.email : "N/A";
        return ( <div>
                <div className="frame">
                    <div className="rectangle-prof" />
                <div className="text-block">
                <div className="allsides-7">
                    Name: {firstName} {lastName}
                </div>
                    <div className="br-class"></div>
                    <div className="allsides-7">
                        email: {email}
                    </div>
                </div>


                    <div className="image">
                        <img className="rectangle" alt="Rectangle" src="https://www.pinclipart.com/picdir/big/168-1685829_task-complete-icon-outline-complete-icon-png-clipart.png"/>

                    </div>
                </div>
            </div>
        )
    }
}

export default profile