package de.uniol.inf.is.odysseus.p2p.operatorpeer.listener;

import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEvent;

public interface IP2PPOEventListener extends IPOEventListener {
	
	void sendEvent(POEvent poEvent);
	
}
