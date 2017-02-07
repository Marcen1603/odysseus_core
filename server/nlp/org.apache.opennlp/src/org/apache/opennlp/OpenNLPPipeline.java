package org.apache.opennlp;

import java.util.HashMap;
import java.util.Set;

import org.apache.opennlp.algorithms.SentenceModel;
import org.apache.opennlp.algorithms.TokenModel;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.Pipeline;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;

public class OpenNLPPipeline extends Pipeline{
	
	OpenNLPPipeline(Set<String> information, HashMap<String, Option> configuration) throws NLPException {
		super(information, configuration);
	}

	static{
		algorithms.add(TokenModel.class);
		algorithms.add(SentenceModel.class);
	}


	@Override
	protected void configure(Set<String> information, HashMap<String, Option> configuration) {
		
	}

}
