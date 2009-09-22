package de.uniol.inf.is.odysseus.base.planmanagement.event.error;

import de.uniol.inf.is.odysseus.base.planmanagement.event.AbstractEvent;

/**
 * Defines an error event which could be send by an {@link IErrorEventHandler}.
 * 
 * @author Wolf Bauer
 * 
 */
public class ErrorEvent extends AbstractEvent<IErrorEventHandler, Exception> {

	/**
	 * This ID defines an error event
	 */
	public static String ERROR = "ERROR";

	/**
	 * Creates a new event.
	 * 
	 * @param sender The sender of this event.
	 * @param id ID that identifies this event. This ID should be unique.
	 * @param message The error message which is created.
	 */
	public ErrorEvent(IErrorEventHandler sender, String id, String message) {
		super(sender, id, new Exception(message));
	}

	/**
	 * Creates a new event.
	 * 
	 * @param sender The sender of this event.
	 * @param id ID that identifies this event. This ID should be unique.
	 * @param value The {@link Exception} which created this event.
	 */
	public ErrorEvent(IErrorEventHandler sender, String id, Exception value) {
		super(sender, id, value);
	}

	/**
	 * Return the error message of this event.
	 * 
	 * @return The error message of this event.
	 */
	public String getMessage() {
		return getValue() != null ? getValue().getMessage() : "";
	}
}
