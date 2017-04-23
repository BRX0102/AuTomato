import json

data1 = """ { 'humidity' :
'21'

}"""
data1 = data1.replace("\n", " ")
data1 = data1.replace("'", "\"")

newData = json.loads(data1)

test = json.dumps(newData)

print type(test)
