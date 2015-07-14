package de.uniol.inf.is.odysseus.recovery.configuration;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

/**
 * Provider of all Odysseus Script keywords related to recovery.
 * 
 * @author Michael Brand
 *
 */
public class RecoveryConfigKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> recoveryKeywords = Maps
				.newHashMap();
		recoveryKeywords.put(RecoveryConfigKeyword.getName(),
				RecoveryConfigKeyword.class);
		return recoveryKeywords;
	}

}