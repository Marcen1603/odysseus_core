package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;

public class Chunks extends Annotation  {
	public static final String NAME = "chunks";
	
	private String[] chunkTags;

	public Chunks(String[] chunkTags){
		this.chunkTags = chunkTags;
	}
	
	@Override
	public Object toObject() {
		return chunkTags;
	}

	@Override
	public IClone clone(){
		return new Chunks(chunkTags.clone());
	}
}
