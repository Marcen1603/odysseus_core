package de.uniol.inf.is.odysseus.base.planmanagement.event.error;

import de.uniol.inf.is.odysseus.base.planmanagement.event.AbstractEvent;

public class ErrorEvent extends AbstractEvent<IErrorEventHandler, Exception> {

	public static String ERROR = "ERROR";

	public ErrorEvent(IErrorEventHandler sender, String id, String message) {
		super(sender, id, new Exception(message));
	}

	public ErrorEvent(IErrorEventHandler sender, String id, Exception value) {
		super(sender, id, value);
	}

	public String getMessage() {		
		return getValue() != null ? getValue().getMessage() : "";
	}
}
