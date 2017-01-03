package de.uniol.inf.is.odysseus.nlp.toolkits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.SentenceAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.TokenAnnotation;

public abstract class NLPToolkit {
	protected List<String> information;

	private List<Class<? extends IAnnotation>> annotationClasses = Arrays.asList(
			SentenceAnnotation.class, 
			TokenAnnotation.class
			);
	
	public NLPToolkit(List<String> information){
		this.information = information;
	}
	
	public abstract Annotation annotate(String text);

	abstract Class<? extends IAnnotation> getHighestAnnotation();
	

	
	List<Class<? extends IAnnotation>> getAnnotationClasses() {
		List<Class<? extends IAnnotation>> classes = new ArrayList<>();
		
		for(Class<? extends IAnnotation> clazz : annotationClasses){
			try {
				if(information.contains(clazz.getField("NAME").get(null))){
					classes.add(clazz);
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException  e) {
				e.printStackTrace();
			}
		}
		return classes;
	}
}
