from base import app
from flask import jsonify

ap = app.app
mysql = app.mysql

STATIC_INFO = dict()

def get_static_info(combo_type=''):
    global STATIC_INFO
    STATIC_INFO = dict()
    cursor = mysql.connect().cursor()
    static_combo_type_query = "SELECT * from Static_info"
    if combo_type:
        static_combo_type_query += " where static_combo_type='" + combo_type + "'"
    cursor.execute(static_combo_type_query)
    data = []
    data = cursor.fetchall()

    if data is None:
        print('no data')
    else:
        for i in range(len(data)):
            print(data[i])
            if data[i][1] in STATIC_INFO:
                STATIC_INFO[data[i][1]].append((data[i][0], data[i][2]))
            else:
                STATIC_INFO[data[i][1]] = [(data[i][0], data[i][2])]

@ap.route('/static_info/')
@ap.route('/static_info/<string:combo_type>/')
def static_info(combo_type=''):
    get_static_info(combo_type)
    return jsonify(STATIC_INFO)

get_static_info()