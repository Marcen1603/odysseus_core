package de.uniol.inf.is.odysseus.admission.status.keyword;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

/**
 * This class adds the LOADSHEDDING keyword to the usable keywords in PQL.
 */
public class AdmissionKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> map = new HashMap<String, Class<? extends IPreParserKeyword>>();
		map.put(LoadSheddingPreParserKeyword.LOADSHEDDING, LoadSheddingPreParserKeyword.class);
		return map;
	}

}
