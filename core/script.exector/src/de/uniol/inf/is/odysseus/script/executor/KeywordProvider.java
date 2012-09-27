/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.script.executor;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.keyword.AddQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.DropAllQueriesPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.ExecuteQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.LoginUserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.LogoutUserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.SchedulerPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.StartAllClosedQueriesPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.StartSchedulerPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.StopSchedulerPreParserKeyword;
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
		keywords.put(StartAllClosedQueriesPreParserKeyword.STARTQUERIES, StartAllClosedQueriesPreParserKeyword.class);
		keywords.put(SchedulerPreParserKeyword.SCHEDULER, SchedulerPreParserKeyword.class);
		keywords.put(DropAllQueriesPreParserKeyword.DROPALLQUERIES, DropAllQueriesPreParserKeyword.class);
		keywords.put(StartSchedulerPreParserKeyword.KEYWORD, StartSchedulerPreParserKeyword.class);
		keywords.put(StopSchedulerPreParserKeyword.KEYWORD, StopSchedulerPreParserKeyword.class);
		
		return keywords;
	}

}
