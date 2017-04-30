# models.py
import flask_sqlalchemy, app


# app.app = app module's app variable
app.app.config['SQLALCHEMY_DATABASE_URI'] = app.os.getenv('DATABASE_URL')
#app.app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://brandan:blockwood@localhost/postgres'
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
 def json(self):
  return {
   'latitude':self.latiude,
   'longitude':self.longitude,
   'status':self.status
   }
   

 def __repr__(self): # what's __repr__?
    return '<latitude: %d, Longitude: %d,status: %d>' % (self.latiude,self.longitude,self.status)

class sensors(db.Model):
 _tablename_='sensors'
 id = db.Column(db.Integer, primary_key=True)  # key
 latiude = db.Column(db.Float)
 longitude = db.Column(db.Float)
 numOfWet = db.Column(db.Integer)
 
 def __init__(self,latiude,longitude,numOfWet):
    self.latiude = latiude
    self.longitude=longitude
    self.numOfWet=numOfWet
    
 def json(self):
  return {
   'latitude':self.latiude,
   'longitude':self.longitude,
   'status':self.numOfWet
   }
   

 def __repr__(self): # what's __repr__?
    return '<latitude: %d, Longitude: %d,status: %d>' % (self.latiude,self.longitude,self.numOfWet)
   