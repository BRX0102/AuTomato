# models.py
import flask_sqlalchemy, app


# app.app = app module's app variable
app.app.config['SQLALCHEMY_DATABASE_URI'] = app.os.getenv('DATABASE_URL')
db = flask_sqlalchemy.SQLAlchemy(app.app)

#db.create_all()
class ClosedRoads(db.Model):
 __tablename__ = 'closedroads'
 id = db.Column(db.Integer, primary_key=True)  # key
 latiude = db.Column(db.Float)
 longitude = db.Column(db.Float)
 status = db.Column(db.Integer)
 

 def __init__(self, latiude,longitude,status):
    self.latiude = latiude
    self.longitude=longitude
    self.status=status

 def __repr__(self): # what's __repr__?
    return '<latiude: %d, Longitude: %d,status: %d>' % (self.latiude,self.longitude,self.status)