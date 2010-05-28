package de.uniol.inf.is.odysseus.p2p.operatorpeer.listener;

import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;

public interface IP2PPOEventListener extends IPOEventListener {
	
	void sendEvent(POEvent poEvent);
	
}
