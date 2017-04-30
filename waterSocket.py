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

socketIO = SocketIO('http://shielded-brushlands-57140.herokuapp.com', verify=False)

socketIO.on('connect', on_connect)

readings = []
poLatitude = 36.673737
poLongitude = -121.657529
severity = 3
sentFlag = False

# enable the serial port for the Arduino Uno
ser = serial.Serial('/dev/ttyACM0', 9600)
# execute the loop forever

sum = 0
while 1:
    #water sensor
    data1 = ser.readline()
    if '\n' in data1:
        data1 = data1.split()[0]
        if int(data1) >= 100 and int(data1)<1000:
            socketIO.emit('water',data1)
            print data1

            #to ping a location if the sensor reaches a certain level
            for i in readings:
                sum = sum + int(i)

            
            
            if int(data1) < 490:
                if sentFlag is not True: 
                    print "below the threshold"
                    socketIO.emit('markEndPoint',{
                        'latitude': poLatitude,
                        'longitude': poLongitude,
                        'blockType': severity,
                    })
                    sentFlag = True

            print readings
            if len(readings) == 5:
                # deletes the list
                del readings[:]
                sum = 0

    #socketIO.wait(seconds=1)
