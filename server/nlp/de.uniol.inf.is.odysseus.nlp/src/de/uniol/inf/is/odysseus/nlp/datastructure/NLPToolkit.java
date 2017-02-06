package de.uniol.inf.is.odysseus.nlp.datastructure;

import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Option;

/**
 * Parent class for integrated nlp frameworks.
 */
public abstract class NLPToolkit {
	/**
	 * Pipeline of the NLPToolkit-Object
	 * 
	 * @see Pipeline
	 */
	protected Pipeline pipeline;
	

	/**
	 * Instantiates a NLPToolkit.
	 * 
	 * @see NLPToolkit
	 * 
	 * @param information List of Annotation-Names that have to be included in the annotated object after annotation.
	 * @param configuration HashMap of properties for {@link IAnnotationModel} configuration
	 */
	NLPToolkit(Set<String> information, HashMap<String, Option> configuration){
		this.instantiatePipeline(information, configuration);
	}

	/**
	 * Instantiates the pipeline field with a subclass-specified pipeline.
	 * 
	 * @param information List of Annotation-Names that have to be included in the annotated object after annotation.
	 * @param configuration HashMap of properties for {@link IAnnotationModel} configuration
	 */
	abstract void instantiatePipeline(Set<String> information, HashMap<String, Option> configuration);
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NLPToolkit){
			NLPToolkit o = (NLPToolkit) obj;
			return pipeline.equals(o.pipeline);
		}
		return false;
	}
}
