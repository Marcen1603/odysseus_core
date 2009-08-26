package de.uniol.inf.is.odysseus.base.planmanagement.event;


public abstract class AbstractEvent<SenderType, ValueType> implements IEvent<SenderType, ValueType> {

	private String id;

	private ValueType value;

	private SenderType sender;

	protected AbstractEvent(SenderType sender, String id, ValueType value) {
		this.id = id;
		this.sender = sender;
		this.value = value;
	}

	@Override
	public SenderType getSender() {
		return this.sender;
	}
	
	@Override
	public ValueType getValue() {
		return this.value;
	}

	@Override
	public String getID() {
		return this.id;
	}
}
