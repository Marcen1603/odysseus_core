package de.uniol.inf.is.odysseus.rest2.client.websocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketClient2 {

	public static void main(String[] args) throws URISyntaxException {

		WebSocketClient mWs = new WebSocketClient(new URI("ws://localhost:8888/queries/0/02300fc8-2116-47c5-873a-ecf68f776114/0/Binary/ce945i0iaknu4fite5keeiire7")) {
			
			@Override
			public void onMessage(String message) {
				System.out.println(message);
			}
			
			@Override
			public void onMessage(ByteBuffer message) {
				System.out.println("received ByteBuffer "+message);
			}
			
			@Override
			public void onOpen(ServerHandshake handshake) {
				System.out.println("opened connection");
			}

			@Override
			public void onClose(int code, String reason, boolean remote) {
				System.out.println("closed connection");
			}

			@Override
			public void onError(Exception ex) {
				ex.printStackTrace();
			}

		};
		mWs.connect();
	}

}
