package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class P2PKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = Maps.newHashMap();
		keywords.put(ExportPreParserKeyword.KEYWORD, ExportPreParserKeyword.class);
		keywords.put(UnexportPreParserKeyword.KEYWORD, UnexportPreParserKeyword.class);
		keywords.put(ExportAllPreParserKeyword.KEYWORD, ExportAllPreParserKeyword.class);
		keywords.put(UnexportAllPreParserKeyword.KEYWORD, UnexportAllPreParserKeyword.class);
		keywords.put(DoUDPPreParserKeyword.KEYWORD, DoUDPPreParserKeyword.class);
		return keywords;
	}

}
