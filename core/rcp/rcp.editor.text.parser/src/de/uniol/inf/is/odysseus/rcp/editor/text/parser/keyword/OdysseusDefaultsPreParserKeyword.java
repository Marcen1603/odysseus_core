package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.editor.text.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.OdysseusDefaults;

public class OdysseusDefaultsPreParserKeyword extends AbstractPreParserKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter)
			throws QueryTextParseException {
		// Ignore
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter)
			throws QueryTextParseException {
		String[] params = getSimpleParameters(parameter);
		if (params.length >= 2) {
			boolean permanently = false;
			if (params.length == 3) {
				if ("TRUE".equalsIgnoreCase(params[2])) {
					permanently = true;
				}
			}
			OdysseusDefaults.set(params[0],params[1],permanently, getCurrentUser(variables));
		}

		return null;
	}
}
