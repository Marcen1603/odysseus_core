package de.uniol.inf.is.odysseus.rest2.common.model.events;

public class ServerEvent {
	
	public String type;
	public String value;
	public String message;
	
	public ServerEvent(String type, String value, String message) {
		this.type = type;
		this.value = value;
		this.message = message;
	}
	

}
