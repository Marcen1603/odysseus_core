package de.uniol.inf.is.odysseus.rest2.common.model.events;

public class CompilerEvent {

	public String type;
	public String event;
	public String parserId;

	public CompilerEvent(String type, String event, String parserId) {
		this.type = type;
		this.event = event;
		this.parserId = parserId;
	}

}
