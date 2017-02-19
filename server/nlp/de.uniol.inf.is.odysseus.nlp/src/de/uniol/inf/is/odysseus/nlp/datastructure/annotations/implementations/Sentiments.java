package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;

public class Sentiments extends Annotation {
	private int[] sentimentsPerSentence;
	
	public Sentiments(int[] sentiments){
		sentimentsPerSentence = sentiments;
	}
	
	@Override
	public Object toObject() {
		return sentimentsPerSentence;
	}

}
