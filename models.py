# models.py
import flask_sqlalchemy, app


# app.app = app module’s app variable
app.app.config['SQLALCHEMY_DATABASE_URI'] = 
 'postgresql://brandan:blockwood@localhost/postgres'
db = flask_sqlalchemy.SQLAlchemy(app.app)

class ChatMessage(db.Model):
 latiude = db.Column(db.Real)
 longitude = db.Column(db.Real)
 status = db.Column(db.Integer)
 

 def __init__(self, latiude,longitude,status):
    self.latiude = latiude
    self.longitude=longitude
    self.status=status

 def __repr__(self): # what's __repr__?
    return '<Message: %d %d %d>' % (self.latiude,self.longitude,self.status)