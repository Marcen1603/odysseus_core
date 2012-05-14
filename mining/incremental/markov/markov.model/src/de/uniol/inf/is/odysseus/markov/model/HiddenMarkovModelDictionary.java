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
