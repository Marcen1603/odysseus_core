package de.uniol.inf.is.odysseus.incubation.graph.provider;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.incubation.graph.keyword.GraphdatastructureDropKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

/**
 * Provider for keyword registration in the system.
 * 
 * @author Kristian Bruns
 */
public class GraphKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();
		keywords.put(GraphdatastructureDropKeyword.GRAPHDATASTRUCTURE, GraphdatastructureDropKeyword.class);
		return keywords;
	}

}
