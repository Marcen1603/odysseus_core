package de.uniol.inf.is.odysseus.nlp.datastructure.toolkit;

import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.IAnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.Pipeline;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;

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
	 * @throws NLPException 
	 */
	protected NLPToolkit(Set<String> information, HashMap<String, Option> configuration) throws NLPException{
		this.instantiatePipeline(information, configuration);
	}

	/**
	 * Instantiates the pipeline field with a subclass-specified pipeline.
	 * 
	 * @param information List of Annotation-Names that have to be included in the annotated object after annotation.
	 * @param configuration HashMap of properties for {@link IAnnotationModel} configuration
	 * @throws NLPException 
	 */
	protected abstract void instantiatePipeline(Set<String> information, HashMap<String, Option> configuration) throws NLPException;
	
	/**
	 * Returns new {@link Annotated} object and annotates it with the configured pipeline.
	 * @param text to annotate
	 * @return fully annotated text
	 */
	public Annotated annotate(String text){
		Annotated annotated = new Annotated(text, pipeline.getInformation());
		pipeline.annotate(annotated);
		return annotated;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NLPToolkit){
			NLPToolkit o = (NLPToolkit) obj;
			return pipeline.equals(o.pipeline);
		}
		return false;
	}
}
