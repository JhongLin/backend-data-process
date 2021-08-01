import mysql.connector


def getEmailAddress(serial):
    cep_db = mysql.connector.connect(
        host="localhost",
        user="cep",
        passwd="********",
        database="cep"
    )
    SQL_REQUEST = ('SELECT manager.email FROM manager AS manager '
        'LEFT JOIN user_project AS user_project ON manager.user_id=user_project.user_id '
        'LEFT JOIN sensor AS sensor ON user_project.project_id=sensor.project_id '
        'WHERE sensor.sensor_id=')
    cep_cursor = cep_db.cursor()
    cep_cursor.execute(SQL_REQUEST+str(serial))
    user_Emails = cep_cursor.fetchall()
    if len(user_Emails) == 0:
        return False
    addresses = []
    for address in user_Emails:
        if address[0] != '':
            addresses.append(address[0])
    return addresses

def getMachineName(serial):
    cep_db = mysql.connector.connect(
    host="localhost",
    user="cep",
    passwd="********",
    database="cep"
    )
    SQL_REQUEST = "SELECT displayname FROM sensor WHERE sensor_id="+str(serial)
    cep_cursor = cep_db.cursor()
    cep_cursor.execute(SQL_REQUEST)
    displayname = cep_cursor.fetchall()
    if len(displayname) != 0:
        return displayname[0][0]
    else:
        return False

def main():
    pass

if __name__ == "__main__":
    main()