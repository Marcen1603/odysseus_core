package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;

public abstract class TrainableFileAnnotationModel<T extends IAnnotation> extends FileStreamModel<T> implements ITrainableModel  {
	public TrainableFileAnnotationModel(HashMap<String, Option> configuration) throws NLPModelNotFoundException, NLPException {
		super(configuration);
	}

	public TrainableFileAnnotationModel() {
		super();
	}

	private static final long serialVersionUID = 267063142301178985L;

}
