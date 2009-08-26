package de.uniol.inf.is.odysseus.base.planmanagement.event;

public interface IEvent<SenderType, ValueType> {
	public String getID();
	
	public ValueType getValue();

	public SenderType getSender();
}
