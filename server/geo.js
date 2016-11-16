var Redis = require('ioredis');
var redis = new Redis();
var fs = require('fs');
var json = JSON.parse(fs.readFileSync('./pokemons.json', 'utf8'));

function makeid(){
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 6; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

for (i=0;i<1;i++){
	pokemonName=json.pokemon[Math.floor((Math.random() * 150))].name+"_"+makeid();
	redis.geoadd('atlanta',33.7490+Math.random()*0.1-0.05,84.3880+Math.random()*0.1-0.05,pokemonName);
}

var longitude=33.7480
var latitude=84.3881

redis.georadius('atlanta', longitude, latitude, 200, 'm', "WITHCOORD", function (err, result) {
  console.log(result);
});


