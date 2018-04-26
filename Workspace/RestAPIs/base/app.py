from flask import Flask
from flaskext.mysql import MySQL
from flask_restful import Api
from base.paceplace.common import db

mysql = MySQL()
app = Flask(__name__)
# cors = CORS(app, resources={r"/*": {"origins": "*"}})

app.config['MYSQL_DATABASE_USER'] = db.MYSQL_DATABASE_USER
app.config['MYSQL_DATABASE_PASSWORD'] = db.MYSQL_DATABASE_PASSWORD
app.config['MYSQL_DATABASE_DB'] = db.MYSQL_DATABASE_DB
app.config['MYSQL_DATABASE_HOST'] = db.MYSQL_DATABASE_HOST

mysql.init_app(app)
api = Api(app)

# importing other python files which will not be used in this file, we import them just to initialize the file data
# static_info has to be the first line of the import statement -- rest all will follow it

from base.paceplace.common import static_info
from base.paceplace.restapi import user_info
