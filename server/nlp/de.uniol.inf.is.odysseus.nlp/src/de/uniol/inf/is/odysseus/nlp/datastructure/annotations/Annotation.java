package de.uniol.inf.is.odysseus.nlp.datastructure.annotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.IClone;

public abstract class Annotation implements IAnnotation{
	protected Map<String, IAnnotation> annotations = new HashMap<>();

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
	
	protected Map<String, IAnnotation> cloneAnnotations(){
		Map<String, IAnnotation> cloneAnnotations = new HashMap<>();
		for(Entry<String, IAnnotation> entry : annotations.entrySet()){
			cloneAnnotations.put(entry.getKey(), (IAnnotation) entry.getValue().clone());
		}
		return cloneAnnotations;
	}


	@Override
	public abstract IClone clone();

}
