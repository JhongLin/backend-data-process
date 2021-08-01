from pyModbusTCP.client import ModbusClient
import mysql.connector
#import win_inet_pton
import socket
import re
import numpy as np
import time

def getstatus(addr):

    #print addr
    c = ModbusClient()
    c.port(502)
    ifip=re.match(r"^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$",addr)
    if not ifip:
        try:
            addr=socket.gethostbyname(addr)
        except:
            return None
    #print addr
    c.host(addr)

    if c.open():
        regs=c.read_holding_registers(399, 4)
        c.close()
        return regs


def getaddr():

    cep_db = mysql.connector.connect(
    host="localhost",
    user="cep",
    passwd="********",
    database="cep"
    )
    cep_cursor = cep_db.cursor()
    SQL_REQUEST = "SELECT sensor_id,address FROM sensor"
    
    cep_cursor.execute(SQL_REQUEST)
    result = cep_cursor.fetchall()

    return result

def update(sensors):

    cep_db = mysql.connector.connect(
    host="localhost",
    user="cep",
    passwd="********",
    database="cep"
    )
    cep_cursor = cep_db.cursor()
    SQL_UPDATE="UPDATE sensor SET status = CASE sensor_id "
    for sensor in sensors:
        #SQL_UPDATE+="SET sensor_id="+sensor[0]+" where sensor_id="+sensor[0]+";"
        SQL_UPDATE+=" WHEN "+sensor[0]+" THEN '"+sensor[1]+"'"
    SQL_UPDATE+=" END"
    #print SQL_UPDATE
    cep_cursor.execute(SQL_UPDATE)
    cep_db.commit()

def run():

    sensors=getaddr()
    #print sensors
    datas=[]
    for sensor in sensors:
        
        data=[sensor[0],"uknown"]
        status=getstatus(sensor[1])
        if status!=None:
            data[1]=','.join(map(str,np.around(np.array(status)*0.01,decimals=2)))
            data[1]+=','+time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
        datas.append(data)
    
    #print datas
    update(datas)


'''sensors=[
    ["1007","315,333,777"],
    ["1006","314,332,778"],
    ["1001","uknown"]
]
update(sensors)'''

#cep_cursor=connect_to_sql()
#print(query())

'''addr="356853055830485.eairlink.com"
status=getstatus(addr)
print (status)'''

run()