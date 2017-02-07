package org.apache.opennlp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.FileStreamModel;


public abstract class OpenNLPModel<A extends IAnnotation> extends FileStreamModel<A>{
	
	public OpenNLPModel(){
		super();
	}
	
	public OpenNLPModel(HashMap<String, Option> configuration) throws NLPModelNotFoundException, NLPException {
		super(configuration);	
	}

}
