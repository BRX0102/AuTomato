import time
import serial
import json

# enable the serial port for the Arduino Uno
ser = serial.Serial('/dev/ttyACM0', 9600)
# execute the loop forever
while 1:
    #temperature and humidity
    data1 = ser.readline()
    data1 = data1.replace("\n", " ")
    data1 = data1.replace("'", "\"")
    data2 = ser.readline()
    data2 = data2.replace("\n", " ")
    data2 = data2.replace("'", "\"")
    data3 = ser.readline()
    data3 = data3.replace("\n", " ")
    data3 = data3.replace("'", "\"")
    
    print data1
    print data2
    print data3
    
    #newData = json.loads(temp)
    #socketIO.emit('co2',data1)
    #socketIO.emit('co2',data2)
    #socketIO.emit('co2',data3)
    #asString = json.dumps(newData)

