package windperformancercp.event;

/**
 * Defines an event which is send by an object and has a value.
 * 
 * @author Wolf Bauer
 * 
 * @param <SenderType>
 *            Type of the sender which could send this event.
 * @param <ValueType>
 *            Type of the value which this event could have.
 */
public interface IEvent<SenderType, ValueType> {
	
	public IEventType getEventType();

	/**
	 * Returns the value of this event.
	 * 
	 * @return The value of this event.
	 */
	public ValueType getValue();

	/**
	 * Returns the sender of this event.
	 * 
	 * @return The sender of this event.
	 */
	public SenderType getSender();
}
