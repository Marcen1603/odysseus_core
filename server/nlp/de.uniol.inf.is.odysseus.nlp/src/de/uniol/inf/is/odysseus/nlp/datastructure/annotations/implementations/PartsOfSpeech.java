package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;

public class PartsOfSpeech extends Annotation {
	private String[] tags;

	public PartsOfSpeech(String[] tags){
		this.tags = tags;
	}
	
	public String[] getTags(){
		return tags;
	}
	
	@Override
	public Object toObject() {
		return tags;
	}

}
