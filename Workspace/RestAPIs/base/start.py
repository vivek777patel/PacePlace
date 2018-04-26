from flask_script import Manager

# import app
from base.app import app

# napp = app.app

# manager = Manager(napp)
manager = Manager(app)

if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0')
    # napp.run(debug=True)
