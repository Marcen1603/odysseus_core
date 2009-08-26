package de.uniol.inf.is.odysseus.p2p.operatorpeer.listener;

import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;

public interface IP2PPOEventListener extends POEventListener {
	
	void sendEvent(POEvent poEvent);
	
}
