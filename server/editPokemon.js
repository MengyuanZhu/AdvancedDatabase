var MongoClient = require('mongodb').MongoClient
  , assert = require('assert');

var url = 'mongodb://localhost:27017/users';

//insert a pokemon to the player
var insertPokemon = function(db, username, pokemonName, hp, weight, type, height, attack, defense) {
  var collection = db.collection(username);
  collection.insert( {"pokemon" : [pokemonName, hp, weight, type, height, attack,defense]}, function(err, result) {
    assert.equal(err, null);
  });
}

//list all pokemons player has
var listPokemons = function(db, username,callback) {
  var collection = db.collection(username);
  collection.find({"pokemon":{'$exists': 1}}).toArray(function(err, docs) {
    assert.equal(err, null);
    callback(docs)    
  });
}

//delete a pokemon
var removePokemon = function(db, username,pokemonName) {
  var collection = db.collection(username);
  collection.remove({"pokemon.0":pokemonName},function(err, docs) {
    assert.equal(err, null);
  });
}

//update a pokemon
var updatePokemon = function(db, username, pokemonName, hp) {
  var collection = db.collection(username);
  collection.update({"pokemon.0":pokemonName},{$inc: {"pokemon.1":hp}},function(err, docs) {
    assert.equal(err, null);
  });
}

// Use connect method to connect to the server
MongoClient.connect(url, function(err, db) {
  assert.equal(null, err);
  console.log("Connected successfully to server");


  username="mzhu7"
  pokemonName="Pikachu"
  hp=500
  weight=10
  type="grass"
  height=1
  attack=100
  defense=100

  //state 0:show list, 1: insert pokemon, 2: delete pokemon, 3: update pokemon
  state=0
  
  if (state==0){
    listPokemons(db, username,function(result) {
      console.log(result)
    });
    
  }
  else if (state==1){
    insertPokemon(db, username, pokemonName, hp, weight, type, height, attack, defense)
  }
  else if (state==2){
    removePokemon(db, username, pokemonName);
  }
  else if (state==3){
    updatePokemon(db, username, pokemonName, 10);
  }



  db.close();
});




