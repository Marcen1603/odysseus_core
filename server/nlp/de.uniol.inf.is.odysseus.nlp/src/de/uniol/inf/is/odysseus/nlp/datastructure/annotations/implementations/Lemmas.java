package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;

public class Lemmas extends Annotation {
	private String[] lemmas;

	public Lemmas(String[] lemmas){
		this.lemmas = lemmas;
	}
	
	@Override
	public Object toObject() {
		return lemmas;
	}

}