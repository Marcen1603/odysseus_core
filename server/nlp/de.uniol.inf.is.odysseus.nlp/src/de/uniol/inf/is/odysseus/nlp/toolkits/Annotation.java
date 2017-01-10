package de.uniol.inf.is.odysseus.nlp.toolkits;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.IAnnotation;

public class Annotation {
	public static final String NERID = "namedentity";
	public static final String SENTENCEID = "sentence";
	public static final String TOKENID = "token";
	private String text;
	private HashMap<Class<? extends IAnnotation>, IAnnotation> annotations = new HashMap<>();

	Annotation(String text){
		this.text = text;
	}

	public IAnnotation getAnnotation(Class<? extends IAnnotation> clazz){
		return annotations.get(clazz);
	}

	public void setAnnotation(Class<? extends IAnnotation> clazz, IAnnotation annotation){
		annotations.put(clazz, annotation);
	}

	public String getText() {
		// TODO Auto-generated method stub
		return text;
	}
}
