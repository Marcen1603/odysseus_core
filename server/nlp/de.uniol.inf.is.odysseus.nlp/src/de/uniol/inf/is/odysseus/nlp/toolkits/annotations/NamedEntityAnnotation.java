package de.uniol.inf.is.odysseus.nlp.toolkits.annotations;

import java.util.List;

public class NamedEntityAnnotation implements IAnnotation {

	public static final String NAME = "namedentity";
	private List<String> namedEntities;
	

	public NamedEntityAnnotation(List<String> namedEntities) {
		this.namedEntities = namedEntities;
	}


	public List<String> getNamedEntities() {
		return namedEntities;
	}

}
