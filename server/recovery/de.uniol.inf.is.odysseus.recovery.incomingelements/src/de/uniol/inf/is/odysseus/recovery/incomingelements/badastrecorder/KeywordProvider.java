package de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

/**
 * Provides the preparser keyword {@link BaDaStRecorderKeyword}.
 * 
 * @author Michael Brand
 *
 */
public class KeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> out = Maps.newHashMap();
		out.put(BaDaStRecorderKeyword.getName(), BaDaStRecorderKeyword.class);
		return out;
	}

}