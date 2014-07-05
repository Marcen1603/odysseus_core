package de.uniol.inf.is.odysseus.sports.rest.dao;

public class PeerSocket {
	String ip;
	String port;
	
	public PeerSocket(String ip, String port) {
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
}
