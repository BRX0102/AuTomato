import time
import serial
import json

# enable the serial port for the Arduino Uno
ser = serial.Serial('/dev/ttyACM0', 9600)
# execute the loop forever
while 1:
    #temperature and humidity
    data1 = ser.readline()
    print data1
