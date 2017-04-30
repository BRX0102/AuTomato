# app.py
import os, flask, flask_socketio,requests
from flask import request
from flask_socketio import emit,send
import json
import flask_sqlalchemy
from email.MIMEMultipart import MIMEMultipart
from email.MIMEText import MIMEText
from email.MIMEImage import MIMEImage
import smtplib

app = flask.Flask(__name__)

# app.app = app module's app variable
app.config['SQLALCHEMY_DATABASE_URI'] = os.getenv('DATABASE_URL')
import models
db = flask_sqlalchemy.SQLAlchemy(app)
socketio = flask_socketio.SocketIO(app)




@app.route('/')
def hello():
 return flask.render_template('index.html')
 


@socketio.on('connect')
def on_connect():
 print "%s USER CONNECTED " %  flask.request.sid


    
@socketio.on('water')
def on_co2(data):
 hello=data
 print(hello)
 socketio.emit('co2Client',hello,broadcast=all)
  


@socketio.on('readData')
def read_data():
 print os.getenv('DATABASE_URL')
 socketio.emit('coordinates',{"items":[item.json() for item in models.ClosedRoads.query.all()]})
 
@socketio.on('markEndPoint')
def point_on_map(data):
 print data 
 print data['latitude']
 print data['longitude']
 print data['blockType']
 point=models.ClosedRoads(float(data['latitude']),float(data['longitude']),int(data['blockType']))
 models.db.session.add(point)
 models.db.session.commit()
 socketio.emit('newPoint',{"latitude":data["latitude"],"longitude":data["longitude"],"blockType":data["blockType"]})
 socketio.emit('co2Client',data,broadcast=all)
 #socketio.emit('markEndPointSuccess',{"status":"Success"})
 
 
 print data

@socketio.on('sendText')
def sendMessage(data):
    print "I got here tehehe"
     # Use sms gateway provided by mobile carrier:
    # at&t:     number@mms.att.net
    # t-mobile: number@tmomail.net
    # verizon:  number@vtext.com
    # sprint:   number@page.nextel.com
    # Establish a secure session with gmail's outgoing SMTP server using your gmail account4
    strFrom = os.getenv('email')
    strTo = str(os.getenv("number"))+"@mms.att.net"
    # Create the root message and fill in the from, to, and subject headers
    msgRoot = MIMEMultipart('related')
    msgRoot['From'] = strFrom
    msgRoot['To'] = strTo
    msgRoot.preamble = 'This is a multi-part message in MIME format.'
    msg = 'Alert at '+data
    
    msgAlternative = MIMEMultipart('alternative')
    msgRoot.attach(msgAlternative)
    msgText = MIMEText(msg, 'plain')
    msgAlternative.attach(msgText)
    #start emailing
    server = smtplib.SMTP( "smtp.gmail.com", 587 )
    # Send the email (this example assumes SMTP authentication is required)
    server.starttls();
    server.login( os.getenv('email'),os.getenv('password'))

    server.sendmail(strFrom, strTo, msgRoot.as_string())
    server.quit()

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