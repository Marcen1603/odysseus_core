package de.uniol.inf.is.odysseus.event.error;

import de.uniol.inf.is.odysseus.event.AbstractEvent;

/**
 * Defines an error event which could be send by an {@link IErrorEventHandler}.
 * 
 * @author Wolf Bauer
 * 
 */
public class ErrorEvent extends AbstractEvent<IErrorEventHandler, Exception> {

	private String message;

	/**
	 * Creates a new event.
	 * 
	 * @param sender The sender of this event.
	 * @param id ID that identifies this event. This ID should be unique.
	 * @param message The error message which is created.
	 */
	public ErrorEvent(IErrorEventHandler sender, ExceptionEventType eventType, String message, Exception e) {
		super(sender, eventType, e);
		this.message = message;
	}

	/**
	 * Creates a new event.
	 * 
	 * @param sender The sender of this event.
	 * @param id ID that identifies this event. This ID should be unique.
	 * @param value The {@link Exception} which created this event.
	 */
	public ErrorEvent(IErrorEventHandler sender, ExceptionEventType eventType, Exception value) {
		super(sender, eventType, value);
	}

	/**
	 * Return the error message of this event.
	 * 
	 * @return The error message of this event.
	 */
	public String getMessage() {
		return getValue() != null ? message+getValue().getMessage() : message;
	}
}
