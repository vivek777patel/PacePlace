from base.paceplace.common import static_info
from base import app
from flask import jsonify, request
import sys
import pymysql.cursors
from base.paceplace.common import db
from base.paceplace.common.static_info import STATIC_INFO_DETAIL

ap = app.app



USER_INFO = dict()
result = []


@ap.route('/getUserInfo/', methods=['GET', 'POST'])
def get_user_info():
    print(STATIC_INFO_DETAIL)
    content = request.json
    try:
        if content:
            user_name = content['EMAIL']
            password = content['PASSWORD']
        else:
            user_name = request.form['EMAIL']
            password = request.form['PASSWORD']
            print(user_name)
    except KeyError:
        print("User Name or Password is missing in POSTed JSON ")
        return jsonify({"KeyError": "user_name or password is missing"})
        sys.exit(0)

    print(user_name+" SS "+password)
    global USER_INFO
    USER_INFO = dict()

    connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                 user=db.MYSQL_DATABASE_USER,
                                 password=db.MYSQL_DATABASE_PASSWORD,
                                 db=db.MYSQL_DATABASE_DB,
                                 charset='utf8mb4',
                                 cursorclass=pymysql.cursors.DictCursor)

    static_combo_type_query = "SELECT * from user_info"

    if user_name and password:
        static_combo_type_query += " where email='"+user_name+"' and password='"+password+"'"

    try:

        with connection.cursor() as cursor:
            cursor.execute(static_combo_type_query)
            global result
            result = []
            result = cursor.fetchone()
    finally:
        connection.close()
    if result is None:
        print('no data')
    else:
        result['gender'] = STATIC_INFO_DETAIL[int(result['gender'])]
        result['account_type'] = STATIC_INFO_DETAIL[int(result['account_type'])]
        result['graduation_type'] = STATIC_INFO_DETAIL[int(result['graduation_type'])]
        result['status_id'] = STATIC_INFO_DETAIL[int(result['status_id'])]
        result['student_type'] = STATIC_INFO_DETAIL[int(result['student_type'])]
        result['subject'] = STATIC_INFO_DETAIL[int(result['subject'])]

    return jsonify(result)
