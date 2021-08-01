import smtplib
from email.mime.text import MIMEText
from email.header import Header


def sendAlertEmail(receivers, machine_name, vibr_info, filename):
    sender = 'abc@mail.com'
    passwd = 'myAccount'
    #receivers = ['qcp87623abc@gmail.com', 'jhong_lin@sanlien.com.tw', 'qcp87623abc@hotmail.com','jeremy850407@gmail.com']
    fileURL = 'http://cep.sanlien.com/CEP/downloadFile.php?files='+filename+'&act=event&path=../CEP_WebData/event'
    msg_content = ('<br>This Alert is from the Palert S3 \"'+machine_name+'\":<br/>'
        +'<br>'+vibr_info[1]+'<br/>'
        +'<br>The Event File Download: '+ '<a href=\"'+fileURL+'\">click here</a>'  +'<br/>'+
        '<br>If you want to see more details, please go to our website: http://cep.sanlien.com/CEP/<br/>'
    )
    receiverstr = '<'+receivers[0]+'>'
    if len(receivers) > 1:
        for i in range(1, len(receivers)):
            receiverstr += ', <'+receivers[i]+'>'
    msg = MIMEText(msg_content, 'html', 'utf-8')
    msg['Subject'] = Header(vibr_info[0]+' CEP Web Service: Vibration Alert from '+machine_name, 'utf-8')
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

def main():
    pass

if __name__ == "__main__":
    main()
