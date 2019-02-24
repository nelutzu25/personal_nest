import sys
import Adafruit_DHT as dhtreader
h,t = dhtreader.read(dhtreader.DHT22, 4)
if t and h:
    print("Temp = {0} *C, Hum = {1} %".format(t, h))
else:
    print("Failed to read from sensor, maybe try again?")