package org.apache.opennlp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.TrainableFileAnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;


public abstract class OpenNLPTrainableModel<A extends IAnnotation> extends TrainableFileAnnotationModel<A>{
	public OpenNLPTrainableModel(){
		super();
	}
	
	public OpenNLPTrainableModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException {
		super(configuration);	
	}

	@Override
	public void store(File file) throws FileNotFoundException, IOException {
		try(OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(file))){
		  store(modelOut);
		}
		            
	}

	protected abstract void store(OutputStream modelOut) throws IOException;
	
}
