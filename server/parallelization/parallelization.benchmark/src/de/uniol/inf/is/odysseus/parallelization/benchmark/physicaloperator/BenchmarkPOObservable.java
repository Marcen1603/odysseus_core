package de.uniol.inf.is.odysseus.parallelization.benchmark.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;


public class BenchmarkPOObservable extends Observable{
	private Map<UUID, Observer> observerMap;
	
	public BenchmarkPOObservable(){
		this.observerMap = new HashMap<UUID, Observer>();
		BenchmarkObserverRegistry registry = BenchmarkObserverRegistry.getInstance();
		observerMap = registry.getObserverMap();
	}
	
	public void updateObservers(){
		for (Observer observer : observerMap.values()) {
			observer.update(this, null);
		}
	}
	
	public void removeObserver(UUID observerUUID) {
		observerMap.remove(observerUUID);
	}
}
