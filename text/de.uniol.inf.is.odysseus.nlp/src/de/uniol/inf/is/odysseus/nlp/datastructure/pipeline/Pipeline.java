package de.uniol.inf.is.odysseus.nlp.datastructure.pipeline;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.server.event.error.ParameterException;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.IAnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.IJoinable;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelPrerequisitesNotFulfilledException;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;

/**
 * This abstract class is used for the definition of a pipeline inside {@link NLPToolkit} classes.
 */
public abstract class Pipeline {	
	private final Logger LOGGER = LoggerFactory.getLogger(Pipeline.class);
	
	/**
	 * Pipeline defined by fixed-order List of Annotation-Algorithms.
	 * @see IAnnotationModel
	 */
	protected List<AnnotationModel<? extends IAnnotation>> pipeline = new LinkedList<>();

	/**
	 * Pipeline defined by fixed-order List of Annotation-Algorithms.
	 * @see IAnnotationModel
	 */
	protected Set<Class<? extends AnnotationModel<? extends IAnnotation>>> pipelineClasses = new HashSet<>();

	/**
	 * List of Annotation-Names that have to be included in the annotated object after annotation.
	 */
	private List<String> models;
	
	/**
	 * A set, containing all possible models.
	 * @see IAnnotationModel
	 */
	protected static Set<Class<? extends AnnotationModel<? extends IAnnotation>>> algorithms = new HashSet<>();
	
	/**
	 * Internal constructor for unconfigured Pipeline for model-access.
	 */
	public Pipeline(){
		configure();
	}
	
	/**
	 * Internal constructor for user-configured pipelines.
	 * 
	 * @param models List of Annotation-Names that have to be included in the annotated object after annotation.
	 * @param configuration HashMap of properties for {@link IAnnotationModel} configuration
	 * @throws NLPException 
	 */
	public Pipeline(List<String> models, Map<String, Option> configuration) throws NLPException{	
		configure();
		configure(models, configuration);
		
		this.models = models;		
		for(String inf: models){
			Class<? extends AnnotationModel<?>> model = identifierToModel(inf);
			addModelToPipeline(model, configuration);
		}
		
	}
	
	private void addModelToPipeline(Class<? extends AnnotationModel<? extends IAnnotation>> model, Map<String, Option> configuration) throws NLPException{
		try {
			if(model == null)
				throw new NLPModelNotFoundException("One of the specified models were not found");
			
			Set<Class<? extends AnnotationModel<? extends IAnnotation>>> prerequisites = model.getConstructor().newInstance().prerequisites();
			AnnotationModel<? extends IAnnotation> instance = model.getConstructor(Map.class).newInstance(configuration);

			/*
			 * Check if all prerequisites are fulfilled.
			 */
			if(!pipelineClasses.containsAll(prerequisites)){
				throw new NLPModelPrerequisitesNotFulfilledException("Some of the prerequsites of " + instance.identifier() + " are not fulfilled. "
						+ "Try to review the pipeline order and check if any nlp-algorithms may have to be run earlier than this one.");
			}
					
			if(!pipeline.contains(instance)){
				pipeline.add(instance);
				pipelineClasses.add(model);
			}
		} catch (IllegalAccessException | IllegalArgumentException
				 | SecurityException | InstantiationException ignored) {
			ignored.printStackTrace();
		}catch(NoSuchMethodException e){
			throw new ParameterException("AnnotationModels should implement both constructors AnnotationModel() and AnnotationModel(Map)");
		}catch(InvocationTargetException e){
			Throwable ex = e.getCause();
			if(ex instanceof NLPException || ex instanceof NLPModelNotFoundException){
				throw (NLPException)e.getCause();
			}else{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Used for automatically detecting algorithm-order.
	 * 
	 * @param information List of Annotation-Names that have to be included in the annotated object after annotation.
	 * @param configuration HashMap of properties for {@link IAnnotationModel} configuration
	 */
	protected abstract void configure(List<String> information, Map<String, Option> configuration);
	
	
	/**
	 * Used for adding models to algorithms set.
	 */
	protected abstract void configure();
	
	
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
	 * @throws NLPModelNotFoundException 
	 */
	public Class<? extends AnnotationModel<?>> identifierToModel(String identifier) throws NLPModelNotFoundException {
		for(Class<? extends AnnotationModel<?>> model : algorithms){
			try {
				String id = model.getConstructor().newInstance().identifier();
				if(id.equals(identifier))
					return model;
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | InstantiationException ignored) {
				ignored.printStackTrace();
			}
		}
		throw new NLPModelNotFoundException(identifier);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pipeline){
			Pipeline o = (Pipeline) obj;
			return this.pipeline.equals(o.pipeline);
		}
		return false;
	}
	
	/**
	 * Returns set of information to be included in Annotated object after annotation.
	 * @see Pipeline#annotate(Annotated)
	 * @return Set of strings with information to be included
	 */
	public List<String> getModels() {
		return models;
	}
	
	/**
	 * This method can be used to exchange specific AnnotationModels in the pipeline
	 * @param exchange the model to be inserted
	 * 
	 */
	public void exchange(AnnotationModel<?> exchange){
		int idx = -1;
		for(int i = 0; i < pipeline.size(); i++){
			if(pipeline.get(i).identifier().equals(exchange.identifier())){
				idx = i;
			}
		}
		if(idx != -1){
			LOGGER.info("Exchanged Model: "+exchange.identifier());
			if(exchange instanceof IJoinable){
				IJoinable old = (IJoinable) pipeline.get(idx);
				AnnotationModel<?> model = old.join(exchange, true); 
				pipeline.set(idx, model);
			}else{
				pipeline.set(idx, exchange);
			}
		}
	}
	
}
