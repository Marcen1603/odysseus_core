package de.uniol.inf.is.odysseus.nlp.toolkits;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	protected List<Class<? extends IAnnotation>> internalPipeline;
	protected HashMap<Class<? extends IAnnotation>, Object> models;
	private List<Class<? extends IAnnotation>> annotationClasses;


	
	public NLPToolkit(List<String> information, HashMap<String, Option> configuration, List<Class<? extends IAnnotation>> pipeline) throws NLPInformationNotSupportedException, NLPModelNotFoundException{
		this.annotationClasses = Arrays.asList(
				SentenceAnnotation.class, 
				TokenAnnotation.class
				);
		this.models = new HashMap<>();
		this.internalPipeline = pipeline;
		this.information = information;
		this.configuration = configuration;
		this.init();
	}
	
	public abstract void init() throws NLPInformationNotSupportedException, NLPModelNotFoundException;

	public Annotation annotate(String text) {
		Annotation annotation = new Annotation(text);
		Class<? extends IAnnotation> highestAnnotation = getHighestAnnotation();
		
		if(highestAnnotation != null){
			try {
				switch((String)highestAnnotation.getField("NAME").get(null)){
				case "sentence":
					annotateSentences(annotation);
				case "token":
					annotateSentences(annotation);
					annotateTokens(annotation);
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return annotation;
	}

	
	abstract void annotateSentences(Annotation annotation);
	abstract void annotateTokens(Annotation annotation);

	protected Class<? extends IAnnotation> getHighestAnnotation() {
		List<Class<? extends IAnnotation>> pipeline = new ArrayList<>(internalPipeline);
		Collections.reverse(pipeline);
		for(Class<? extends IAnnotation> clazz : pipeline){
			try {
				String name = (String) clazz.getField("NAME").get(null);
				for(String inclName : information){
					if(inclName.equals(name)){
						return clazz;
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException  e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	protected InputStream getModelAsStream(Class<? extends IAnnotation> annotationGoal) throws FileNotFoundException, NLPModelNotFoundException{
		String identifier = null;
		try {
			identifier = (String) annotationGoal.getField("NAME").get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ignored) {
		}
		Option option = configuration.get("model."+identifier);
		if(option == null)
			throw new NLPModelNotFoundException("model."+identifier);
		
		File file = new File((String)option.getValue());
		
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
