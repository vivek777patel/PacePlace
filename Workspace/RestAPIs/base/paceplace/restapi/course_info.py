from base import app
from flask import jsonify, request
import sys
import pymysql.cursors
from base.paceplace.common import db
from base.paceplace.common.static_info import STATIC_INFO_DETAIL, STATIC_INFO_TYPE

ap = app.app

USER_INFO = dict()
result = {}

my_connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                user=db.MYSQL_DATABASE_USER,
                                password=db.MYSQL_DATABASE_PASSWORD,
                                db=db.MYSQL_DATABASE_DB,
                                charset='utf8mb4',
                                cursorclass=pymysql.cursors.DictCursor)


@ap.route('/getUserCourses/', methods=['GET', 'POST'])
def get_user_courses():
    content = request.json
    try:
        if content:
            user_id = content['USER_ID']
        else:
            user_id = request.form['USER_ID']

        print(user_id)
        # print(STATIC_INFO_TYPE['GENDER'][gender])
    except KeyError:
        print("User Name or Password is missing in POSTed JSON ")
        return jsonify({"KeyError": "user_name or password is missing"})
        sys.exit(0)

    response = True
    # static_combo_type_query = "SELECT * from user_info"
    #
    # if user_name and password:
    #     static_combo_type_query += " where email='" + user_name + "' and password='" + password + "'"
    #
    # get_user(static_combo_type_query)
    # response = False
    # global result
    # if result:
    #     response = True
    return jsonify({"RESPONSE": response, "DATA": result})
