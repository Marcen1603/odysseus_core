package de.uniol.inf.is.odysseus.script.executor;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.keyword.AddQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.BufferPlacementPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.CyclicQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.ExecuteQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.QuerySharingPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.SchedulerPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.StartAllClosedQueriesPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.UseRewritePreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class KeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();

		keywords.put("QUERY", AddQueryPreParserKeyword.class);
		keywords.put("ADDQUERY", AddQueryPreParserKeyword.class);
		keywords.put("RUNQUERY", ExecuteQueryPreParserKeyword.class);
		keywords.put("CYCLICQUERY", CyclicQueryPreParserKeyword.class);
		keywords.put("BUFFERPLACEMENT", BufferPlacementPreParserKeyword.class);
		keywords.put("STARTQUERIES", StartAllClosedQueriesPreParserKeyword.class);
		keywords.put("SCHEDULER", SchedulerPreParserKeyword.class);
		keywords.put("DOQUERYSHARING", QuerySharingPreParserKeyword.class);
		keywords.put("DOREWRITE", UseRewritePreParserKeyword.class);
		return keywords;
	}

}
