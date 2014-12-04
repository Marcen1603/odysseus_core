package de.uniol.inf.is.odysseus.peer.recovery.keyword;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class RecoveryPreParserKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = Maps.newHashMap();
		
		keywords.put(RecoveryAllocatorPreParserKeyword.KEYWORD, RecoveryAllocatorPreParserKeyword.class);
		keywords.put(RecoveryStrategyManagerPreParserKeyword.KEYWORD, RecoveryStrategyManagerPreParserKeyword.class);
		return keywords;
	}

}
