import requests
import random

for i in range(0,2):
    r = requests.post('http://localhost:3000/add', data = {
        'name': 'Agustín',
        'address' : 'Av. Matta ' + str(random.randint(0,999)),
        'phone': str(random.randint(40, 50))
    })
    i+= 1

print(r.status_code, r.reason)
