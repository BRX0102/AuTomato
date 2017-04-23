# app.py
import os, flask, flask_socketio,requests
from flask_socketio import emit,send
import json

app = flask.Flask(__name__)


socketio = flask_socketio.SocketIO(app)

   
@app.route('/')
def hello():
 return "happy tomato day"

@socketio.on('connect')
def on_connect():
 print "%s USER CONNECTED " %  flask.request.sid

@socketio.on('co2')
def on_co2(data):
 #hello="hello"
 #hello = str(data)
 #print hello
 socketio.emit('co2Client',data, broadcast=True)
   
@socketio.on('disconnect')
def on_disconnect():
 global names
 print "USER DISCONNECTED"
 
    
if __name__ == '__main__':# __name__!
 socketio.run(
 app,
 host=os.getenv('IP', '0.0.0.0'),
 port=int(os.getenv('PORT', 8008)),
 debug=True
 )
