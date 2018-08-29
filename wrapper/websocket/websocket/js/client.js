/**
 * 
 */
function wsconnect(port) {
	let wsocket = new WebSocket("ws://localhost:" + port);
	wsocket.binaryType = 'arraybuffer';
	let decoder = new TextDecoder('UTF-8');
	wsocket.onmessage = function(m) {
		console.log(decoder.decode(m.data))
	};
	return wsocket;
}