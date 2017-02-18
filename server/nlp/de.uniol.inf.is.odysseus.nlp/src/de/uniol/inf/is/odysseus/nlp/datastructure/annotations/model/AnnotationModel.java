package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;

public abstract class AnnotationModel<T extends IAnnotation> implements IAnnotationModel<T>, Serializable {
	private static final long serialVersionUID = -5602583923531732056L;
	
	public final static String NAME = "model";
	protected Set<Class<? extends AnnotationModel<? extends IAnnotation>>> prerequisites = new HashSet<>();

	/**
	 * This constructor must be inherited.
	 */
	public AnnotationModel(){
		addRequirements();
	}
	
	/**
	 * Creates AnnotationModel and initializes it with the user-specified configuration.
	 * @param configuration
	 */
	public AnnotationModel(HashMap<String, Option> configuration){
		addRequirements();
	}
	
	/**
	 * Adds prerequisites to Set.
	 */
	protected abstract void addRequirements();
	

	//public abstract void makeSerializable();
	
	public Set<Class<? extends AnnotationModel<? extends IAnnotation>>> prerequisites(){
		return prerequisites;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AnnotationModel<?>){
			AnnotationModel<?> o = (AnnotationModel<?>) obj;
			return o.hashCode() == this.hashCode();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return identifier().hashCode();
	}
}
