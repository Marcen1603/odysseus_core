/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.keyword;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

/**
 * @author Dennis Nowak
 *
 */
public class AutomaticParallelizationKeywordProvider implements IPreParserKeywordProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider#
	 * getKeywords()
	 */
	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();
		keywords.put(AutomaticParallelizationPreParserKeyword.KEYWORD, AutomaticParallelizationPreParserKeyword.class);
		return keywords;
	}

}
