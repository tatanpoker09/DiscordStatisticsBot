import React from "react";
import {Row, Col, Container} from "react-bootstrap";

class LeaderboardsUserContainerView extends React.Component{
    constructor(props) {
        super(props);
        this.message_count = this.props.userData.message_count;
        this.username = this.props.userData.username;
        this.profile_pic = this.props.userData.username;
        this.position = this.props.position;
    }

    render() {
        return (
            <Container className={"border round-container"}>
                <Row>
                    <Col id={"leaderboards-profile-pic"}>
                        <img src="data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUA
        AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO
            9TXL0Y4OHwAAAABJRU5ErkJggg==" alt="Red dot" />
                    </Col>
                    <Col id={"leaderboards-username"}>
                        {this.username}
                    </Col>
                    <Col id={"leaderboards-message-count"}>
                        {this.message_count}
                    </Col>
                    <Col id={"leaderboards-position"}>
                        {this.position}
                    </Col>
                </Row>
            </Container>
        );
    }
}

export default LeaderboardsUserContainerView;
