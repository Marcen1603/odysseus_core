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
package de.uniol.inf.is.odysseus.script.parser;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.parser.keyword.OdysseusDefaultsPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.QueryNamePreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.QuerySharingPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.ReloadFromLogPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.SleepPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.UseRewritePreParserKeyword;

public class KeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();		
		keywords.put(OdysseusDefaultsPreParserKeyword.ODYSSEUS_PARAM, OdysseusDefaultsPreParserKeyword.class);		
		keywords.put(ReloadFromLogPreParserKeyword.RELOADFROMLOG, ReloadFromLogPreParserKeyword.class);		
		keywords.put(QuerySharingPreParserKeyword.DOQUERYSHARING, QuerySharingPreParserKeyword.class);
		keywords.put(UseRewritePreParserKeyword.DOREWRITE, UseRewritePreParserKeyword.class);
		keywords.put(SleepPreParserKeyword.SLEEP, SleepPreParserKeyword.class);
		keywords.put(QueryNamePreParserKeyword.QNAME, QueryNamePreParserKeyword.class);
		return keywords;
	}

}
