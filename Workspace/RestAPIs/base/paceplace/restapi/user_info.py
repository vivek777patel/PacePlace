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


def get_user(query=None):
    if query is None:
        return

    connection = my_connection
    try:

        with connection.cursor() as cursor:
            cursor.execute(query)
            global result
            result = {}
            result = cursor.fetchone()
    finally:
        connection.cursor().close()
    if result is None:
        print('no data')
    else:
        result['genderInt'] = int(result['gender'])
        result['gender'] = STATIC_INFO_DETAIL[int(result['gender'])]
        result['account_typeInt'] = int(result['account_type'])
        result['account_type'] = STATIC_INFO_DETAIL[int(result['account_type'])]
        result['graduation_typeInt'] = int(result['graduation_type'])
        result['graduation_type'] = STATIC_INFO_DETAIL[int(result['graduation_type'])]
        result['status_idInt'] = int(result['status_id'])
        result['status_id'] = STATIC_INFO_DETAIL[int(result['status_id'])]
        result['student_typeInt'] = int(result['student_type'])
        result['student_type'] = STATIC_INFO_DETAIL[int(result['student_type'])]
        result['subjectInt'] = int(result['subject'])
        result['subject'] = STATIC_INFO_DETAIL[int(result['subject'])]


# @ap.route('/checkUser/', methods=['GET', 'POST'])
@ap.route('/checkUser/', methods=['POST'])
def check_user():
    content = request.json
    try:
        if content:
            user_name = content['EMAIL']
        else:
            user_name = request.form['EMAIL']
    except KeyError:
        print("User Name or Password is missing in POSTed JSON ")
        return jsonify({"KeyError": "user_name or password is missing"})
        sys.exit(0)

    static_combo_type_query = "SELECT * from user_info"

    if user_name:
        static_combo_type_query += " where email='" + user_name + "'"

    get_user(static_combo_type_query)
    response = False
    global result
    if result:
        response = True
    return jsonify({"RESPONSE": response, "DATA": result})


@ap.route('/getUserInfo/', methods=['GET', 'POST'])
def get_user_info():
    content = request.json
    try:
        if content:
            user_name = content['EMAIL']
            password = content['PASSWORD']
        else:
            user_name = request.form['EMAIL']
            password = request.form['PASSWORD']

        # print(gender)
        # print(STATIC_INFO_TYPE['GENDER'][gender])
    except KeyError:
        print("User Name or Password is missing in POSTed JSON ")
        return jsonify({"KeyError": "user_name or password is missing"})
        sys.exit(0)

    static_combo_type_query = "SELECT * from user_info"

    if user_name and password:
        static_combo_type_query += " where email='" + user_name + "' and password='" + password + "'"

    get_user(static_combo_type_query)
    response = False
    global result
    if result:
        response = True
    return jsonify({"RESPONSE": response, "DATA": result})


@ap.route('/addUser/', methods=['GET', 'POST'])
def add_user():
    content = request.json
    try:
        if content:
            first_name = content['FIRST_NAME']
            last_name = content['LAST_NAME']
            user_name = content['EMAIL']
            dob = content['DOB']
            mobile = content['MOBILE']
            password = content['PASSWORD']
            status_id = STATIC_INFO_TYPE['STATUS']['Active']

            # Get int value from json directly
            # Hardcoded right now
            gender = STATIC_INFO_TYPE['GENDER']['Male']
            account_type = STATIC_INFO_TYPE['ACCOUNT_TYPE']['Student']
            graduation_type = STATIC_INFO_TYPE['GRADUATION_TYPE']['Graduate']
            subject = STATIC_INFO_TYPE['SUBJECT']['Computer Science']
            student_type = STATIC_INFO_TYPE['STUDENT_TYPE']['International']
        # else:
        #     user_name = request.form['EMAIL']
        #     password = request.form['PASSWORD']
    except KeyError:
        return jsonify({"KeyError": "key fields are missing is missing in POSTed JSON "})
        sys.exit(0)

    static_combo_type_query = "SELECT * from user_info"

    if user_name:
        static_combo_type_query += " where email='" + user_name + "'"
    get_user(static_combo_type_query)
    global result
    if result is not None:
        return jsonify({"RESPONSE": False, "DATA": "User Already Registered"})
    try:
        connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                     user=db.MYSQL_DATABASE_USER,
                                     password=db.MYSQL_DATABASE_PASSWORD,
                                     db=db.MYSQL_DATABASE_DB,
                                     charset='utf8mb4',
                                     cursorclass=pymysql.cursors.DictCursor)
        with connection.cursor() as cursor:
            # print("Executing Query 1")
            # Read a single record
            sql = "INSERT INTO user_info (firstname,last_name,email,dob,mobile,password,status_id, gender, " \
                  "account_type, graduation_type , subject, student_type) " \
                  "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
            cursor.execute(sql, (first_name, last_name, user_name, dob, mobile, password, status_id, gender,
                                 account_type, graduation_type, subject, student_type))
        connection.commit()
        response = True
        # print("Executing Query 2")
    except Exception as e:
        print("Exeception occurred :{}".format(e))
        connection.cursor().close()
        response = False
        return jsonify({"RESPONSE": response, "DATA": {"DATA": "Exception in User Registration"}})
    finally:
        # connection.close()
        connection.cursor().close()
    print("User Registration successful")
    return jsonify({"RESPONSE": response, "DATA": {"DATA": "User Registered successfully"}})
