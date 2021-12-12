import * as React from "react";
import {useEffect} from "react";
import {useRef} from "react";

const d3 = require("d3");

function DayStatistics(props){
    let username = props.username? props.username : "all";

    const ref = useRef();
    const width = 1000;
    const height = 800;
    var x,y,gX, gY, xAxis, yAxis;
    var idList=1
    var color = d3.scaleOrdinal(d3.schemeCategory10);
    var mainData = null;
    var line;
    var settings = {
        targets:[],
        detail:{
            type:"line"
        }
    };

    useEffect(() => {
        var margin = {top: 20, right: 40, bottom: 60, left: 60},
            width = 1500 - margin.left - margin.right,
            height = 400 - margin.top - margin.bottom;

// Parse the date / time
        var parseTime = d3.timeParse("%Y-%m-%d");
        var xScale = d3.scaleBand().rangeRound([0, width], .05).padding(0.1);

        var yScale = d3.scaleLinear().range([height, 0]);

        var xAxis = d3.axisBottom(xScale).tickFormat(d3.timeFormat("%Y-%m"));

        var yAxis = d3.axisLeft(yScale)
            .ticks(10);

        const svg = d3.select(ref.current)
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform",
                "translate(" + margin.left + "," + margin.top + ")");
        var zoom = d3.zoom().on("zoom",zoomed);

        fetch(`http://localhost:3001/messages/date/${username}`)
            .then((res)=> res.json())
            .then(function(data) {
            data.forEach(function (d) {
                d.date = parseTime(d.date);
                d.value = +d.count;
            });

            xScale.domain(data.map(function (d) {
                return d.date;
            }));
            yScale.domain([0, d3.max(data, function (d) {
                return d.value;
            })]);

            svg.append("g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + height + ")")
                .call(xAxis)
                .selectAll("text")
                .style("text-anchor", "end")
                .attr("dx", "-.8em")
                .attr("dy", "-.55em")
                .attr("transform", "rotate(-90)");


                var ticks = d3.selectAll(".tick text");
                ticks.each(function(_,i){
                    if(i%30 !== 0) d3.select(this).remove();
                });
            svg.append("g")
                .attr("class", "y axis")
                .call(yAxis)
                .append("text")
                .attr("transform", "rotate(-90)")
                .attr("y", 6)
                .attr("dy", ".71em")
                .style("text-anchor", "end")
                .text("Value ($)");

            svg.selectAll("bar")
                .data(data)
                .enter().append("rect")
                .style("fill", "steelblue")
                .attr("x", function (d) {
                    return xScale(d.date);
                })
                .attr("width", xScale.bandwidth())
                .attr("y", function (d) {
                    return yScale(d.value);
                })
                .attr("height", function (d) {
                    console.log(yScale);
                    return height - yScale(d.value);
                });
        })
    }, []);

    function zoomed() {
        gX.call(xAxis.scale(d3.event.transform.rescaleX(x)));
        var new_x = d3.event.transform.rescaleX(x);

        if(settings.detail.type === "line"){
            line.x(function(d) { return  new_x(d.date); })
            d3.select("#canvas").selectAll("path.line")
                .data(mainData)
                .attr("d",function(d){
                    return line(d.data);
                });
        } else if(settings.detail.type == "bar"){
            let barWidth = new_x(new Date("2016-01-02")) - new_x(new Date("2016-01-01"));
            d3.select("#canvas").selectAll("rect.bar")
                .data(mainData[0].data)
                .attr("x",function(d){return new_x(d.date)-barWidth*0.5;})
                .attr("width",barWidth);
        }
    }

    return (
        <svg
            ref={ref}
        />
    )
}
export default DayStatistics;
