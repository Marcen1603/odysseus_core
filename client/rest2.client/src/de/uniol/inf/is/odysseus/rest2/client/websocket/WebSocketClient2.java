package de.uniol.inf.is.odysseus.rest2.client.websocket;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketClient2 {

	public static void main(String[] args) throws URISyntaxException {

		WebSocketClient mWs = new WebSocketClient(new URI("ws://localhost:8888/queries/0/egal/0/JSON/egal"),
				new Draft_6455()) {
			@Override
			public void onMessage(String message) {
				System.out.println(message);
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
