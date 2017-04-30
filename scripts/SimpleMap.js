import React, { Component } from 'react';
import GoogleMapReact from 'google-map-react';

const AnyReactComponent = ({ text }) => <div>{text}</div>;

export default class SimpleMap extends Component {
  constructor(props){
    super(props);
    
    this._loadPoints= this._loadPoints.bind(this)
  }
  componentDidMount () {
    socket.on('coordinates', this._loadPoints);
  }
  
  _loadPoints(data){
    for(var i = 0; i < data.length; i++){
      pointsLat.push(data.items['latitude']);
      pointsLon.push(data.items['longitude']);
      pointsText.push(data.items['blockType']);
    }
  }

  render() {
    return (
      <GoogleMapReact
        defaultCenter={this.props.center}
        defaultZoom={this.props.zoom}
      >
        {
          this.props.pointsLat.map((point,i) => {
            return (
            <AnyReactComponent
              key={i}
              lat={this.props.pointsLat}
              lon={this.props.pointsLon}
              text={this.props.text}
            />
            );
          })
        }
      </GoogleMapReact>
    );
  }
}

SimpleMap.defaultProps = {
  pointsLat: [],
  pointsLon: [],
  pointsText: [],
  center: {lat: 36.676909, lng: -121.655713},
  zoom: 13
}