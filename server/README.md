
##Platform
Ubuntu 16.04

##Start server

Start redis:
'$redis-server'

Start mongoDB:
'$sudo service mongod start'

##Server-side scripts

Generate pokemons:
'$python generatePokemon.py'

Find pokemons in a region:
'$node getPokemon.js ' 

Location test (work with getPokemon.js):
'$java DataClient'

Edits pokemon by the player:
'$node editPokemon.js'
