from base import app
from flask import jsonify, request
import sys
import pymysql.cursors
from base.paceplace.common import db

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

    query = """
        SELECT cd.course_day, cd.course_startdate, cd.course_enddate, cd.course_time,cd.seat_available,cd.seat_capacity,
        ci.course_name, ci.course_desc, IFNULL(ci.course_rating,0) as course_rating, IFNULL(ci.number_of_raters,0) as number_of_raters, ci.credit,
        li.location_name, li.address_line1, li.city,
        ui.firstname, ui.email,
        si_cd_course_type.static_combo_value, si_cd_course_day.static_combo_value,
        si_ci_graduation_type.static_combo_value, si_ci_subject.static_combo_value
        
        FROM student_course_map scm, course_details cd , course_info ci, user_info ui, location_info li, static_info si_cd_course_type, static_info si_cd_course_day,
        static_info si_ci_graduation_type, static_info si_ci_subject
        where 
        scm.course_det_id = cd.course_det_id
        and cd.course_id = ci.course_id
        and cd.user_id = ui.user_id
        and cd.course_location = li.location_id
        and cd.course_type = si_cd_course_type.static_info_id
        and cd.course_day = si_cd_course_day.static_info_id
        and ci.graduation_type = si_ci_graduation_type.static_info_id
        and ci.subject = si_ci_subject.static_info_id
    """
    query += " and scm.user_id = " + str(user_id)
    print(query)
    connection = my_connection
    try:
        with connection.cursor() as cursor:
            print("Executing query to fetch student course_ details")
            cursor.execute(query)
            global result
            result = cursor.fetchall()
            response = True
    except Exception as e:
        print("Exeception occurred :{}".format(e))
        connection.cursor().close()
        response = False
        return jsonify({"RESPONSE": response, "DATA": {"DATA": "Exception in User Registration"}})
    finally:
        connection.cursor().close()

    if result:
        print("Sending response back with data")
        return jsonify({"RESPONSE": response, "DATA": result})
    else:
        result = {}
        print("Sending response back with blank data")
        return jsonify({"RESPONSE": response, "DATA": result})
