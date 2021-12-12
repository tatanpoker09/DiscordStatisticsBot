import * as React from "react";
import LeaderboardsUserContainerView from "./LeaderboardsUserContainerView";
import {Col, Container, Row} from "react-bootstrap";

class LeaderboardsView extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            leaderboardData: undefined,
        }
    }

    componentDidMount() {
        this.fetchLeaderboards();
    }

    fetchLeaderboards(){
        fetch("http://localhost:3001/messages/leaderboard")
            .then(res => res.json())
            .then(res => {
                this.setState({leaderboardData: res})
            });
    }

    //NECESITO UNA LISTA DE objetos, con userdata ordenados por cantidad de mensajes.
    render() {
        if(this.state.leaderboardData){
            return (
                <div className={"container"}>
                    <h1>Leaderboards</h1>
                    <Container>
                        <Row>
                            <Col id={"leaderboards-profile-pic"}>
                            </Col>
                            <Col id={"leaderboards-username"}>
                                Username
                            </Col>
                            <Col id={"leaderboards-message-count"}>
                                Message Count
                            </Col>
                            <Col id={"leaderboards-position"}>
                                Position
                            </Col>
                        </Row>
                    </Container>
                    {this.state.leaderboardData.map((entry, i)=>{
                        return <LeaderboardsUserContainerView userData={entry} position={i+1} />
                    })};
                </div>
            );
        } else {
            return(
                <div className={"container"}>
                </div>
            );
        }
    }
}

export default LeaderboardsView;
