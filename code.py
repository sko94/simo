import mysql.connector
import google.auth
import googleapiclient.discovery

# Replace these values with your own Google Sheet and MySQL details
spreadsheet_id = "your-spreadsheet-id"
range_name = "Sheet1!A:D"
mysql_url = "mysql+mysqlconnector://your-mysql-username:your-mysql-password@localhost/your-database-name"

# Set up the Google Sheets API client
creds, project = google.auth.default()
service = googleapiclient.discovery.build("sheets", "v4", credentials=creds)

# Fetch the data from the Google Sheet
result = service.spreadsheets().values().get(
    spreadsheetId=spreadsheet_id, range=range_name).execute()
values = result.get("values", [])

if not values:
    print("No data found.")

# Connect to the MySQL database
cnx = mysql.connector.connect(url=mysql_url)
cursor = cnx.cursor()

# Loop through the rows of the Google Sheet and insert them into the MySQL database
for row in values:
    sql = "INSERT INTO sheet_data (column1, column2, column3, column4) VALUES (%s, %s, %s, %s)"
    cursor.execute(sql, row)

cnx.commit()
cnx.close()
