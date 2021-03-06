from base import app
from flask import jsonify

ap = app.app
mysql = app.mysql

STATIC_INFO = dict()
STATIC_INFO_DET = dict()
STATIC_INFO_DETAIL = dict()
STATIC_INFO_TYPE = dict()


def get_static_info(combo_type=''):

    global STATIC_INFO
    STATIC_INFO = dict()
    global STATIC_INFO_DET
    STATIC_INFO_DET = dict()
    global STATIC_INFO_DETAIL
    STATIC_INFO_DETAIL = dict()

    cursor = mysql.connect().cursor()
    static_combo_type_query = "SELECT * from static_info"
    if combo_type:
        static_combo_type_query += " where static_combo_type='" + combo_type + "'"
    cursor.execute(static_combo_type_query)
    data = []
    data = cursor.fetchall()

    if data is None:
        print('no data')
    else:
        for i in range(len(data)):
            if data[i][1] in STATIC_INFO:
                STATIC_INFO[data[i][1]].append((data[i][0], data[i][2]))
                STATIC_INFO_TYPE[data[i][1]].update({data[i][2]: data[i][0]})
            else:
                STATIC_INFO[data[i][1]] = [(data[i][0], data[i][2])]
                STATIC_INFO_TYPE[data[i][1]] = {data[i][2]: data[i][0]}

            if data[i][0] in STATIC_INFO:
                STATIC_INFO_DET[data[i][0]].append((data[i][1], data[i][2]))
                STATIC_INFO_DETAIL[data[i][0]].append(data[i][2])
            else:
                STATIC_INFO_DET[data[i][0]] = [(data[i][1], data[i][2])]
                STATIC_INFO_DETAIL[data[i][0]] = data[i][2]

        print(STATIC_INFO_TYPE)


@ap.route('/static_info/', methods=['GET', 'POST'])
@ap.route('/static_info/<string:combo_type>/')
def static_info(combo_type=''):
    get_static_info(combo_type)
    response = False
    global STATIC_INFO
    if STATIC_INFO:
        response = True
    return jsonify({"RESPONSE": response, "DATA": STATIC_INFO})
    # return jsonify(STATIC_INFO)


get_static_info()