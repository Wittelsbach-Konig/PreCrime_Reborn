import React, { Component } from 'react';
import '../css/BurgerMenu.css';

class BurgerMenu extends Component {
    constructor(props) {
        super(props);
        this.state = {
            menuOpen: false,
            pullRole: {"DETECTIVE": false,
            "TECHNIC": false,
            "AUDITOR": false,
            "REACTIONGROUP":false}

        };
    }

    toggleMenu = () => {
        this.setState((prevState) => ({ menuOpen: !prevState.menuOpen }));
    };

    changeRole = (role) =>{
        const {pullRole} = this.state
        const rolesToDisplay = ["DETECTIVE", "TECHNIC", "AUDITOR", "REACTIONGROUP"];
        rolesToDisplay.map((lrole) => {
            if (lrole === role) {
                pullRole[lrole] = true
                localStorage.setItem('curRole',lrole)
            }
            else{
                pullRole[lrole] = false
            }
        }
        )

        console.log(pullRole)
        this.props.updatePull(pullRole)

    };

    render() {
        const { menuOpen } = this.state;
        const { roles } = this.props;
        return (
            <div>
                <button
                    onClick={this.toggleMenu}
                    className={`burger-button ${menuOpen ? 'open' : ''}`}
                >
                    &#9776;
                </button>

                <div className={`burger-menu ${menuOpen ? 'open' : ''}`}>
                    <p className="role-p">Roles</p>
                    <ul>
                        {roles.map(role => (
                            <li key={role} onClick={() => this.changeRole(role)}>{role}</li>
                        ))}
                    </ul>
                </div>
            </div>
        );
    }
}

export default BurgerMenu;
