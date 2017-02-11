package keyword;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class AdmissionKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> map = new HashMap<String, Class<? extends IPreParserKeyword>>();
		map.put(LoadSheddingPreParserKeyword.LOADSHEDDING, LoadSheddingPreParserKeyword.class);
		return map;
	}

}
