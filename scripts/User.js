import React,{Component } from 'react';
import * as SocketIO from 'socket.io-client';
import PropTypes from 'prop-types';


var ReactDOM = require('react-dom');

var socket = SocketIO.connect();

  socket.on('connect',function() {
      
      });
  


class Info extends Component{
 constructor(props) {
   super(props);
    //this.state = {
   //   CO2:PropTypes.string
      
    //};
   
  }
 

  
  componentDidMount() {
     
      socket.on('co2Client',this._CO2)
    
      socket.on('disconnect',function(){
        
      });
  }
  _CO2(data)
  {
  //this.state.CO2=data
  console.log("hello");
    
  }

  callStuff()
  {
    socket.emit("co2");
  }
 
  

  render() {
      return (
        <div>
       
        <div>
     
        </div>
        <div>
        <button type="button" onClick={(e) => this.handleClick(e)}>Click Me!</button>
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
