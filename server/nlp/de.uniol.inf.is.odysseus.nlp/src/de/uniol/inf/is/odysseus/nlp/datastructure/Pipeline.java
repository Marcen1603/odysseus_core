package de.uniol.inf.is.odysseus.nlp.datastructure;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;

/**
 * This abstract class is used for the definition of a pipeline inside {@link NLPToolkit} classes.
 */
public abstract class Pipeline {	
	/**
	 * Pipeline defined by fixed-order List of Annotation-Algorithms.
	 * @see IAnnotationModel
	 */
	protected List<AnnotationModel<? extends IAnnotation>> pipeline = new LinkedList<>();

	/**
	 * List of Annotation-Names that have to be included in the annotated object after annotation.
	 */
	private Set<String> information;
	
	/**
	 * A set, containing all possible models.
	 * @see IAnnotationModel
	 */
	protected static Set<Class<? extends AnnotationModel<? extends IAnnotation>>> algorithms = new HashSet<>();
	
	/**
	 * Internal constructor for user-configured pipelines.
	 * 
	 * @param information List of Annotation-Names that have to be included in the annotated object after annotation.
	 * @param configuration HashMap of properties for {@link IAnnotationModel} configuration
	 * @throws NLPException 
	 */
	protected Pipeline(Set<String> information, HashMap<String, Option> configuration) throws NLPException{	
		this.information = information;
		
		for(String inf: information){
			try {
				Class<? extends AnnotationModel<?>> model = identifierToModel(inf);
				addModelToPipeline(model, configuration);
			} catch (InstantiationException ignored) {
			}
		}
		
		configure(information, configuration);
	}
	
	private void addModelToPipeline(Class<? extends AnnotationModel<? extends IAnnotation>> model, HashMap<String, Option> configuration) throws NLPException{
		try {
			if(model == null)
				throw new NLPModelNotFoundException("One of the specified models were not found");
			Set<Class<? extends AnnotationModel<? extends IAnnotation>>> prerequisites = model.getConstructor().newInstance().prerequisites();
			
			for(Class<? extends AnnotationModel<? extends IAnnotation>> algorithm: prerequisites){
				addModelToPipeline(algorithm, configuration);
			}
			
			AnnotationModel<? extends IAnnotation> instance = model.getConstructor(HashMap.class).newInstance(configuration);
			
			if(!pipeline.contains(instance)){
				pipeline.add(instance);
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| NoSuchMethodException | SecurityException | InstantiationException ignored) {
			ignored.printStackTrace();
		} catch(InvocationTargetException e){
			if(e.getCause() instanceof NLPException){
				throw (NLPException)e.getCause();
			}
		}
	}
	
	/**
	 * Used for automatically detecting algorithm-order.
	 * 
	 * @param information List of Annotation-Names that have to be included in the annotated object after annotation.
	 * @param configuration HashMap of properties for {@link IAnnotationModel} configuration
	 */
	protected abstract void configure(Set<String> information, HashMap<String, Option> configuration);
	
	
	/**
	 * Method that runs the pipeline on an annotated object.
	 * 
	 * @param annotated Object for annotation.
	 */
	public void annotate(Annotated annotated) {
		for(AnnotationModel<? extends IAnnotation> model : pipeline){
			model.annotate(annotated);
		}
	}
	

	/**
	 * Returns the class of a specific model
	 * @param identifier of model
	 * @return Class of model
	 * @throws InstantiationException 
	 */
	private Class<? extends AnnotationModel<?>> identifierToModel(String identifier) throws InstantiationException {
		for(Class<? extends AnnotationModel<?>> model : algorithms){
			try {
				String id = model.getConstructor().newInstance().identifier();
				if(id.equals(identifier))
					return model;
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException ignored) {
				ignored.printStackTrace();
			}
		}
		return null;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pipeline){
			Pipeline o = (Pipeline) obj;
			return this.pipeline.equals(o.pipeline);
		}
		return false;
	}

	public Set<String> getInformation() {
		return information;
	}

}
