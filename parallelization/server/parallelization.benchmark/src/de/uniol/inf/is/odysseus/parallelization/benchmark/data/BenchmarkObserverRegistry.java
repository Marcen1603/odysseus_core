/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.parallelization.benchmark.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Registry for observers used in benchmark observer operator. If a observer is
 * registered it will be informed if the evaluation goal is reached
 * 
 * @author ChrisToenjesDeye
 *
 */
public class BenchmarkObserverRegistry {

	private static BenchmarkObserverRegistry instance;
	private Map<UUID, IBenchmarkObserver> observerMap = new HashMap<UUID, IBenchmarkObserver>();

	/**
	 * returns a shared instance of this registry
	 * @return instance of registry
	 */
	public static BenchmarkObserverRegistry getInstance() {
		if (instance == null) {
			instance = new BenchmarkObserverRegistry();
		}
		return instance;
	}

	/**
	 * registers a new observer with an uuid for identification
	 * @param uuid for identification 
	 * @param observer 
	 */
	public void registerObserver(UUID uuid, IBenchmarkObserver observer) {
		if (!observerMap.containsKey(uuid)) {
			observerMap.put(uuid, observer);
		}
	}

	/**
	 * unregisters an observer with a given uuid
	 * @param observable
	 * @param observerUUID
	 */
	public void unregisterObserver(BenchmarkPOObservable<?> observable,
			UUID observerUUID) {
		observerMap.remove(observerUUID);
	}

	/**
	 * returns all registered observer
	 * @return all observers
	 */
	public Map<UUID, IBenchmarkObserver> getObserverMap() {
		return observerMap;
	}

}
