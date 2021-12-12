import * as React from "react";
import {Col, Container, Row} from "react-bootstrap";
import ChannelList from "./ChannelList";
import DayStatistics from "./DayStatistics"
import {useState} from "react";
import {useCallback} from "react";
import {useEffect} from "react";

function DiscordStats(){
    const [channels, setChannels] = useState([]);
    const [loading, setLoading] = useState(true);
    const fetchDiscordChannels = useCallback(async()=>{
        let channels = [];
        let response = await fetch("http://localhost:3001/channels/all")
            .then((res)=> res.json());
        response.forEach((channel)=>{
            channels.push(channel.name)
        });

        setChannels(channels);
        setLoading(false);
    });

    useEffect(() => {
    fetchDiscordChannels();
}, []);

if (loading) return <p>Loading posts...</p>;
        return(<div className={"margin-top"}>
            <Container fluid={true}>
                <Row>
                    <Col xs lg="2">
                        <div id={"channels-header"}>
                            Discord Channels
                        </div>
                        <div>
                            <ChannelList channels={channels}/>
                        </div>
                    </Col>
                    <Col>
                        <div className={"border"}>
                            <DayStatistics/>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>)
}
export default DiscordStats;
