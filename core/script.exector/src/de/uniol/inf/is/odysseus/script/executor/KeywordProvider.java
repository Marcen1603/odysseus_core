package de.uniol.inf.is.odysseus.script.executor;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.keyword.AddQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.CyclicQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.ExecuteQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.LoginUserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.LogoutUserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.SchedulerPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.StartAllClosedQueriesPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class KeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();
		keywords.put(LoginUserPreParserKeyword.LOGIN, LoginUserPreParserKeyword.class);
		keywords.put(LogoutUserPreParserKeyword.LOGOUT, LogoutUserPreParserKeyword.class);

		keywords.put(AddQueryPreParserKeyword.QUERY, AddQueryPreParserKeyword.class);
		keywords.put(AddQueryPreParserKeyword.ADDQUERY, AddQueryPreParserKeyword.class);
		keywords.put(ExecuteQueryPreParserKeyword.RUNQUERY, ExecuteQueryPreParserKeyword.class);
		keywords.put(CyclicQueryPreParserKeyword.CYCLICQUERY, CyclicQueryPreParserKeyword.class);
		keywords.put(StartAllClosedQueriesPreParserKeyword.STARTQUERIES, StartAllClosedQueriesPreParserKeyword.class);
		keywords.put(SchedulerPreParserKeyword.SCHEDULER, SchedulerPreParserKeyword.class);
		return keywords;
	}

}
