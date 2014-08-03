package de.uniol.inf.is.odysseus.sports.rest.dao;



public class SocketInfo {
	String ip;
	int port;
	
	public SocketInfo(){
		
	}
	
	public SocketInfo(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
