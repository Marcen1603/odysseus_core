package de.uniol.inf.is.odysseus.script.parser;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.parser.keyword.LoginUserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.LogoutUserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.OdysseusDefaultsPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.ParserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.ReloadFromLogPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.SLAPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.TransCfgPreParserKeyword;

public class KeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();
		keywords.put("LOGIN", LoginUserPreParserKeyword.class);
		keywords.put("LOGOUT", LogoutUserPreParserKeyword.class);
		keywords.put("ODYSSEUS_PARAM", OdysseusDefaultsPreParserKeyword.class);
		keywords.put("RELOADFROMLOG", ReloadFromLogPreParserKeyword.class);
		keywords.put("PARSER", ParserPreParserKeyword.class);
		keywords.put("TRANSCFG", TransCfgPreParserKeyword.class);
		keywords.put("SLA", SLAPreParserKeyword.class);
		return keywords;
	}

}
