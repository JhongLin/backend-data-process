import smtplib
from email.mime.text import MIMEText
from email.header import Header
import mysql.connector
import time

def sendAlertEmail(receivers, machine_name, info):
    sender = 'abc@mail.com'
    passwd = 'myAccount'
    #receivers = ['qcp87623abc@gmail.com', 'jhong_lin@sanlien.com.tw', 'qcp87623abc@hotmail.com','jeremy850407@gmail.com']
    #fileURL = 'http://cep.sanlien.com/CEP/downloadFile.php?files='+filename+'&act=event&path=../CEP_WebData/event'
    msg_content = ('<br>This Alert is from the Palert S3 \"'+machine_name+'\":<br/>'
        +'<br>'+info+'<br/>'
    )
    receiverstr = '<'+receivers[0]+'>'
    if len(receivers) > 1:
        for i in range(1, len(receivers)):
            receiverstr += ', <'+receivers[i]+'>'
    msg = MIMEText(msg_content, 'html', 'utf-8')
    msg['Subject'] = Header(info[0]+' CEP Web Service: Power Alert from '+machine_name, 'utf-8')
    msg['From'] = Header('CEP-WebService', 'utf-8')
    msg['To'] = Header(receiverstr, 'utf-8')

    try:
        smtp = smtplib.SMTP_SSL()
        smtp.connect('mail.cep.com.sg', 465)
        smtp.ehlo()
        smtp.login(sender, passwd)
        smtp.sendmail(sender, receivers, msg.as_string())
        print('Email sent!')
    except smtplib.SMTPException:
        print('ERROR!')
    smtp.quit()

def get_sensor():

    '''cep_db = mysql.connector.connect(
    host="localhost",
    user="cep",
    passwd="********",
    database="cep"
    )
    cep_cursor = cep_db.cursor()'''
    SQL_REQUEST = "SELECT sensor_id,project_id,status,displayname FROM sensor"
    
    cep_cursor.execute(SQL_REQUEST)
    result = cep_cursor.fetchall()

    return result

def get_mail():

    SQL_REQUEST = "SELECT user_project.project_id,manager.email FROM user_project INNER JOIN manager ON user_project.user_id = manager.user_id"
    cep_cursor.execute(SQL_REQUEST)
    result = cep_cursor.fetchall()

    project_mail={}
    for res in result:
        if res[0] not in project_mail:
            if res[1]!="":
                project_mail[res[0]]=[res[1]]
        else:
            if res[1]!="":
                project_mail[res[0]].append(res[1])
        
    return project_mail

def run():

    project_mail=get_mail()
    sensors=get_sensor()

    for sensor in sensors:
        #print sensor
        if sensor[2]=="" or sensor[2]=="unknown":
            continue
        exvol=float(sensor[2].split(',')[1])
        if exvol<11.5:
            msg=sensor[3]+": external power is lower than 11.5V "+time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
            sendAlertEmail(project_mail[sensor[1]],sensor[3],msg)


cep_db = mysql.connector.connect(
host="localhost",
user="cep",
passwd="********",
database="cep"
)
cep_cursor = cep_db.cursor()

run()


