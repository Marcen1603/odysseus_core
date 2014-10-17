package de.uniol.inf.is.odysseus.sports.rest.dto;

import java.util.List;



public class DistributedQuerySocketInfo {
	String ip;
	int port;
	List<AttributeInformation> attributeList;

	
	public DistributedQuerySocketInfo(){
		
	}
	
	public DistributedQuerySocketInfo(String ip, int port, List<AttributeInformation> attributeList) {
		this.ip = ip;
		this.port = port;
		this.attributeList = attributeList;
	}
	
	

	public List<AttributeInformation> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<AttributeInformation> attributeList) {
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
