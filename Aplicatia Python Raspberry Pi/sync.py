import MySQLdb
import os
import time
import sys

#connect
db=MySQLdb.connect(host="1.cateisibiu.ro",port=3306,user="cateisib_1",passwd="64Std(0b{w=x2Qg",db="cateisib_1")
# prepare a cursor object using cursor() method
cursor = db.cursor()
cursor.execute("SELECT VERSION()")

try:
   cursor.execute("SELECT sync_frequency FROM devices WHERE id=1")
   # Fetch all the rows in a list of lists.
   results = cursor.fetchall()
   for row in results:
      sync = row[0]
      # Now print fetched result
      print sync
   # Commit your changes in the database
   db.commit()
except:
   # Rollback in case there is any error
   db.rollback()
# disconnect from server
db.close()