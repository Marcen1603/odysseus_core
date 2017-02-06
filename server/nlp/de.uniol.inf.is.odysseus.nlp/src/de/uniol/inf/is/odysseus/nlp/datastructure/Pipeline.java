package de.uniol.inf.is.odysseus.nlp.datastructure;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Option;

/**
 * This abstract class is used for the definition of a pipeline inside {@link NLPToolkit} classes.
 */
public abstract class Pipeline {
	/**
	 * Pipeline defined by fixed-order List of Annotation-Algorithms. Used a {@link Set} inside the class because some algorithms do not depend upon each other and have the same prerequisites.
	 * @see IAnnotationModel
	 */
	protected List<Set<IAnnotationModel<? extends IAnnotation>>> pipeline = new LinkedList<>();
	
	
	/**
	 * Internal constructor for user-configured pipelines.
	 * 
	 * @param information List of Annotation-Names that have to be included in the annotated object after annotation.
	 * @param configuration HashMap of properties for {@link IAnnotationModel} configuration
	 */
	Pipeline(Set<String> information, HashMap<String, Option> configuration){
		configure(information, configuration);
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
	public abstract void annotate(Annotated annotated);
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pipeline){
			Pipeline o = (Pipeline) obj;
			return this.pipeline.equals(o.pipeline);
		}
		return false;
	}
}
