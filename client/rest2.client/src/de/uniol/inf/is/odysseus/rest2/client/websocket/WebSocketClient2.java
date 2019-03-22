package de.uniol.inf.is.odysseus.rest2.client.websocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketClient2 {

	public static void main(String[] args) throws URISyntaxException {

		WebSocketClient mWs = new WebSocketClient(new URI("ws://localhost:8888/queries/0/40242b3b-d6a0-4368-85c3-087e6fa76390/0/JSON/vnjd7be522p0kto4o3e0lcfap2")) {
			
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
