# app.py
import os, flask, flask_socketio,requests
from flask import request
from flask_socketio import emit,send
import json
import flask_sqlalchemy

app = flask.Flask(__name__)

# app.app = app module's app variable
#app.config['SQLALCHEMY_DATABASE_URI'] = os.getenv('DATABASE_URL')
import models
#db = flask_sqlalchemy.SQLAlchemy(app)
socketio = flask_socketio.SocketIO(app)




@app.route('/')
def hello():
 return flask.render_template('index.html')
 


@socketio.on('connect')
def on_connect():
 print "%s USER CONNECTED " %  flask.request.sid

#why heroku why
@socketio.on_error_default
def default_error_handler(e):
    print(request.event["message"]) # "my error event"
    print(request.event["args"])    # (data,)
    
@socketio.on('water')
def on_co2(data):
 hello=data
 print(hello)
 socketio.emit('co2Client',hello,broadcast=all)
   

@socketio.on('readData')
def read_data():
 items =models.ClosedRoads.query.all()
 print items
  
@socketio.on('disconnect')
def on_disconnect():
 global names
 print "USER DISCONNECTED"
 
    
if __name__ == '__main__':# __name__!
 socketio.run(
 app,
 host=os.getenv('IP', '0.0.0.0'),
 port=int(os.getenv('PORT', 8080)),
 debug=True
 )