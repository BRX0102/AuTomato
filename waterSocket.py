from socketIO_client import SocketIO, LoggingNamespace
import time
import serial
import json
import requests

"""
relays the information with sockets
"""
def on_connect():
    print('connect')

def on_disconnect():
    print('disconnect')

def on_reconnect():
    print('reconnect')

def on_aaa_response(*args):
    print('on_aaa_response', args)

socketIO = SocketIO('http://shielded-brushlands-57140.herokuapp.com', verify=False)

socketIO.on('connect', on_connect)

# enable the serial port for the Arduino Uno
#ser = serial.Serial('/dev/ttyACM0', 9600)
# execute the loop forever
while 1:
#    #temperature and humidity
#    data1 = ser.readline()
    socketIO.emit('co2','hello brandan')
    socketIO.wait(seconds=5)
