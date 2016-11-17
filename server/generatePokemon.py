import redis
import string
import random
import json
import threading
import sched
import time
import datetime

r = redis.Redis()
activePokemon={} #to store pokemons name_id and timestamp
mutex = threading.Lock() 

#read pokemons data
with open('pokemons.json') as data_file:    
    pokemonData = json.load(data_file)

#generate unique ID for each pokemon
def idGenerator(size=6, chars=string.ascii_uppercase + string.digits):
	return ''.join(random.choice(chars) for _ in range(size))

#delete a pokemon from the database
def delPokemon(location, pokemonName):
	r.zrem(location, pokemonName)

#add a random pokemon location data to redis
def newPokemon():
	location="atlanta"
	longitude=33.7490
	latitude=84.3880
	pokemonName=pokemonData["pokemon"][random.randint(0,150)]["name"]+"_"+idGenerator()
	mutex.acquire()
	global activePokemon	
	r.geoadd(location, longitude+random.random()*0.1-0.05,latitude+random.random()*0.1-0.05,pokemonName);
	activePokemon[pokemonName]=datetime.datetime.now()+datetime.timedelta(0,600)
	mutex.release()

#javascript setinterval python version
def setInterval(func, sec):
	def func_wrapper():
		setInterval(func, sec) 
		func()  
	t = threading.Timer(sec, func_wrapper)	
	t.start()
	return t

#check the activePokemon stamps and delete expired pokemons
def checkRemovePokemon():
	mutex.acquire()
	global activePokemon	
	now=datetime.datetime.now()
	
	for key, value in activePokemon.items():
		if value<now:
			delPokemon("atlanta", key)
			del activePokemon[key]	
	mutex.release()

setInterval(newPokemon, 0.1)
setInterval(checkRemovePokemon, 1)













