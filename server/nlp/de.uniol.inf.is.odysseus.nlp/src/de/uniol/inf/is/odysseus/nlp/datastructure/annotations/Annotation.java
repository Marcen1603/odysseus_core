package de.uniol.inf.is.odysseus.nlp.datastructure.annotations;

import java.util.HashMap;
import java.util.Map;

public abstract class Annotation implements IAnnotation{
	private Map<String, IAnnotation> annotations = new HashMap<>();

	@Override
	public Map<String, IAnnotation> getAnnotations() {
		return annotations;
	}
	
	@Override
	public String identifier() {
		return getClass().getSimpleName().toLowerCase();
	}
	
	@Override
	public void put(Annotation annotation) {
		getAnnotations().put(annotation.identifier(), annotation);
	}


}
