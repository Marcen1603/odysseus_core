package de.uniol.inf.is.odysseus.physicaloperator.base.event;

import java.util.EventListener;

/**
 * 
 *  
 */

public interface IPOEventListener extends EventListener {
	public void poEventOccured(POEvent poEvent);
}