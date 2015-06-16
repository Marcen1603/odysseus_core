package de.uniol.inf.is.odysseus.multithreaded.interoperator.keyword;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class InterOperatorParallelizationKeywordProvider implements
		IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();
		keywords.put(InterOperatorParallelizationPreParserKeyword.KEYWORD,
				InterOperatorParallelizationPreParserKeyword.class);
		return keywords;
	}

}
