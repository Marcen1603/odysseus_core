package de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods;

public class EventMethodParameter {
	private final String name;
	private final String type;
	
	
	public EventMethodParameter(String type, String name) {
		this.name = name;
		this.type = type;
	}


	public String getName() {
		return name;
	}


	public String getType() {
		return type;
	}
	
	

}
