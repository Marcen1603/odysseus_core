package de.uniol.inf.is.odysseus.parallelization.benchmark.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.UUID;

public class BenchmarkObserverRegistry {

	private static BenchmarkObserverRegistry instance;
	private Map<UUID, Observer> observerMap = new HashMap<UUID, Observer>();

	public static BenchmarkObserverRegistry getInstance() {
		if (instance == null) {
			instance = new BenchmarkObserverRegistry();
		}
		return instance;
	}

	public void registerObserver(UUID uuid, Observer observer) {
		if (!observerMap.containsKey(uuid)) {
			observerMap.put(uuid, observer);
		}
	}

	public void unregisterObserver(BenchmarkPOObservable observable,
			UUID observerUUID) {
		if (observable != null) {
			observable.removeObserver(observerUUID);
		}
		observerMap.remove(observerUUID);
	}

	public Map<UUID, Observer> getObserverMap() {
		return observerMap;
	}

}
