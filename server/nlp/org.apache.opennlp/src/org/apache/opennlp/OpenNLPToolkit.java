package org.apache.opennlp;

import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;


public class OpenNLPToolkit extends NLPToolkit{

	public OpenNLPToolkit(Set<String> information, HashMap<String, Option> configuration) throws NLPException {
		super(information, configuration);
	}

	@Override
	protected void instantiatePipeline(Set<String> information, HashMap<String, Option> configuration) throws NLPException {
		this.pipeline = new OpenNLPPipeline(information, configuration);
	}
	
}
