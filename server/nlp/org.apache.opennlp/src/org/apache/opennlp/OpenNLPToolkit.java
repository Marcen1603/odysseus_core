package org.apache.opennlp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.TrainableFileAnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.InvalidSpanException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelSerializationFailed;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;



public class OpenNLPToolkit extends NLPToolkit{

	/**
	 * Instantiates Toolkit with empty Pipeline for algorithm access only.
	 */
	public OpenNLPToolkit(){
		super();
	}
	
	public OpenNLPToolkit(List<String> models, HashMap<String, Option> configuration) throws NLPException {
		super(models, configuration);
	}

	@Override
	protected void instantiatePipeline(List<String> models, HashMap<String, Option> configuration) throws NLPException {
		this.pipeline = new OpenNLPPipeline(models, configuration);
	}

	@Override
	protected void instantiateEmptyPipeline() {
		this.pipeline = new OpenNLPPipeline();
	}
	
	public static Span convertOpenNLPSpanToSpan(opennlp.tools.util.Span span) throws InvalidSpanException{
		return new Span(span.getStart(), span.getEnd());
	}
	

}
