import pymysql.cursors
from flask import jsonify, request

from base import app
from base.paceplace.common import db
from base.paceplace.common.static_info import STATIC_INFO_TYPE

ap = app.app

result = {}
location = {}
location_name = dict()


@ap.route('/getLocation/', methods=['GET', 'POST'])
def get_location_details():
    global location
    if location:
        return jsonify({"RESPONSE": True, "DATA": location})
    else:
        location = {}
        return jsonify({"RESPONSE": True, "DATA": location})


def get_location():
    global location
    location = {}
    global location_name

    query = """
            SELECT * from location_info
        """
    connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                 user=db.MYSQL_DATABASE_USER,
                                 password=db.MYSQL_DATABASE_PASSWORD,
                                 db=db.MYSQL_DATABASE_DB,
                                 charset='utf8mb4',
                                 cursorclass=pymysql.cursors.DictCursor)
    try:
        with connection.cursor() as cursor:
            cursor.execute(query)
            location = cursor.fetchall()
            print(location)
            cursor.close()
            for i in range(len(location)):
                if location[i]['location_name'] not in location_name:
                    location_name[location[i]['location_name']] = location[i]['location_id']
                # else:
                #     location_name[location[i]['location_name']] = location[i]['location_id']
            print(location_name)
    except Exception as e:
        print("Exeception occurred : {}".format(e))
        connection.cursor().close()
    finally:
        connection.cursor().close()


@ap.route('/addEvent/', methods=['GET', 'POST'])
def add_event():
    global location_name
    content = request.json
    try:
        if content:
            print("in Content JSON")
            event_name = content['EVENT_NAME']
            event_description = content['EVENT_DESCRIPTION']
            event_location = location_name[content['EVENT_LOCATION']]
            event_posted_by = content['EVENT_POSTED_BY']
            event_date = content['EVENT_DATE']
            if content['SUBJECT']:
                subject = STATIC_INFO_TYPE['SUBJECT'][content['SUBJECT']]
            else:
                subject = None

            if content['GRADUATION_TYPE']:
                graduation_type = STATIC_INFO_TYPE['GRADUATION_TYPE'][content['GRADUATION_TYPE']]
            else:
                graduation_type = None

        else:
            print("in Content FORM DATA")
            event_name = request.form['EVENT_NAME']
            event_description = request.form['EVENT_DESCRIPTION']
            event_location = location_name[request.form['EVENT_LOCATION']]
            event_posted_by = request.form['EVENT_POSTED_BY']
            event_date = request.form['EVENT_DATE']
            if request.form['GRADUATION_TYPE'] and request.form['GRADUATION_TYPE'] != 'GRADUATION_TYPE':
                graduation_type = STATIC_INFO_TYPE['GRADUATION_TYPE'][request.form['GRADUATION_TYPE']]
            else:
                graduation_type = None

            if request.form['SUBJECT'] and request.form['SUBJECT'] != 'SUBJECT':
                subject = STATIC_INFO_TYPE['SUBJECT'][request.form['SUBJECT']]
            else:
                subject = None

    except KeyError:
        print("ADD Events POSTED Fields Missing")
        return jsonify({"RESPONSE": False, "DATA": "key fields are missing is missing in POSTed JSON "})

    global result
    result = None

    try:
        connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                     user=db.MYSQL_DATABASE_USER,
                                     password=db.MYSQL_DATABASE_PASSWORD,
                                     db=db.MYSQL_DATABASE_DB,
                                     charset='utf8mb4',
                                     cursorclass=pymysql.cursors.DictCursor)
        with connection.cursor() as cursor:

            sql = "INSERT INTO event_info (event_name,event_desc,event_date,event_location," \
                  "event_postedby,event_for_gt , event_for_sub) " \
                  "VALUES (%s, %s, %s, %s, %s, %s, %s)"
            cursor.execute(sql, (event_name, event_description, event_date, event_location,
                                 event_posted_by, graduation_type, subject))
        connection.commit()
        response = True
        # print("Executing Query 2")
    except Exception as e:
        print("Exeception occurred :{}".format(e))
        connection.cursor().close()
        response = False
        return jsonify({"RESPONSE": response, "DATA": {"DATA": "Exception in Event Post"}})
    finally:
        connection.cursor().close()
    print("Event posted successful")
    return jsonify({"RESPONSE": response, "DATA": {"DATA": "Event posted successfully"}})


@ap.route('/getEventDetails/', methods=['GET', 'POST'])
def get_event_details():
    global result
    result = {}

    response = True

    query = """
        SELECT  ei.event_name, ei.event_desc, DATE_FORMAT(ei.event_date,'%b %d, %Y') as event_date,
        DAYNAME(ei.event_Date) as event_day,li.location_name, li.address_line1, li.city,ei.event_desc,
        ui.firstname,ui.user_id,si_ci_graduation_type.static_combo_value as grad_type,
        si_ci_subject.static_combo_value as subject
        FROM event_info ei join user_info ui on ei.event_postedby = ui.user_id
        join location_info li on ei.event_location = li.location_id
        left join static_info si_ci_graduation_type on ei.event_for_gt = si_ci_graduation_type.static_info_id
        left join static_info si_ci_subject on ei.event_for_sub = si_ci_subject.static_info_id
        where ei.event_date >= CURDATE() order by ei.event_Date, ei.event_name
    """
    print(query)
    connection = pymysql.connect(host=db.MYSQL_DATABASE_HOST,
                                 user=db.MYSQL_DATABASE_USER,
                                 password=db.MYSQL_DATABASE_PASSWORD,
                                 db=db.MYSQL_DATABASE_DB,
                                 charset='utf8mb4',
                                 cursorclass=pymysql.cursors.DictCursor)
    try:
        with connection.cursor() as cursor:
            print("Executing query to fetch Event Details")
            cursor.execute(query)
            result = cursor.fetchall()
            print(result)
            response = True
            cursor.close()
    except Exception as e:
        print("Exeception occurred :{}".format(e))
        connection.cursor().close()
        response = False
        return jsonify({"RESPONSE": response, "DATA": {"DATA": "Exception in Getting Event Data"}})
    finally:
        connection.cursor().close()

    if result:
        print("Sending response back with data")
        return jsonify({"RESPONSE": response, "DATA": result})
    else:
        result = {}
        print("Sending response back with blank data")
        return jsonify({"RESPONSE": response, "DATA": result})


get_location()
