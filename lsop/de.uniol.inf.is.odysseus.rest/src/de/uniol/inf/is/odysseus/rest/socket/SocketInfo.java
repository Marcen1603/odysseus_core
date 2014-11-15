package de.uniol.inf.is.odysseus.rest.socket;

import java.util.List;



public class SocketInfo {
	private String ip;
	private int port;
	private List<AttributeInformation> schema;
	
	public SocketInfo(){
		
	}
	
	public SocketInfo(String ip, int port, List<AttributeInformation> schema) {
		this.ip = ip;
		this.port = port;
		this.schema = schema;
	}
	
	

	public List<AttributeInformation> getSchema() {
		return schema;
	}

	public void setSchema(List<AttributeInformation> schema) {
		this.schema = schema;
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
