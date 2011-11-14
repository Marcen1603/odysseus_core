package de.uniol.inf.is.odysseus.script.parser;

import java.util.Map;

public interface IPreParserKeywordProvider {
	Map<String, Class<? extends IPreParserKeyword>> getKeywords();
}
