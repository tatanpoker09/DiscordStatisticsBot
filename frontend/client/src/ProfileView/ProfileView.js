import * as React from "react";
import UserDayStatistics from "./UserDayStatistics";
import DayStatistics from "../MainView/DayStatistics";

class ProfileView extends React.Component{
    constructor(props) {
        super(props);
        this.username = this.props.userData.username;
        this.state = {
            timeSinceJoin: "8 Months ago",
            messagesSentCount: "Loading Messages..."
        };
    }

    componentDidMount(){
        this.parseTimeSinceJoin(this.props.userData.date_since_join);
    }

    loadMessageCount(){
        fetch(`http://localhost:3001/users/${this.username}/messagecount`)
            .then(res => res.json())
            .then(res => {
                this.setState({messagesSentCount: res.count});
            })
            .catch(e => console.log(e));
    }

    parseTimeSinceJoin(date_since_join) { //Recieve time in milliseconds.
        let days_since_join = date_since_join/(60000*60*24);
        if(days_since_join > 30){
            const months_since_join = days_since_join/30;
            this.setState({timeSinceJoin: months_since_join.toFixed(0) +" Months ago."})
        } else {
            this.setState({timeSinceJoin: days_since_join.toFixed(0) +" Days ago."})
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        this.loadMessageCount();
    }

    render() {
        return(
            <div className={"center"}>
                <div>
                    <h1>{this.username}'s Profile</h1>
                    Member since: {this.state.timeSinceJoin}
                </div>
                <div>
                    <p>Messages Sent: {this.state.messagesSentCount}</p>
                </div>
                <div>
                    <DayStatistics username={this.username}/>
                </div>
            </div>
        );
    }
}

export default ProfileView;
