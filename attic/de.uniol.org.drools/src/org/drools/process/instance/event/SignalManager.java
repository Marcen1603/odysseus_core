package org.drools.process.instance.event;

import org.drools.runtime.process.EventListener;

public interface SignalManager {
	
	void signalEvent(String type, Object event);
	
	void signalEvent(long processInstanceId, String type, Object event);
	
	void addEventListener(String type, EventListener eventListener);
	
	void removeEventListener(String type, EventListener eventListener);

}
