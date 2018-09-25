package de.uniol.inf.is.odysseus.core.server.planmanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

abstract public class AbstractQueryParser implements IQueryParser {


	@Override
	public Map<String, List<String>> getTokens(ISession user) {
		Map<String, List<String>> tokens = new HashMap<>();
		return tokens;
	}

	@Override
	public List<String> getSuggestions(String hint, ISession user) {
		return new ArrayList<>();
	}

}
