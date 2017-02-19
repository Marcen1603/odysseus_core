package org.apache.opennlp;

import java.util.HashMap;
import java.util.List;

import org.apache.opennlp.algorithms.ChunksModel;
import org.apache.opennlp.algorithms.LemmasModel;
import org.apache.opennlp.algorithms.NamedEntitiesModel;
import org.apache.opennlp.algorithms.PartsOfSpeechModel;
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
		algorithms.add(NamedEntitiesModel.class);
		algorithms.add(PartsOfSpeechModel.class);
		algorithms.add(LemmasModel.class);
		algorithms.add(ChunksModel.class);
	}

}
