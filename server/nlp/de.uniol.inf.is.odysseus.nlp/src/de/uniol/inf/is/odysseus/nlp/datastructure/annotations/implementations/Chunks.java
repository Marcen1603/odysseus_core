package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;

public class Chunks extends Annotation  {
	private Span[] chunks;

	public Chunks(Span[] chunks){
		this.chunks = chunks;
	}
	
	@Override
	public Object toObject() {
		int[][] chunkSpans = new int[chunks.length][2];
		for(int i = 0; i < chunks.length; i++){
			chunkSpans[i] = chunks[i].toArray();
		}
		return chunkSpans;
	}

}
