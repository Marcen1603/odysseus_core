package org.apache.opennlp;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.TrainableFileAnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;


public abstract class OpenNLPModel<A extends IAnnotation> extends TrainableFileAnnotationModel<A>{
	private static final long serialVersionUID = -5883316418476673362L;

	public OpenNLPModel(){
		super();
	}
	
	public OpenNLPModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException {
		super(configuration);	
	}

}
