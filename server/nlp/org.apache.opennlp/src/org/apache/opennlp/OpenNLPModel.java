package org.apache.opennlp;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.FileStreamModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.TrainableFileAnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;


public abstract class OpenNLPModel<A extends IAnnotation> extends TrainableFileAnnotationModel<A>{
	
	public OpenNLPModel(){
		super();
	}
	
	public OpenNLPModel(HashMap<String, Option> configuration) throws NLPModelNotFoundException, NLPException {
		super(configuration);	
	}

}
