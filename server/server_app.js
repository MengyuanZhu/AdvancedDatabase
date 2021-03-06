const MongoClient = require('mongodb').MongoClient,
    assert = require('assert');
const crypto = require('crypto');
const net = require('net');

//pokemon users database
var url = 'mongodb://localhost:27017/users';

//insert a pokemon to the player
var insertPokemon = function(db, username, pokemonName, hp, weight, type, height, attack, defense, callback) {
    var collection = db.collection(username);
    collection.insert({
        "pokemon": pokemonName,"hp": parseInt(hp), "weight":weight, "type":type,"height": height,"attack": attack, "defense": defense
    }, function(err, result) {
        assert.equal(err, null);
        callback(0);
    });
}

//list all pokemons player has
var listPokemons = function(db, username, callback) {
    var collection = db.collection(username);
    collection.find({
        "pokemon": {
            '$exists': 1
        }
    },
    {"_id":0}).toArray(function(err, docs) {
        assert.equal(err, null);
        callback(JSON.stringify(docs))
    });
}

//delete a pokemon
var removePokemon = function(db, username, pokemonName,callback) {
    var collection = db.collection(username);
    collection.remove({
        "pokemon": pokemonName
    }, function(err, docs) {
        assert.equal(err, null);
        callback(0);
    });
}

//update a pokemon
var updatePokemon = function(db, username, pokemonName,  callback) {
    var collection = db.collection(username);
    collection.update({
        "pokemon": pokemonName
    }, {
        $inc: {
            "hp": 1
        }
    }, function(err, docs) {
        assert.equal(err, null);
        callback(0);
    });
}

var registerUser = function(db, username, password, callback) {
    var collection = db.collection(username);
    collection.find({
        "password": {
            '$exists': 1
        }
    }, {
        _id: 0,
        name: 0
    }).toArray(function(err, docs) {
        assert.equal(err, null);

        // verify whether the the username has been registered
        if (docs.length > 0) {
            callback(1); // username does exist
            return;
        } else {
            //mzhu7 is the salt
            crypto.pbkdf2(password, 'mzhu7', 100, 512, 'sha512', (err, key) => {
                if (err) throw err;
                db.createCollection(username);
                collection = db.collection(username);
                collection.insert({
                    "name": username,
                    "password": key.toString("hex")
                }, function(err, result) {
                    assert.equal(err, null);
                    callback(0);
                });
            })
        };
    });
}

var login = function(db, username, password, callback) {
    var collection = db.collection(username);
    collection.find({
        "password": {
            '$exists': 1
        }
    }, {
        _id: 0,
        name: 0
    }).toArray(function(err, docs) {
        assert.equal(err, null);

        // verify whether the the username has been registered
        if (docs.length < 1) {
            callback(1); //Error: user does not exist
            return;
        } else {
            crypto.pbkdf2(password, 'mzhu7', 100, 512, 'sha512', (err, key) => {
                if (err) throw err;
                if (key.toString("hex") == docs[0]["password"])
                    callback(0); //password is correct
                else
                    callback(1); //password is wrong
            })
        };
    });
}

var userEvent = function(db, data, sock) {

    var clientData = data.toString().substring(0, data.length - 1).split(",");
    state = clientData[0]
    console.log(clientData)

    //state
    //0: show list
    //1: insert pokemon
    //2: delete pokemon
    //3: update pokemon
    //4: register
    //5: verification


    if (state == 0) {
        username = clientData[1];
        listPokemons(db, username, function(result) {
            console.log(result)
            sock.write(result+"\n");
        });

    } else if (state == 1) {
        username = clientData[1];
        pokemonName = clientData[2];
        hp = clientData[3];
        weight = clientData[4];
        type = clientData[5];
        height = clientData[6];
        attack = clientData[7];
        defense = clientData[8];
        insertPokemon(db, username, pokemonName, hp, weight, type, height, attack, defense, function(result){sock.write(result+"\n")});
    } else if (state == 2) {
        username = clientData[1]
        pokemonName = clientData[2]
        removePokemon(db, username, pokemonName, function(result) {
          sock.write(result+"\n");
        });
    } else if (state == 3) {
        username = clientData[1]
        pokemonName = clientData[2]
        updatePokemon(db, username, pokemonName, function(result) {
          sock.write(result+"\n");
        });
    } else if (state == 4) {
        username = clientData[1]
        password = clientData[2]
        registerUser(db, username, password, function(result) {
            sock.write(result+"\n");
        });
    } else if (state == 5) {
        username = clientData[1]
        password = clientData[2]
        login(db, username, password, function(result) {
            if (result == 0) {
                sock.write("Login success! \n");
            } else {
                sock.write("Your password is wrong! \n");
            }
        });
    }
}

//Use connect method to connect to the server
MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    console.log("Connected successfully to MongoDB");

    var HOST = 'target.gsu.edu';
    var PORT = 6970;

    net.createServer(function(sock) {

        // We have a connection - a socket object is assigned to the connection automatically
        console.log('CONNECTED: ' + sock.remoteAddress + ':' + sock.remotePort);

        // Add a 'data' event handler to this instance of socket
        sock.on('data', function(data) {
            userEvent(db, data, sock);
        });

        // Add a 'close' event handler to this instance of socket
        sock.on('close', function(data) {
            console.log('CLOSED: ' + sock.remoteAddress + ' ' + sock.remotePort);
        });

    }).listen(PORT, HOST);


});
