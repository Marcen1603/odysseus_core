package de.uniol.inf.is.odysseus.sports.rest.dao;

import java.util.ArrayList;



public class PeerSocket {
	String ip;
	int port;
	ArrayList<AttributeInformation> attributeList;
	
	public PeerSocket(){
		
	}
	
	public PeerSocket(String ip, int port, ArrayList<AttributeInformation> attributeList) {
		this.ip = ip;
		this.port = port;
		this.attributeList = attributeList;
	}

	public ArrayList<AttributeInformation> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(ArrayList<AttributeInformation> attributeList) {
		this.attributeList = attributeList;
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
