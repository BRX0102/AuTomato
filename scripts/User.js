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
    temp: "30"
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
  this.setState({ CO2:data, temp:50 });
  //console.log("hello");
    
  }

  callStuff()
  {
    socket.emit("co2");
  }
 
  

  render() {
      return (
        <div>
       <p>{this.state.CO2}</p>
       <div className="col-md-2 col-md-offset-5">
      		<Thermometer
      				min={30}
      				max={100}
      				width={20}
      				height={300}
      				backgroundColor={'blue'}
      				fillColor={'green'}
      				current={this.state.temp}
      		/>
        </div>
        <div>
        <button type="button" onClick={(e) => this.callStuff(e)}>Click Me!</button>
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
