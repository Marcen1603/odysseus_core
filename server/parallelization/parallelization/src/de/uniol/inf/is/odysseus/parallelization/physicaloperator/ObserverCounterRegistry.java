package de.uniol.inf.is.odysseus.parallelization.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.UUID;

public class ObserverCounterRegistry {
	
	private static ObserverCounterRegistry instance;
	private Map<UUID, Observer> observerMap = new HashMap<UUID, Observer>();
	
	public static ObserverCounterRegistry getInstance(){
		if (instance == null){
			instance = new ObserverCounterRegistry();
		}
		return instance;
	}
	
	public void registerObserver(UUID uuid, Observer observer){
		
	}
	
	public void unregisterObserver(ObserverCounterPOHelper observable, UUID observerUUID){
		observable.removeObserver(observerUUID);
		observerMap.remove(observerUUID);
	}

	public Map<UUID, Observer> getObserverMap(){
		return observerMap;
	}

}
