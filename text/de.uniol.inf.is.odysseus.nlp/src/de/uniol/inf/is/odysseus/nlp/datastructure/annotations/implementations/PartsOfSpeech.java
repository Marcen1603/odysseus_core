package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;

public class PartsOfSpeech extends Annotation {
	public static final String NAME = "partsofspeech";
	
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
	

	@Override
	public IClone clone(){
		return new PartsOfSpeech(tags.clone());
	}

}
