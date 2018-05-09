import pymysql.cursors
from flask import jsonify, request
import json
from base import app
from base.paceplace.common import db

ap = app.app

USER_INFO = dict()
result = {}
student_ratings = dict()
course_ratings = dict()
professor_ratings = dict()


@ap.route('/saveRegisteredCourses/', methods=['GET', 'POST'])
def save_registered_courses():
    global result
    result = {}
    content = request.json
    connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                 user=db.MYSQL_DATABASE_USER,
                                 password=db.MYSQL_DATABASE_PASSWORD,
                                 db=db.MYSQL_DATABASE_DB,
                                 charset='utf8mb4',
                                 cursorclass=pymysql.cursors.DictCursor)
    response = False
    try:
        print(content)
        if type(content) is str:
            content = json.loads(content)

        for li in content:
            print(li['USER_ID'])
            user_id = li['USER_ID']
            course_det_id = li['COURSE_DET_ID']
            connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                         user=db.MYSQL_DATABASE_USER,
                                         password=db.MYSQL_DATABASE_PASSWORD,
                                         db=db.MYSQL_DATABASE_DB,
                                         charset='utf8mb4',
                                         cursorclass=pymysql.cursors.DictCursor)
            with connection.cursor() as cursor:
                sql = "insert into student_course_map (user_id, course_det_id) values (%s, %s)"
                cursor.execute(sql, (user_id, course_det_id))

            connection.commit()
            response = True

    except KeyError:
        print("Key is missing in POSTed JSON ")
        return jsonify(str({"RESPONSE": response, "DATA": {"DATA": "Required key is missing in POSTed JSON of Course Registration"}}))
    except Exception as e:
        print("Exeception occurred :{}".format(e))
        connection.cursor().close()
        response = False
        return jsonify(str({"RESPONSE": response, "DATA": {"DATA": "Exception in Course Registration"}}))
    finally:
        # connection.close()
        connection.cursor().close()
    response = True
    print(str({"RESPONSE": response, "DATA": {"DATA": "CourseRegistered successfully"}}))
    return jsonify(str({"RESPONSE": response, "DATA": {"DATA": "CourseRegistered successfully"}}))


@ap.route('/getCoursesForRegistration/', methods=['GET', 'POST'])
def get_courses_for_registration():
    global result
    result = {}
    content = request.json
    try:
        if content:
            user_id = content['USER_ID']
        else:
            user_id = request.form['USER_ID']

        print(user_id)

    except KeyError:
        print("User Name or Password is missing in POSTed JSON ")
        return jsonify({"KeyError": "user_name or password is missing"})

    response = True

    query = """
        SELECT  cd.course_time,ci.course_name, ci.course_desc,
        li.location_name, li.address_line1, li.city,
        ui.firstname, ui.email,ui.user_id as professor_user_id,
        si_cd_course_type.static_combo_value, si_cd_course_day.static_combo_value,si_ci_graduation_type.static_combo_value, 
        si_ci_subject.static_combo_value,ci.course_id,cd.course_det_id
        
        FROM course_details cd join course_info ci on cd.course_id = ci.course_id
        join user_info ui on cd.user_id = ui.user_id
        join location_info li on cd.course_location = li.location_id
        join static_info si_cd_course_type on cd.course_type = si_cd_course_type.static_info_id
        join static_info si_cd_course_day on cd.course_day = si_cd_course_day.static_info_id
        join static_info si_ci_graduation_type on ci.graduation_type = si_ci_graduation_type.static_info_id
        join static_info si_ci_subject on ci.subject = si_ci_subject.static_info_id
        where cd.course_det_id not in (Select course_det_id from student_course_map scm
    """
    query += " where scm.user_id = " + str(user_id) + ")"
    print(query)
    connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                user=db.MYSQL_DATABASE_USER,
                                password=db.MYSQL_DATABASE_PASSWORD,
                                db=db.MYSQL_DATABASE_DB,
                                charset='utf8mb4',
                                cursorclass=pymysql.cursors.DictCursor)
    try:
        with connection.cursor() as cursor:
            print("Executing query to fetch student course details")
            cursor.execute(query)
            result = cursor.fetchall()
            print(result)
            response = True
            cursor.close()
    except Exception as e:
        print("Exeception occurred :{}".format(e))
        connection.cursor().close()
        response = False
        return jsonify({"RESPONSE": response, "DATA": {"DATA": "Exception in User Registration"}})
    finally:
        connection.cursor().close()

    if result:
        print("Sending response back with data " + str(result))
        return jsonify({"RESPONSE": response, "DATA": result})
    else:
        result = []
        print("Sending response back with blank data")
        return jsonify({"RESPONSE": response, "DATA": str(result)})


@ap.route('/getUserCourses/', methods=['GET', 'POST'])
def get_user_courses():
    global result
    result = {}
    content = request.json
    try:
        if content:
            user_id = content['USER_ID']
            print("json")
        else:
            user_id = request.form['USER_ID']
            print("form")

        print(user_id)
        # print(STATIC_INFO_TYPE['GENDER'][gender])
    except KeyError:
        print("USER_ID POSTed JSON ")
        return jsonify({"KeyError": "user_name or password is missing"})

    response = True

    query = """
        SELECT cd.course_day, DATE_FORMAT(cd.course_startdate,'%b %d, %Y') as course_startdate, DATE_FORMAT(cd.course_enddate,'%b %d, %Y') as course_enddate, cd.course_time,cd.seat_available,cd.seat_capacity,
        ci.course_name, ci.course_desc, IFNULL(ci.course_rating,0) as course_rating, IFNULL(ci.number_of_raters,0) as number_of_raters, ci.credit,
        li.location_name, li.address_line1, li.city,
        ui.firstname, ui.email,ui.user_id as professor_user_id,
        si_cd_course_type.static_combo_value, si_cd_course_day.static_combo_value,
        si_ci_graduation_type.static_combo_value, si_ci_subject.static_combo_value,
        IFNULL(scm.course_ratings,0) as student_course_rating, IFNULL(scm.professor_ratings,0) as student_professor_rating,
        IFNULL(pr.rate,0) as professor_ratings, IFNULL(pr.no_of_raters,0) as professor_raters,
        ci.course_id, scm.scm_id, pr.prof_rate_id
        FROM student_course_map scm join course_details cd on scm.course_det_id = cd.course_det_id
        join course_info ci on cd.course_id = ci.course_id
        join user_info ui on cd.user_id = ui.user_id
        join location_info li on cd.course_location = li.location_id
        join static_info si_cd_course_type on cd.course_type = si_cd_course_type.static_info_id
        join static_info si_cd_course_day on cd.course_day = si_cd_course_day.static_info_id
        join static_info si_ci_graduation_type on ci.graduation_type = si_ci_graduation_type.static_info_id
        join static_info si_ci_subject on ci.subject = si_ci_subject.static_info_id
        left join professor_rater pr on ui.user_id = pr.user_id
    """
    query += " where scm.user_id = " + str(user_id)
    print(query)
    connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                user=db.MYSQL_DATABASE_USER,
                                password=db.MYSQL_DATABASE_PASSWORD,
                                db=db.MYSQL_DATABASE_DB,
                                charset='utf8mb4',
                                cursorclass=pymysql.cursors.DictCursor)
    try:
        with connection.cursor() as cursor:
            print("Executing query to fetch student course details")
            cursor.execute(query)
            result = cursor.fetchall()
            print({"RESPONSE": response, "DATA": result})
            response = True
            cursor.close()
    except Exception as e:
        print("Exeception occurred :{}".format(e))
        connection.cursor().close()
        response = False
        return jsonify({"RESPONSE": response, "DATA": {"DATA": "Exception in User Registration"}})
    finally:
        connection.cursor().close()

    if result:
        print("Sending response back with data" + str(result))
        return jsonify({"RESPONSE": response, "DATA": result})
    else:
        result = []
        print("Sending response back with blank data" + str(result))
        return jsonify({"RESPONSE": response, "DATA": result})


def update_student_ratings(student_course_rating, student_prof_rating,student_course_id):
    global student_ratings
    try:
        connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                     user=db.MYSQL_DATABASE_USER,
                                     password=db.MYSQL_DATABASE_PASSWORD,
                                     db=db.MYSQL_DATABASE_DB,
                                     charset='utf8mb4',
                                     cursorclass=pymysql.cursors.DictCursor)
        with connection.cursor() as cursor:
            sql = "UPDATE student_course_map set course_ratings=%s, professor_ratings=%s where scm_id=%s"
            cursor.execute(sql, (student_course_rating, student_prof_rating,student_course_id))

        connection.commit()
        response = True
    except Exception as e:
        print("Exeception occurred :{}".format(e))
        connection.cursor().close()
        response = False

        student_ratings["RESPONSE"] = response
        student_ratings["DATA"] = {"DATA": "Exception in User Registration"}
        return jsonify({"RESPONSE": response, "DATA": {"DATA": "Exception in User Registration"}})
    finally:
        # connection.close()
        connection.cursor().close()

    student_ratings["RESPONSE"] = response
    student_ratings["DATA"] = {"DATA": "User Registered successfully"}
    return jsonify({"RESPONSE": response, "DATA": {"DATA": "User Registered successfully"}})


def update_course_ratings(overall_course_rating, overall_course_raters, course_id):
    global course_ratings
    try:
        connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                     user=db.MYSQL_DATABASE_USER,
                                     password=db.MYSQL_DATABASE_PASSWORD,
                                     db=db.MYSQL_DATABASE_DB,
                                     charset='utf8mb4',
                                     cursorclass=pymysql.cursors.DictCursor)
        with connection.cursor() as cursor:
            sql = "UPDATE course_info set course_rating=%s, number_of_raters=%s where course_id=%s"
            cursor.execute(sql, (overall_course_rating, overall_course_raters, course_id))

        connection.commit()
        response = True
    except Exception as e:
        print("Exeception occurred :{}".format(e))
        connection.cursor().close()
        response = False
        course_ratings["RESPONSE"] = response
        course_ratings["DATA"] = {"DATA": "Exception in User Registration"}
        return jsonify({"RESPONSE": response, "DATA": {"DATA": "Exception in User Registration"}})
    finally:
        # connection.close()
        connection.cursor().close()
    course_ratings["RESPONSE"] = response
    course_ratings["DATA"] = {"DATA": "User Registered successfully"}
    return jsonify({"RESPONSE": response, "DATA": {"DATA": "User Registered successfully"}})


def update_professor_ratings(overall_prof_rating, overall_prof_raters, prof_rate_id, prof_user_id):
    global professor_ratings
    try:
        connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                     user=db.MYSQL_DATABASE_USER,
                                     password=db.MYSQL_DATABASE_PASSWORD,
                                     db=db.MYSQL_DATABASE_DB,
                                     charset='utf8mb4',
                                     cursorclass=pymysql.cursors.DictCursor)
        with connection.cursor() as cursor:
            sql = "replace into professor_rater (prof_rate_id, user_id, rate, no_of_raters) values (%s,%s, %s,%s)"
            cursor.execute(sql, (prof_rate_id,prof_user_id,overall_prof_rating, overall_prof_raters))

        connection.commit()
        response = True
    except Exception as e:
        print("Exeception occurred :{}".format(e))
        connection.cursor().close()
        response = False
        professor_ratings["RESPONSE"] = response
        professor_ratings["DATA"] = {"DATA": "Exception in User Registration"}
        return jsonify({"RESPONSE": response, "DATA": {"DATA": "Exception in User Registration"}})
    finally:
        # connection.close()
        connection.cursor().close()

    professor_ratings["RESPONSE"] = response
    professor_ratings["DATA"] = {"DATA": "User Registered successfully"}
    return jsonify({"RESPONSE": response, "DATA": {"DATA": "User Registered successfully"}})


@ap.route('/saveUserRatings/', methods=['GET', 'POST'])
def save_user_ratings():
    content = request.json
    try:
        if content:
            student_course_rating = content['STUDENT_COURSE_RATING']
            overall_course_raters = content['OVERALL_COURSE_RATERS']
            overall_course_rating = content['OVERALL_COURSE_RATING']

            student_prof_rating = content['STUDENT_PROF_RATING']
            overall_prof_raters = content['OVERALL_PROF_RATERS']
            overall_prof_rating = content['OVERALL_PROF_RATING']

            course_id = content['COURSE_ID']
            student_course_id = content['STUDENT_COURSE_ID']
            prof_rate_id = content['PROF_RATE_ID']
            prof_user_id = content['PROF_USER_ID']
        else:
            student_course_rating = request.form['STUDENT_COURSE_RATING']
            overall_course_raters = request.form['OVERALL_COURSE_RATERS']
            overall_course_rating = request.form['OVERALL_COURSE_RATING']

            student_prof_rating = request.form['STUDENT_PROF_RATING']
            overall_prof_raters = request.form['OVERALL_PROF_RATERS']
            overall_prof_rating = request.form['OVERALL_PROF_RATING']

            course_id = request.form['COURSE_ID']
            student_course_id = request.form['STUDENT_COURSE_ID']
            prof_rate_id = request.form['PROF_RATE_ID']
            prof_user_id = request.form['PROF_USER_ID']

    except KeyError:
        print("User Name or Password is missing in POSTed JSON ")
        return jsonify({"KeyError": "user_name or password is missing"})

    update_student_ratings(student_course_rating, student_prof_rating,student_course_id)
    global student_ratings
    if student_ratings["RESPONSE"]:
        update_course_ratings(overall_course_rating, overall_course_raters, course_id)
        global course_ratings
        if course_ratings["RESPONSE"]:
            update_professor_ratings(overall_prof_rating, overall_prof_raters, prof_rate_id, prof_user_id)
            global professor_ratings
            return jsonify(professor_ratings)

