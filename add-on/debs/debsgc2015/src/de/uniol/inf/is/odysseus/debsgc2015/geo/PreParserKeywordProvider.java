package de.uniol.inf.is.odysseus.debsgc2015.geo;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class PreParserKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = Maps.newHashMap();
		keywords.put(InitCityGridPreParserKeyword.KEYWORD, InitCityGridPreParserKeyword.class);
		return keywords;
	}

}