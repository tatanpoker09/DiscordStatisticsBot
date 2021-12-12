import * as React from "react";
import {ListGroup, ListGroupItem} from "react-bootstrap";


class ChannelList extends React.Component{
    constructor(props) {
        super(props);
    }

    render() {
        if (!this.props.channels) {
            return <p>Loading channels...</p>;
        } else {
            return (
                    <ListGroup>

                        {this.props.channels.map((item) => {
                            return <ListGroupItem action onClick={()=>this.handleClick(item)}>{item}</ListGroupItem>
                        })
                        }
                    </ListGroup>
            );
        }
    }

    handleClick(name) {
        console.log(name);
    }
}

export default ChannelList;
