var redis = require("redis"),
    client = redis.createClient();

client.on("error", function(error) {
    console.log(error);
});



client.geoadd(atlanta, 33.7490, 84.3880, pikachu)
var interval = setInterval(function() {
  setTimeout(function(){  client.hdel("dsbkey","test keys 2",   function (err, res) {}); }, 2000);
  console.log("dsb")
}, 1000);


setTimeout(function(){
	client.quit();
	clearInterval(interval);
},5000)

