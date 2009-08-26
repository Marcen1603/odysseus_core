package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;

public interface IPOEventSender {
	public void subscribe(POEventListener listener, POEventType type);
	public void unsubscribe(POEventListener listener, POEventType type);
	public void subscribeToAll(POEventListener listener);
	public void unSubscribeFromAll(POEventListener listener);
}
