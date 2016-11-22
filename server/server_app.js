var MongoClient = require('mongodb').MongoClient
  , assert = require('assert');
const crypto = require('crypto');


//pokemon users database
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

var registerUser = function(db, username, password, callback) {
  var collection = db.collection(username);
  collection.find({"password":{'$exists': 1}},{_id:0,name:0}).toArray(function(err, docs) {
    assert.equal(err, null);

    // verify whether the the username has been registered
    if (docs.length>0) {
      callback(1);// username does exist
      return;
    }
    else{
      //mzhu7 is the salt
      crypto.pbkdf2(password, 'mzhu7', 100, 512, 'sha512', (err, key) => {
        if (err) throw err;
        db.createCollection(username);
        collection = db.collection(username);
        collection.insert( {"name" : username, "password":key.toString("hex")}, function(err, result) {
          assert.equal(err, null);
          console.log(password)
          callback(0);
        });
      })
    };
  });
}

var login = function(db, username, password, callback) {
  var collection = db.collection(username);
  collection.find({"password":{'$exists': 1}},{_id:0,name:0}).toArray(function(err, docs) {
    assert.equal(err, null);

   // verify whether the the username has been registered
    if (docs.length<1) {
      callback(1);//Error: user does not exist
      return;
    }
    else{
      crypto.pbkdf2(password, 'mzhu7', 100, 512, 'sha512', (err, key) => {
      if (err) throw err;
      if (key.toString("hex") ==docs[0]["password"])
        callback(0); //passwrod is correct
      else
        callback(1); //password is wrong
      })
    };
  });
}




//Use connect method to connect to the server
MongoClient.connect(url, function(err, db) {
  assert.equal(null, err);
  console.log("Connected successfully to server");

  username="mzhu6"
  password="123"
  pokemonName="Pikachu"
  hp=500
  weight=10
  type="grass"
  height=1
  attack=100
  defense=100



  //state
  //0: show list
  //1: insert pokemon
  //2: delete pokemon
  //3: update pokemon
  //4: register
  //5: verification
  state=5

  if (state==0){
    listPokemons(db, username,function(result) {
      console.log(result)
    });

  }
  else if (state==1){
    insertPokemon(db, username, pokemonName, hp, weight, type, height, attack, defense);
  }
  else if (state==2){
    removePokemon(db, username, pokemonName);
  }
  else if (state==3){
    updatePokemon(db, username, pokemonName, 10);
  }
  else if (state==4){
    registerUser(db, username, password, function(result){
	    console.log(result)} );
  }
  else if (state==5){
    login(db, username, password, function(result){
	    console.log(result)} );
  }





});
