import redis
import string
import random
import json
import threading

r = redis.Redis()

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
	r.geoadd(location, longitude+random.random()*0.1-0.05,latitude+random.random()*0.1-0.05,pokemonName);
	t = threading.Timer(600, delPokemon, [location, pokemonName])
	t.start()

#javascript setinterval python version
def setInterval(func, sec):
	def func_wrapper():
		setInterval(func, sec) 
		func()  
	t = threading.Timer(sec, func_wrapper)
	t.start()
	return t

setInterval(newPokemon, 0.01)
