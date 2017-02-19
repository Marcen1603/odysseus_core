package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;

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

}
