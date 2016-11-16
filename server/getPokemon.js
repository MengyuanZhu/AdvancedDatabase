var Redis = require('ioredis');
var redis = new Redis();
var fs = require('fs');
var json = JSON.parse(fs.readFileSync('./pokemons.json', 'utf8'));

var longitude=33.7480
var latitude=84.3881

redis.georadius('atlanta', longitude, latitude, 200, 'm', "WITHCOORD", function (err, result) {
  console.log(result);
});


