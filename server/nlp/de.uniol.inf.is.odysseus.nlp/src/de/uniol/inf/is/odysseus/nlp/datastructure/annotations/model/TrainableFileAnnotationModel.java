package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;

public abstract class TrainableFileAnnotationModel<T extends IAnnotation> extends FileStreamModel<T> implements ITrainableModel  {
	public TrainableFileAnnotationModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException {
		super(configuration);
	}

	public TrainableFileAnnotationModel() {
		super();
	}


}
