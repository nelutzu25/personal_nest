import MySQLdb
import os
import time
import sys
import RPi.GPIO as GPIO
import Adafruit_DHT as dht
import urllib2
import urllib


GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(23, GPIO.OUT)
GPIO.setwarnings(False)
GPIO.setup(24, GPIO.OUT) 
#GPIO.output(23, False)
#GPIO.output(24, False) 

device_id = 1

current_humidity ,current_temperature = dht.read_retry(dht.DHT22, 4) 

if current_temperature and current_humidity:
    print("Temp = {0} *C, Hum = {1} %".format(current_temperature, current_humidity))
else:
    print("Failed to read from sensor, maybe try again?")
date=time.strftime("%Y/%m/%d %H:%M:%S")

query_args = { 'device_id': device_id , 'current_humidity':current_humidity, 'current_temperature': current_temperature, 'date':date }

print query_args

url = 'http://1.cateisibiu.ro/raspberry.php'
 
data = urllib.urlencode(query_args)
 
request = urllib2.Request(url, data)
 
desired_temperature = int(urllib2.urlopen(request).read())

print desired_temperature

if desired_temperature > current_temperature + 2:
   GPIO.output(24, False)
   GPIO.output(23, True)
else:
	if desired_temperature < current_temperature - 2:
   		GPIO.output(23, False)
		GPIO.output(24, True)
	else:
		GPIO.output(23, False)
		GPIO.output(24, False)