import time
import serial

# enable the serial port for the Arduino Uno
ser = serial.Serial('/dev/ttyACM0', 9600)
# execute the loop forever
while 1:
    #temperature and humidity
    # read the serial data sent by the UNO
    print (ser.readline())
    # print the serial data sent by UNO
    print (ser.readline())
