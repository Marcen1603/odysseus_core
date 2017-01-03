package de.uniol.inf.is.odysseus.nlp.toolkits;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.SentenceAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.TokenAnnotation;

public abstract class NLPToolkit {
	protected List<String> information;
	private HashMap<String, Option> configuration;

	
	
	private List<Class<? extends IAnnotation>> annotationClasses = Arrays.asList(
			SentenceAnnotation.class, 
			TokenAnnotation.class
			);


	
	public NLPToolkit(List<String> information, HashMap<String, Option> configuration){
		this.information = information;
		this.configuration = configuration;
	}
	
	public abstract Annotation annotate(String text);

	abstract Class<? extends IAnnotation> getHighestAnnotation();
	

	protected InputStream getModelAsStream(Class<? extends IAnnotation> annotationGoal) throws FileNotFoundException{
		String identifier = null;
		try {
			identifier = (String) annotationGoal.getField("NAME").get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ignored) {
		}
		File file = new File((String)configuration.get("model."+identifier).getValue());
		
		return new FileInputStream(file);
	}
	
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
