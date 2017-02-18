package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;

public class NamedEntities extends Annotation{
	private String type;
	private int[] namedEntities;
	
	public NamedEntities(String type, int[] namedEntities){
		this.type = type;
		this.namedEntities = namedEntities;
	}
	
	@Override
	public Object toObject() {
		return namedEntities;
	}
	
	public String getType(){
		return type;
	}

}
