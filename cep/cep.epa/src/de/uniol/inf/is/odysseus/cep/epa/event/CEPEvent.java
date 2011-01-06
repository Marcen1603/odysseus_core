package de.uniol.inf.is.odysseus.cep.epa.event;

import java.util.EventObject;

/**
 * This class defines a CEPEvent.
 * 
 * @author Christian
 */
@SuppressWarnings("serial")
public class CEPEvent extends EventObject {

	// type constants
	public static final int ADD_MASCHINE = 0;
	public static final int CHANGE_STATE = 1;
	public static final int MACHINE_ABORTED = 2;

	// the type of this event
	private int type;
	// the content of this event
	private Object content;

	/**
	 * This is the constructor of this class.
	 * 
	 * @param source
	 *            is the listener to be notified by this event
	 * @param type
	 *            is the type of this event
	 * @param content
	 *            is the content to be saved
	 */
	public CEPEvent(ICEPEventListener source, int type, Object content) {
		super(source);
		this.type = type;
		this.content = content;
	}

	/**
	 * This is the getter for the type of this event
	 * 
	 * @return the event type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * This is the getter for the content of this event
	 * 
	 * @return the content of this event
	 */
	public Object getContent() {
		return this.content;
	}

}
