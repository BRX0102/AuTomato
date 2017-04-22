import React,{Component } from 'react';
import * as SocketIO from 'socket.io-client';



var ReactDOM = require('react-dom');

var socket = SocketIO.connect();

  socket.on('connect',function() {
      
      });
  


class Info extends Component{
 constructor(props) {
    super(props);
    this.state = {
      CO2:"",moisture:"",heat:""
      
    };
  }
 

  
  componentDidMount() {
     
      socket.on('co2Client',this._CO2)
    
      socket.on('disconnect',function(){
        
      });
  }
  _CO2(data)
  {
    this.state.CO2=data;
    
  }

  callStuff()
  {
    socket.emit("co2");
  }
 
  

  render() {
      return (
        <div>
       
        <div>
        <p>{this.state.CO2}</p>
        </div>
        <div>
        <button type="button" onClick={this.callStuff()}>Click Me!</button>
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
