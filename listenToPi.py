import time
import serial
import json

# enable the serial port for the Arduino Uno
ser = serial.Serial('/dev/ttyACM0', 9600)
# execute the loop forever
while 1:
    temp = """"""
    #temperature and humidity
    data1 = ser.readline()
    data2 = ser.readline()
    data3 = ser.readline()
    
    temp = """"""
    temp = temp + data1
    temp = temp + data2
    temp = temp + data3
