from flask_script import Manager

from base.app import app

manager = Manager(app)

if __name__ == "__main__":
    app.run(debug=True,host='0.0.0.0')