import React,{Component} from 'react';
import * as SocketIO from 'socket.io-client';
import PropTypes from 'prop-types';
import Thermometer from "react-thermometer";
import GoogleMapReact from 'google-map-react';
import SimpleMap from './SimpleMap';

import { Socket } from './Socket';

var ReactDOM = require('react-dom');

  Socket.on('connect',function() {
    
      });
  

class Button extends Component {
  constructor(props) {
   super(props);
    this.state = {

    };
    //added new line of code to bind the state variable before it is used
    //autobinding is disabled
   this._coordinates= this._coordinates.bind(this)
  }
  
   componentDidMount() {
     
      Socket.on('markEndPointSuccess',this._coordinates)
 
  }
    handleSubmit(event) {
        event.preventDefault();

        let random = Math.floor(Math.random() * 100);
        console.log('Generated a random number: ', random);
        Socket.emit('markEndPoint',{'latitude':100,'longitude':100,'blockType':0});
        console.log('Sent up the random number to server!');
        
    }
    _coordinates(data){
    console.log(data);
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <button>Send up a random number!</button>
            </form>
        );
    }
}
class Info extends Component{
 constructor(props) {
   super(props);
    this.state = {
    CO2:"",
    temp: "45",
    humidityLevel: "60",
    moistureLevel: "10",
    moistureState: "wet",
    roadStatus: "muddy"
    };
    //added new line of code to bind the state variable before it is used
    //autobinding is disabled
   this._CO2= this._CO2.bind(this)
  }
 

  
  componentDidMount() {
     
      Socket.on('co2Client',this._CO2)
    
      Socket.on('disconnect',function(){
        
      });
  }
  _CO2(data){
    console.log(data);
    let rand2 = this._getRandom();
    let temp1 = this.state.temp;
    temp1 = parseInt(temp1)+1;
    this.setState({ 
      CO2:'no',
      temp: temp1,
      humidity: rand2
    });
  //console.log("hello");
    
  }
  _getRandom(){
    return Math.floor(Math.random() * 99 + 1);
  }
  
  _smokeImage(){
    let smoke = this.state.CO2;
    if(smoke.hasOwnProperty('no')){
      return '../../static/img/bad.png';
    }else{
      return '../../static/img/good.png'
    }
  }
  
  _moistureLevel(){
    let moistureL = this.state.moistureState;
    if(moistureL.hasOwnProperty("wet")){
      return '../../static/img/wet.jpg';
    }else if(moistureL.hasOwnProperty("dry")){
      return '../../static/img/dry.jpg';
    }else{
      return '../../static/img/moist.jpg';
    }
  }

  callStuff()
  {
    Socket.emit("co2");
  }
 
  

  render() {
    return (
      <div>
        <div className="row">
          <div className="panel panel-default col-md-8 col-xs-8 col-md-offset-2 col-xs-offset-2">
            <div className="panel-heading"><h3 className="panel-title">Current Road Conditions</h3></div>
              <div className="panel-body">
                <div className="madness" style={{width: '100%', height: '400px'}}>
                  <SimpleMap />
                </div>
              </div>
              <div className="panel-footer">Legend: <div id="circleGreen">Clear</div> <div id="circleYellow">Trucks</div> <div id="circleOrange">Tractors</div> <div id="circleRed">Impassable</div></div>
          </div>
        </div>
        
        <div className="row">
          <div className="panel panel-default col-md-8 col-xs-8 col-md-offset-2 col-xs-offset-2">
            <div className="panel-heading"><h3 className="panel-title">Current Trends</h3></div>
            <div className="panel-body">
              <div className="madness" style={{width: '100%', height: '400px'}}>
                <SimpleMap />
              </div>
            </div>
            <div className="panel-footer">Status: {this.state.roadStatus}</div>
        </div>
      </div>
      </div>
    );
  }
  
}

//deals with background image
class GiveMeACat extends Component {
  

 
  render() {
    //console.log(this.state.fetchedData);
    return (

      <div>
        {

            <img id="background"  src={this.state.fetchedData} alt="Cuteness"  />


        }
      </div>
    );
  }
}

export class Chat extends React.Component {
  render() {
    return (
      <div className="Chat">
    
       <Info />
       
      </div>
    );
  }
}