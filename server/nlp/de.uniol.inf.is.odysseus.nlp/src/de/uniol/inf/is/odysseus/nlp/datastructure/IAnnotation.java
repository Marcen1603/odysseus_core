package de.uniol.inf.is.odysseus.nlp.datastructure;

import java.util.Map;

/**
 * Parent-interface for all annotations.
 */
public interface IAnnotation {
	/**
	 * @return identifier of the annotation
	 */
	public String identifier();
	
	/**
	 * Returns all sub-annotations.
	 * @return Annotations inside this annotation object.
	 */
	public Map<String, IAnnotation> getAnnotations();
		
	/**
	 * Returns KeyValue-Object for KeyValue-Outputstream
	 * @return Object for KeyValue-Outputstream
	 */
	public Object toObject();
}
