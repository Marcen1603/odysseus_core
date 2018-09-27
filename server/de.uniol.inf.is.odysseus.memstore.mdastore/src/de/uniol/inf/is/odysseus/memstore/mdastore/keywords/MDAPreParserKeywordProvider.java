package de.uniol.inf.is.odysseus.memstore.mdastore.keywords;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class MDAPreParserKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = Maps.newHashMap();
		keywords.put(MDAStoreInitPreParserKeyword.KEYWORD, MDAStoreInitPreParserKeyword.class);
		keywords.put(MDAStoreDropPreParserKeyword.KEYWORD, MDAStoreDropPreParserKeyword.class);
		return keywords;
	}

}