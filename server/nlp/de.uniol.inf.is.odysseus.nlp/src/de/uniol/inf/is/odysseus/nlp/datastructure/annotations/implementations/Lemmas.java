package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;

public class Lemmas extends Annotation {
	public static final String NAME = "lemmas";
	
	private String[] lemmas;

	public Lemmas(String[] lemmas){
		this.lemmas = lemmas;
	}
	
	public String[] getLemmas(){
		return lemmas;
	}
	
	@Override
	public Object toObject() {
		return lemmas;
	}

	@Override
	public IClone clone(){
		return new Lemmas(lemmas.clone());
	}
}