var Redis = require('ioredis');
var net = require('net');
var fs = require('fs');


var json = JSON.parse(fs.readFileSync('./pokemons.json', 'utf8'));
var redis = new Redis();
var HOST = 'target.gsu.edu';
var PORT = 6969;

net.createServer(function(sock) {
    
    // We have a connection - a socket object is assigned to the connection automatically
    console.log('CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);
    
    // Add a 'data' event handler to this instance of socket
    sock.on('data', function(data) {
        
        console.log('DATA ' + sock.remoteAddress + ': ' + data);
	var locationData = data.toString().substring(0, data.length - 1).split(",");
	console.log(locationData);
	redis.georadius(locationData[0], locationData[1], locationData[2], 50, 'm', "WITHCOORD", function (err, result) {
 		console.log(result);
		sock.write(result+"\n");
	});
        // Write the data back to the socket, the client will receive it as data from the server
        
        
    });
    
    // Add a 'close' event handler to this instance of socket
    sock.on('close', function(data) {
        console.log('CLOSED: ' + sock.remoteAddress +' '+ sock.remotePort);
    });
    
}).listen(PORT, HOST);

console.log('Server listening on ' + HOST +':'+ PORT);
