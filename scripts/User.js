import React,{Component } from 'react';
import * as SocketIO from 'socket.io-client';
import PropTypes from 'prop-types';
import Thermometer from "react-thermometer";


var ReactDOM = require('react-dom');

var socket = SocketIO.connect();

  socket.on('connect',function() {
      
      });
  


class Info extends Component{
 constructor(props) {
   super(props);
    this.state = {
    CO2:"",
    temp: "30",
    humidity: "10"
    };
    //added new line of code to bind the state variable before it is used
    //autobinding is disabled
   this._CO2= this._CO2.bind(this)
  }
 

  
  componentDidMount() {
     
      socket.on('co2Client',this._CO2)
    
      socket.on('disconnect',function(){
        
      });
  }
  _CO2(data){
    console.log(data);
    let rand2 = this._getRandom();
    let temp1 = this.state.temp;
    temp1 = parseInt(temp1)+1;
    this.setState({ 
      CO2:data,
      temp: temp1,
      humidity: rand2
    });
  //console.log("hello");
    
  }
  _getRandom(){
    return Math.floor(Math.random() * 99 + 1);
  }

  callStuff()
  {
    socket.emit("co2");
  }
 
  

  render() {
      return (
        <div>
        <div className="panel panel-default col-md-2 col-xs-2 col-md-offset-3">
          <div className="panel-heading"><h3 className="panel-title">Temperature</h3></div>
          <div className="panel-body">
            <div className="col-md-2 col-xs-2 col-md-offset-3">
              		<Thermometer
              				min={30}
              				max={130}
              				width={20}
              				height={200}
              				backgroundColor={'blue'}
              				fillColor={'green'}
              				current={this.state.temp}
              		/>
              	</div>
          </div>
          <p>The Temperature is currently: {this.state.temp}</p>
        </div>
        
        <div className="panel panel-default col-md-2 col-xs-2">
            <div className="panel-heading"><h3 className="panel-title">Humidity</h3></div>
            <div className="panel-body">
              <div className="col-md-2 col-xs-2">
            		<p>The humidity is currently: {this.state.humidity}</p>
            	</div>
            </div>

            
        </div>
        <div className="panel panel-default col-md-2 col-xs-2">
          <div className="panel-heading"><h3 className="panel-title">Humidity</h3></div>
            <div className="panel-body">
              <div className="col-md-2 col-md-offset-3 col-xs-2 col-xs-offset-3">
                <button type="button" onClick={(e) => this.callStuff(e)}>Click Me!</button>
              </div>
            </div>
          </div>
        <div className="row">
        
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
