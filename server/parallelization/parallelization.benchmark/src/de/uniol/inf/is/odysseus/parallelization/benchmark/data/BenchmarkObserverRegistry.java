package de.uniol.inf.is.odysseus.parallelization.benchmark.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BenchmarkObserverRegistry {

	private static BenchmarkObserverRegistry instance;
	private Map<UUID, IBenchmarkObserver> observerMap = new HashMap<UUID, IBenchmarkObserver>();

	public static BenchmarkObserverRegistry getInstance() {
		if (instance == null) {
			instance = new BenchmarkObserverRegistry();
		}
		return instance;
	}

	public void registerObserver(UUID uuid, IBenchmarkObserver observer) {
		if (!observerMap.containsKey(uuid)) {
			observerMap.put(uuid, observer);
		}
	}

	public void unregisterObserver(BenchmarkPOObservable<?> observable,
			UUID observerUUID) {
		observerMap.remove(observerUUID);
	}

	public Map<UUID, IBenchmarkObserver> getObserverMap() {
		return observerMap;
	}

}
