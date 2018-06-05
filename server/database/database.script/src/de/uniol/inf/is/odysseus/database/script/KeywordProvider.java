package de.uniol.inf.is.odysseus.database.script;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class KeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();
		
		keywords.put(DropAllDatabaseConnectionsPreParserKeyword.NAME, DropAllDatabaseConnectionsPreParserKeyword.class);

		return keywords;
	}

}
