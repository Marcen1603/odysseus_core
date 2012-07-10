/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.markov.model.algorithm;

import java.util.HashMap;
import java.util.Map;

public class MarkovAlgorithmDictionary {

	private static MarkovAlgorithmDictionary instance = null;

	private Map<String, Class<? extends IMarkovAlgorithm>> algorithms = new HashMap<String, Class<? extends IMarkovAlgorithm>>();

	private MarkovAlgorithmDictionary() {
		// intentionally left blank
	}

	public static synchronized MarkovAlgorithmDictionary getInstance() {
		if (instance == null) {
			instance = new MarkovAlgorithmDictionary();
		}
		return instance;
	}

	public void addAlgorithm(String name, Class<? extends IMarkovAlgorithm> algorithm) {
		name = name.toLowerCase();
		this.algorithms.put(name, algorithm);
	}

	public Class<? extends IMarkovAlgorithm> getAlgorithmClass(String name) {
		name = name.toLowerCase();
		return this.algorithms.get(name);
	}

	public IMarkovAlgorithm createNewAlgorithm(String name) {
		name = name.toLowerCase();
		Class<? extends IMarkovAlgorithm> classe = this.algorithms.get(name);
		try {
			return classe.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean exists(String name){
		name = name.toLowerCase();
		return this.algorithms.containsKey(name);
	}

}
