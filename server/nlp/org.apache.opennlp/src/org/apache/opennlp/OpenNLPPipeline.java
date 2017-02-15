package org.apache.opennlp;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.opennlp.algorithms.SentenceModel;
import org.apache.opennlp.algorithms.TokenModel;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.pipeline.Pipeline;

public class OpenNLPPipeline extends Pipeline{
	
	public OpenNLPPipeline(){
		super();
	}
	
	public OpenNLPPipeline(List<String> models, HashMap<String, Option> configuration) throws NLPException {
		super(models, configuration);
		
	}


	@Override
	protected void configure(List<String> information, HashMap<String, Option> configuration) {
		
	}


	@Override
	protected void configure() {
		algorithms.add(TokenModel.class);
		algorithms.add(SentenceModel.class);
	}

}
