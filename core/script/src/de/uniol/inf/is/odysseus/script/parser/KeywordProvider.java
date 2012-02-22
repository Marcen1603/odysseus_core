package de.uniol.inf.is.odysseus.script.parser;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.parser.keyword.BufferPlacementPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.OdysseusDefaultsPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.ParserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.QuerySharingPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.ReloadFromLogPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.SLAPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.TransCfgPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.UseRewritePreParserKeyword;

public class KeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();
		keywords.put(BufferPlacementPreParserKeyword.BUFFERPLACEMENT, BufferPlacementPreParserKeyword.class);
		keywords.put(OdysseusDefaultsPreParserKeyword.ODYSSEUS_PARAM, OdysseusDefaultsPreParserKeyword.class);
		keywords.put(ParserPreParserKeyword.PARSER, ParserPreParserKeyword.class);
		keywords.put(ReloadFromLogPreParserKeyword.RELOADFROMLOG, ReloadFromLogPreParserKeyword.class);
		keywords.put(TransCfgPreParserKeyword.TRANSCFG, TransCfgPreParserKeyword.class);
		keywords.put(SLAPreParserKeyword.SLA, SLAPreParserKeyword.class);
		keywords.put(QuerySharingPreParserKeyword.DOQUERYSHARING, QuerySharingPreParserKeyword.class);
		keywords.put(UseRewritePreParserKeyword.DOREWRITE, UseRewritePreParserKeyword.class);
		return keywords;
	}

}
