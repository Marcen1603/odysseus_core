package de.uniol.inf.is.odysseus.net.querydistribute.keyword;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class QueryDistributionPreParserKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = Maps.newHashMap();
		
		keywords.put(QueryPartitionerPreParserKeyword.KEYWORD, QueryPartitionerPreParserKeyword.class);
		keywords.put(QueryPartModificatorPreParserKeyword.KEYWORD, QueryPartModificatorPreParserKeyword.class);
		keywords.put(QueryPartAllocatorPreParserKeyword.KEYWORD, QueryPartAllocatorPreParserKeyword.class);
		keywords.put(QueryDistributionPreProcessorPreParserKeyword.KEYWORD, QueryDistributionPreProcessorPreParserKeyword.class);
		keywords.put(QueryDistributionPostProcessorPreParserKeyword.KEYWORD, QueryDistributionPostProcessorPreParserKeyword.class);
		
		return keywords;
	}

}
