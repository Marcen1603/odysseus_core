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
package de.uniol.inf.is.odysseus.markov.model;

import java.util.HashMap;
import java.util.Map;

public class HiddenMarkovModelDictionary {
	
	private static HiddenMarkovModelDictionary instance = null;
	
	private Map<String, HiddenMarkovModel> models = new HashMap<String, HiddenMarkovModel>();
	
	private HiddenMarkovModelDictionary(){
		
	}
	
	public static synchronized HiddenMarkovModelDictionary getInstance(){
		if(instance == null){
			instance = new HiddenMarkovModelDictionary();
		}		
		return instance;
	}
	
	public void addHMM(String name, HiddenMarkovModel hmm){
		name = name.toUpperCase();
		this.models.put(name, hmm);
	}
	
	public HiddenMarkovModel getHMM(String name){
		name = name.toUpperCase();
		return this.models.get(name);
	}

}
