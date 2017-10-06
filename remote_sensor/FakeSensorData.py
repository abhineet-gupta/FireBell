#!/usr/bin/python3
'''
Create fake sensor data for a sensor at regular intervals

'''

from urllib import request, parse
import time
import random

URL = "http://13.72.243.229/add.php"

SID = 1

REPS = 10
WAIT_S = 5

MIN_T = 10
MAX_INC = 20
MAX_CO = 25

def fake():
    '''
    Send fake data HTTP GET requests
    '''

    for i in range(REPS):
        temp = "{0:.2f}".format(random.random()*MAX_INC+MIN_T)
        carbon_mono = int(random.random()*MAX_CO)
        smoke = round(random.random())

        data_dict = {"sensor_id": SID,
                     "temp": temp,
                     "co": carbon_mono, "smoke": smoke}

        data_json = parse.urlencode(data_dict).encode()
        req = request.Request(URL, data=data_json)
        request.urlopen(req)

        print(data_dict, "published by sensor #", SID)
        time.sleep(WAIT_S)

if __name__ == "__main__":
    fake()
