package de.uniol.inf.is.odysseus.peer.distribute.keyword;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class PeerPreParserKeywordProvider implements IPreParserKeywordProvider {

	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = Maps.newHashMap();
		
		keywords.put(PeerQueryPartitionerPreParserKeyword.KEYWORD, PeerQueryPartitionerPreParserKeyword.class);
		keywords.put(PeerQueryPartModificatorPreParserKeyword.KEYWORD, PeerQueryPartModificatorPreParserKeyword.class);
		keywords.put(PeerQueryPartAllocatorPreParserKeyword.KEYWORD, PeerQueryPartAllocatorPreParserKeyword.class);
		keywords.put(DoMergePreParserKeyword.KEYWORD, DoMergePreParserKeyword.class);
		keywords.put(DoForceLocalPreParserKeyword.KEYWORD, DoForceLocalPreParserKeyword.class);
		keywords.put(PeerQueryDistributionPreProcessorPreParserKeyword.KEYWORD, PeerQueryDistributionPreProcessorPreParserKeyword.class);
		keywords.put(PeerQueryDistributionPostProcessorPreParserKeyword.KEYWORD, PeerQueryDistributionPostProcessorPreParserKeyword.class);
		
		return keywords;
	}

}
