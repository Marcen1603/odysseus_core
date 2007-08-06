package mg.dynaquest.queryexecution.object;

import mg.dynaquest.queryexecution.event.POEventType;

public class POElementBufferMaxCapacityReached extends POElementBufferEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2220848443738594531L;
	/**
	 * @uml.property  name="capacity"
	 */
	int capacity = -1;

	public POElementBufferMaxCapacityReached(POElementBuffer source,
			int capacity) {
		super(source);
		this.capacity = capacity;
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.POElementBufferMaxCapacityReached;
	}
}