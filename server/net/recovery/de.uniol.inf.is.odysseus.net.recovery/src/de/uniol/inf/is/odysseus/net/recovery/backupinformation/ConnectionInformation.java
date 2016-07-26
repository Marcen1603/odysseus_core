package de.uniol.inf.is.odysseus.net.recovery.backupinformation;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionInformation implements Serializable {

	private static final long serialVersionUID = 3940464045426383356L;

	private String senderId;

	private String receiverId;

	private String host;

	private int port;

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public static JSONObject toJSON(ConnectionInformation info) {
		return new JSONObject(info);
	}

	public static ConnectionInformation fromJSON(JSONObject json) throws JSONException {
		ConnectionInformation info = new ConnectionInformation();
		info.setSenderId(json.getString("senderId"));
		info.setReceiverId(json.getString("receiverId"));
		info.setHost(json.getString("host"));
		info.setPort(json.getInt("port"));
		return info;
	}

}