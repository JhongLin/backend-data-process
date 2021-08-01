import os
import time
import csv
import em
import calldb
def getEventDetails(filePath):
    try:
        file = open(filePath, mode='r')
        line = []
        print("OK")
        for i in range(7):
            line.append(file.readline())
    except:
        print("Read File error!")
        return False
    strtoks = line[0].split(' ')
    vibr_info = strtoks[2]+' '+strtoks[3]+'PPV: '
    time = strtoks[2]+' '+strtoks[3][:-1]
    strtoks = line[4].split()
    max_PPV,max_index = 0 , 0
    for i in range(3,6):
        value = float(strtoks[i][0:-1])
        if max_PPV < value:
            max_PPV = value
            max_index = i-1
    strtoks = line[5].split()
    vibr_info += str(max_PPV)+'(mm/s) '+strtoks[max_index][0:-1]+'(Hz).'
    file.close()
    rl = [] #return list
    rl.append(time)
    rl.append(vibr_info)
    return(rl)
def main():
    #this python file is to clean the upload folder to keep the upload.php efficient
    dirs = [] # all folders in this path
    serials = [] # record every sensor's serial
    serial_amount = [] # record how many PPV_daily the sensors have
    serial_maxDate = [] # record the which one file is lastest PPV_Daily file of each sensor
    path = os.getcwd()
    bakdir_name = 'data_backup' # ignore the backup folder
    bak_path = path+'/'+bakdir_name 
    currentDate = time.strftime("%Y%m%d", time.localtime())
    currentHour = time.strftime("%H", time.localtime())

    scan = os.listdir(path)
    for f in scan:
        if os.path.isdir(path+"/"+f):
            dirs.append(f)


    if not os.path.isdir(bak_path):
        os.mkdir(bak_path, 0755)
    else:
        #print "Bak_dir_exists!"
        dirs.remove(bakdir_name)

    ################# (RE)MOVING PART #################
    for d in dirs:
        dir_path = path+'/'+d
        files = os.listdir(dir_path)
        for f in files: # use system time to determine whether the file should be moved
            try:
                if os.path.splitext(dir_path+'/'+f)[1] == '.csv':
                    names = f.split('_')
                    if names[-1] == 'CEP.csv':
                        if names[1] not in serials:
                            serials.append(names[1])
                            serial_amount.append(1)
                            serial_maxDate.append(names[2])
                        else:
                            serial_amount[serials.index(names[1])] += 1
                            if names[2] > serial_maxDate[serials.index(names[1])]:
                                serial_maxDate[serials.index(names[1])] = names[2]
                        if names[-2] < currentDate and currentHour > "00":
                            os.rename(dir_path+'/'+f, bak_path+'/'+f) # move the file to backup folder
                    elif len(names) > 1:
                        if names[1] == 'palert':
                            if names[3] == 'dinEvent':
                                try:
                                    email_address = calldb.getEmailAddress(names[2])
                                    machine_name = calldb.getMachineName(names[2])
                                    Event_detail = getEventDetails(dir_path+'/'+f)
                                    if (email_address == False) or (machine_name == False) or(Event_detail==False):
                                        print("There's no information in DataBase, please check with administrator")
                                    else:
                                        em.sendAlertEmail(email_address, machine_name, Event_detail, f)
                                except:
                                    print("Error occurs in Sending e-mail")
                                os.rename(dir_path+'/'+f, bak_path+'/'+f) # move the file to backup folder
                                #os.remove(dir_path+'/'+f)'
                            if names[3] == 'dinDaily':
                                os.rename(dir_path+'/'+f, bak_path+'/'+f) # move the file to backup folder
                                #os.remove(dir_path+'/'+f)'
            except OSError as e:
                print e
            #else:
            #    print "File is moved successfully"
    ################# If not sync NTP #################
        #print serials
        #print serial_amount
        for i in range(len(serial_amount)): # Only left a PPV_file which is lastest one
            if serial_amount[i] > 1:
                serial = serials[i]
                maxDate = ''
                for f in files:
                    try:
                        if os.path.splitext(dir_path+'/'+f)[1] == '.csv':
                            names = f.split('_')
                            if names[-1] == 'CEP.csv':
                                if names[1] == serial and names[2] < serial_maxDate[serials.index(names[1])]:
                                    os.rename(dir_path+'/'+f, bak_path+'/'+f) # move the file to backup folder
                    except OSError as e:
                        print e


    ################# Adding header to PPV file #################
    for d in dirs:
        dir_path = path+'/'+d
        files = os.listdir(dir_path)
        for f in files: # use system time to determine whether the file should be moved
            if os.path.splitext(dir_path+'/'+f)[1] == '.csv':
                names = f.split('_')
                if names[-1] == 'CEP.csv':
                    #print(dir_path+'/'+f)
                    fr = open(dir_path+'/'+f,"r")
                    lines = fr.readlines()
                    content = "".join(lines)
                    #print(lines[0])
                    #print(content)
                    if lines[0].split(",")[0] != "#MACHINE_ID":
                        fw = open(dir_path+'/'+f,"w")
                        fw.write("#MACHINE_ID,Time,Peak_Value,Frequency,axis"+"\n"+content)
                        fw.close()
                        #print("RRR")
                    fr.close()


if __name__ == "__main__":
    main()