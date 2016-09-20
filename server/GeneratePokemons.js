var redis = require("redis"),
    client = redis.createClient();

client.on("error", function(error) {
    console.log(error);
});
//client.hmset(["dsbkey", "test keys 1", "test val 1", "test keys 2", "test val 2"], function (err, res) {});

var interval = setInterval(function() {
  client.hmset(["dsbkey", "test keys 1", "test val 1", "test keys 2", "test val 2", "test keys 3", "test val 3"], redis.print);
  setTimeout(function(){  client.hdel("dsbkey","test keys 2",   function (err, res) {}); }, 2000);
  console.log("dsb")
}, 1000);
//client.expire("dsbkey",3);
//

setTimeout(function(){
	client.quit();
	clearInterval(interval);
},5000)

