import React from "react";
import AddUser from "./AddUser";
import {RiCloseCircleLine, RiEditBoxLine} from "react-icons/ri"
class User extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            editForm: false
        }
    }
    user = this.props.user
    render() {
        return (
            <div className="user" >
                <RiCloseCircleLine onClick={() => this.props.onDelete(this.user.id)} className="delete-icon"/>
                <RiEditBoxLine onClick={()=> {this.setState({
                    editForm: !this.state.editForm
                })
                }}
                               className="edit-icon"/>
                <h3>{this.user.first_name} {this.user.last_name}</h3>
                <img src={this.user.avatar}/>
                <p>{this.user.email}</p>
                <b>{this.user.isHappy ? 'Happy :)' : 'Another one :('}</b>
                {this.state.editForm && <AddUser onAdd={this.props.onEdit} user={this.user}/>}
            </div>
        )
    }
}

export default User