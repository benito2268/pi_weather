import os
import board
from adafruit_bme280 import basic as adafruit_bme280
from time import sleep

i2c = board.I2C()
bme280 = adafruit_bme280.Adafruit_BME280_I2C(i2c)

bme280.sea_level_pressure = 982.5

def get_temp():
    return ((bme280.temperature) * (9/5)) + 32

def get_humid():
    return bme280.relative_humidity

def get_press():
    return bme280.pressure + 36.1 # correction for estimated elevation

f = open("weather.txt", "w")
f.write(str(get_temp()))
f.write("~")
f.write(str(get_humid()))
f.write("~")
f.write(str(get_press()));
f.close()