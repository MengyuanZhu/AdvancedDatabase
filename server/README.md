
##Platform
Ubuntu 16.04

MongoDB 3.2.11

`sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927`

`echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.2 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.2.list`

`sudo apt-get update`

`sudo apt-get install -y mongodb-org`

Redis 3.2.4
Download from official website

Python 2.7.12

NPM 3.5.2 
`sudo apt-get install npm`

Java 1.8
Download from Oracle

npm-redis
`npm install ioredis`

npm-mongodb
`npm install mongodb`

python-redis
`sudo pip install redis`

##Start server

Start redis:

`redis-server`

Start mongoDB:

`sudo service mongod start`

##Server-side scripts

Generate pokemons:
`python generatePokemon.py`

Find pokemons in a region:
`node getPokemon.js ` 

Location test (work with getPokemon.js):
`java DataClient`

Edits pokemon by the player:
`node editPokemon.js`

