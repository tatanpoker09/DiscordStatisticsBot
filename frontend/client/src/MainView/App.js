import React, {useCallback, useEffect, useState} from 'react';
import './App.css';

// or less ideally
import {Button, Form, FormControl, Nav, Navbar, NavDropdown} from 'react-bootstrap';

import ChannelList from "./ChannelList";
import DiscordStats from "./DiscordStats";
import ProfileView from "../ProfileView/ProfileView";
import LeaderboardsView from "../Leaderboards/LeaderboardsView";

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            currentView: "MainView",
            userData: {}
        };
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.searchUser = this.searchUser.bind(this);
        this.handleMainButton = this.handleMainButton.bind(this);
        this.handleLeaderboards = this.handleLeaderboards.bind(this);
    }

    searchUser(event) {
        event.preventDefault();
        if(this.state.username!=="") {
            fetch(`http://localhost:3001/users/${this.state.username}`)
                .then((res)=> {
                    if(res.status===200) {
                        return res.json()
                    } else {
                        //Not found :(
                        throw "Username Not Found";
                    }
                })
                .then(res => {
                    //TODO CALL A FUNCTION TO CHANGE EVERYTHING.
                    this.setState({userData: res});
                    this.setState({currentView: "ProfileView"});
                }).catch(er =>{
                    alert("We couldn't find a user by that name");
                });
        }
    }

    handleLeaderboards(){
        this.setState({currentView: "LeaderboardsView"});
    }

    handleMainButton(){
        this.setState({currentView: "MainView"});
    }

    handleUsernameChange(event) {
        this.setState({username: event.target.value});
    }

    render() {
        let renderMainView;
        switch(this.state.currentView){
            case "MainView":
                renderMainView = <DiscordStats/>;
                break;
            case "ProfileView":
                renderMainView = <ProfileView key={this.state.userData && this.state.userData.username} userData={this.state.userData}/>;
                break;
            case "LeaderboardsView":
                console.log("HERE");
                renderMainView = <LeaderboardsView/>;
                break;
        }
        return (
            <div className="App">
                <header className="App-header">
                    <Navbar bg="light" expand="lg">
                        <Navbar.Brand href="#home">TatanStatisticsBot</Navbar.Brand>
                        <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                        <Navbar.Collapse id="basic-navbar-nav">
                            <Nav className="mr-auto">
                                <Nav.Link href="" onClick={this.handleMainButton}>Home</Nav.Link>
                                <Nav.Link href="" onClick={this.handleLeaderboards}>Leaderboards</Nav.Link>
                                <NavDropdown title="Dropdown" id="basic-nav-dropdown">
                                    <NavDropdown.Item href="#action/3.1">Action</NavDropdown.Item>
                                    <NavDropdown.Item href="#action/3.2">Another action</NavDropdown.Item>
                                    <NavDropdown.Item href="#action/3.3">Something</NavDropdown.Item>
                                    <NavDropdown.Divider/>
                                    <NavDropdown.Item href="#action/3.4">Separated link</NavDropdown.Item>
                                </NavDropdown>
                            </Nav>
                            <Form inline onSubmit={this.searchUser}>
                                <FormControl type="text" placeholder="Usuario..." className="mr-sm-2"
                                             value={this.state.username} onChange={this.handleUsernameChange}
                                />
                                <Button variant="outline-success" type={"submit"}>Search</Button>
                            </Form>
                        </Navbar.Collapse>
                    </Navbar>
                </header>
                {renderMainView}


            </div>
        );
    }
}

export default App;
