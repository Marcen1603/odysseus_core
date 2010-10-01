package de.uniol.inf.is.odysseus.event;


public interface IEventListener {

	void eventOccured(IEvent<?,?> event);

}
