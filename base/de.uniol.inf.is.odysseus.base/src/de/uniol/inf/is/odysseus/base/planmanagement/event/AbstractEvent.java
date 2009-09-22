package de.uniol.inf.is.odysseus.base.planmanagement.event;

/**
 * This object is a base class for creating events.
 * 
 * @author Wolf Bauer
 * 
 * @param <SenderType>
 *            Type of the sender which could send this event.
 * @param <ValueType>
 *            Type of the value which this event could have.
 */
public abstract class AbstractEvent<SenderType, ValueType> implements
		IEvent<SenderType, ValueType> {

	/**
	 * ID that identifies this event. This ID should be unique.
	 */
	private String id;

	/**
	 * The value of this event.
	 */
	private ValueType value;

	/**
	 * The sender of this event.
	 */
	private SenderType sender;

	/**
	 * Creates a new event.
	 * 
	 * @param sender The sender of this event.
	 * @param id ID that identifies this event. This ID should be unique.
	 * @param value The value of this event.
	 */
	protected AbstractEvent(SenderType sender, String id, ValueType value) {
		this.id = id;
		this.sender = sender;
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.event.IEvent#getSender()
	 */
	@Override
	public SenderType getSender() {
		return this.sender;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.event.IEvent#getValue()
	 */
	@Override
	public ValueType getValue() {
		return this.value;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.event.IEvent#getID()
	 */
	@Override
	public String getID() {
		return this.id;
	}
}
