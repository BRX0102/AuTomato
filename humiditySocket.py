from socketIO_client import SocketIO, LoggingNamespace
import time
import serial


def on_connect():
    print('connect')

def on_disconnect():
    print('disconnect')

def on_reconnect():
    print('reconnect')

def on_aaa_response(*args):
    print('on_aaa_response', args)

socketIO = SocketIO('localhost', 8008, LoggingNamespace)
socketIO.on('connect', on_connect)
socketIO.on('disconnect', on_disconnect)
socketIO.on('reconnect', on_reconnect)

# enable the serial port for the Arduino Uno
ser = serial.Serial('/dev/ttyACM0', 9600)
# execute the loop forever
while 1:
    #temperature and humidity
    data1 = ser.readline()
    data2 = ser.readline()
    data3 = ser.readline()
    
    socketIO.emit('co2',data1)
    socketIO.emit('co2',data2)
    socketIO.emit('co2',data3)
    #socketIO.emit('co2',"shits good fam")
    time.sleep(5)
    # read the serial data sent by the UNO
    # print the serial data sent by UNO
