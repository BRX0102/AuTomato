import React, { Component } from 'react';
import GoogleMapReact from 'google-map-react';
import * as SocketIO from 'socket.io-client';

import { Socket } from './Socket';

const AnyReactComponent = ({ text }) => (
  <div id="circle">
    {text}
  </div>
);


export default class SimpleMap extends Component {
  constructor(props){
    super(props);
    
    this.state = {
      pointsLat: [],
      pointsLon: [],
      pointsStatus: []
    };
    this._loadPoints= this._loadPoints.bind(this)
  }
  componentWillMount () {
    Socket.on('coordinates', this._loadPoints);
    Socket.emit('readData');
  }
  
  _loadPoints(data){
    data=data["items"]
    console.log(data);
    var {pointsLat, pointsLon, pointsStatus} = this.state;
    for(var i = 0; i < data.length; i++){
      pointsLat.push(data[i]['latitude']);
      pointsLon.push(data[i]['longitude']);
      pointsStatus.push(data[i]["status"]);
      
     
    }
    console.log(pointsLat);
    this.setState({pointsLat, pointsLon, pointsStatus});
  }

  render() {
    var points = [];
    for (var i = 0; i < this.state.pointsLat.length; i++) {
      points.push(<AnyReactComponent
        key={i}
        lat={this.state.pointsLat[i]} 
        lng={this.state.pointsLon[i]} 
        
      />);
    }
    return (
      <div>
        <GoogleMapReact
          bootstrapURLKeys={{key: "AIzaSyC-ybfnXcarIL9o1ZTax8Afg1GHpTyNWP4"}}
          defaultCenter={this.props.center}
          defaultZoom={this.props.zoom}
        >
          {points}
        </GoogleMapReact>
      </div>
    );
  }
}

SimpleMap.defaultProps = {
  center: {lat: 36.676909, lng: -121.655713},
  zoom: 13
}