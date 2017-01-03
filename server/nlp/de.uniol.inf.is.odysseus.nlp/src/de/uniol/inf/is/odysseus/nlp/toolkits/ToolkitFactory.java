package de.uniol.inf.is.odysseus.nlp.toolkits;

import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;

public class ToolkitFactory {

	public static NLPToolkit get(String toolkit, List<String> information, HashMap<String, Option> configuration) {
		return new OpenNLPToolkit(information, configuration);
	}

}
