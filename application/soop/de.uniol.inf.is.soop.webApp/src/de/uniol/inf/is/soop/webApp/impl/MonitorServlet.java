package de.uniol.inf.is.soop.webApp.impl;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import com.google.gson.Gson;

import de.uniol.inf.is.soop.webApp.controlWsClient.SOOPControlWSPort;
import de.uniol.inf.is.soop.webApp.controlWsClient.SOOPControlWSProxy;

public class MonitorServlet extends WebSocketServlet {

	private static final long serialVersionUID = -9048812968644311964L;
	
	private static MonitorServlet instance = null; 

	private Map<String, MonitorWebSocket> connections = new ConcurrentHashMap<String, MonitorWebSocket>();
	private BlockingQueue<Event> queue = new LinkedBlockingQueue<Event>();
	private Thread broadcaster = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					Event event = queue.take();
					for (Entry<String, MonitorWebSocket> entry : connections.entrySet()) {
						try {
							send(entry.getValue(), event);
						} catch (IOException ex) {
							fire(new Event("close").socket(entry.getKey()));
						}
					}
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	});
	
	
	public static MonitorServlet getInstance(){
		if(instance  == null ) {
			instance = new MonitorServlet();
		}	
		return instance;
	}
	
	// obey the singleton
	private MonitorServlet(){}

	@Override
	public void init() throws ServletException {
		super.init();
		broadcaster.setDaemon(true);
		broadcaster.start();
	}

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		return new MonitorWebSocket(request.getParameter("id"));
	}

	private void send(MonitorWebSocket webSocket, Event event) throws IOException {
		String data = new Gson().toJson(event);
		webSocket.connection.sendMessage(data);
	}
	
	// let the Webservice push to the websocket
	public void pushData(String data){
		Event event = new Event ("message");
		HashMap<String, String> list = new LinkedHashMap<String, String>();
		list.put("message", data);
		list.put("username", "PushServer");
		
		event.data(list);
		fire(event);
	}
	
	private void fire(Event event) {
		if (event.type.equals("close")) {
			connections.remove(event.socket);
		}

		handle(event);
	}

	private void handle(Event event) {
		if (event.type.equals("message")) {
			queue.offer(new Event("message").data(event.data));
		}
	}

	private class MonitorWebSocket implements WebSocket.OnTextMessage {
		String id;
		String instanceId;
		String userToken;
		Connection connection;

		public MonitorWebSocket(String id) {
			this.id = id;
		}

		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;
			connections.put(id, this);
			fire(new Event("open").socket(id));
		}

		@Override
		public void onClose(int closeCode, String message) {
			fire(new Event("close").socket(id));
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onMessage(String data) {
			
			Event event = new Gson().fromJson(data, Event.class);
			
			if(event.data.toString().contains("setInstance:")){
				this.instanceId = ((Map<String, String>) event.data).get("message").replace("setInstance:", "");
			} else if(event.data.toString().contains("setToken:")){
				this.userToken = ((Map<String, String>) event.data).get("message").replace("setToken:", "");
			} else if(event.data.toString().contains("phaseChange")){
				SOOPControlWSProxy proxy = new SOOPControlWSProxy();
				SOOPControlWSPort port = proxy.getSOOPControlWS();
				try {
					port.forwardButtonPressed(this.userToken, this.instanceId);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				fire(event);
			}
		}
	}

	private static class Event {
		private String socket;
		private String type;
		private Object data;

		public Event() {

		}

		public Event(String type) {
			this.type = type;
		}

		public Event data(Object data) {
			this.data = data;
			return this;
		}

		public Event socket(String socket) {
			this.socket = socket;
			return this;
		}
	}

}