package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;

public class NamedEntity extends Annotation{
	private String type;
	private Set<Span> namedEntities = new HashSet<>();
	
	public NamedEntity(String type){
		this.type = type;
	}
	
	public void add(Span span){
		namedEntities.add(span);
	}
	
	public Set<Span> getSpans(){
		return namedEntities;
	}
	
	@Override
	public Object toObject() {
		int[][] spans = new int[namedEntities.size()][2];
		int i = 0;
		for(Span span:namedEntities){
			spans[i] = span.toArray();
			i = i + 1;
		}
		return spans;
	}
	
	public String getType(){
		return type;
	}
	

	@Override
	public IClone clone(){
		NamedEntity entity = new NamedEntity(type);
		entity.namedEntities = new HashSet<Span>();
		for(Span span : namedEntities)
			entity.namedEntities.add((Span) span.clone());
		return entity;
	}

}
