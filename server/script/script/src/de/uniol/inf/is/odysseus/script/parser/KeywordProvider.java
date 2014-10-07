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

import de.uniol.inf.is.odysseus.script.parser.keyword.ConfigPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.IncludePreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.MetadataPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.OdysseusDefaultsPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.QueryNamePreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.QueryPriorityPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.ReloadFromLogPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.ResumeOnErrorPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.TrafoOptionPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.UseAdaptionPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.UseFragmentationPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.UseRewritePreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.deprecated.QuerySharingPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.deprecated.UseDistributePreParserKeyword;

@SuppressWarnings("deprecation")
public class KeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();		
		keywords.put(OdysseusDefaultsPreParserKeyword.ODYSSEUS_PARAM, OdysseusDefaultsPreParserKeyword.class);		
		keywords.put(ReloadFromLogPreParserKeyword.RELOADFROMLOG, ReloadFromLogPreParserKeyword.class);		
		keywords.put(QuerySharingPreParserKeyword.DOQUERYSHARING, QuerySharingPreParserKeyword.class);
		keywords.put(UseRewritePreParserKeyword.DOREWRITE, UseRewritePreParserKeyword.class);
		keywords.put(UseDistributePreParserKeyword.DODISTRIBUTE, UseDistributePreParserKeyword.class);
		keywords.put(UseFragmentationPreParserKeyword.DODATAFRAGMENTATION, UseFragmentationPreParserKeyword.class);
		keywords.put(UseAdaptionPreParserKeyword.DOADAPT, UseAdaptionPreParserKeyword.class);
		keywords.put(QueryNamePreParserKeyword.QNAME, QueryNamePreParserKeyword.class);
		keywords.put(ResumeOnErrorPreParserKeyword.KEYWORD, ResumeOnErrorPreParserKeyword.class);
		keywords.put(ConfigPreParserKeyword.KEYWORD, ConfigPreParserKeyword.class);
		keywords.put(IncludePreParserKeyword.KEYWORD, IncludePreParserKeyword.class);
		keywords.put(MetadataPreParserKeyword.METADATA, MetadataPreParserKeyword.class);
		keywords.put(TrafoOptionPreParserKeyword.TRAFOOPTION, TrafoOptionPreParserKeyword.class);
		keywords.put(QueryPriorityPreParserKeyword.NAME, QueryPriorityPreParserKeyword.class);
		return keywords;
	}

}
