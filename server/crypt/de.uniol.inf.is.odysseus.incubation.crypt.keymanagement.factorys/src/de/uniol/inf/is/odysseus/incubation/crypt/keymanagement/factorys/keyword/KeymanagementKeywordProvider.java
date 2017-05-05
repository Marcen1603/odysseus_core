package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys.keyword;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

/**
 * A PreParserKeywordProvider, which provides the Keyword, to create Keys.
 * 
 * @author MarkMilster
 *
 */
public class KeymanagementKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();
		keywords.put(AsymKeyFactoryKeyword.NAME, AsymKeyFactoryKeyword.class);
		keywords.put(SymKeyFactoryKeyword.NAME, SymKeyFactoryKeyword.class);
		return keywords;
	}

}
