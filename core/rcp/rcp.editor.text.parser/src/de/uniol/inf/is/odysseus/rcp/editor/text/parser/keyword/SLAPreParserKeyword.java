package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.editor.text.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.sla.SLADictionary;

public class SLAPreParserKeyword extends AbstractPreParserKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter)
			throws QueryTextParseException {
		/*
		 * check if the selected sla is already defined
		 */
		if (!SLADictionary.getInstance().exists(parameter)) {
			throw new RuntimeException(
					"A service level agreement with the name '" + parameter
							+ " doesn't exist!");
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter)
			throws QueryTextParseException {
		/*
		 * TODO buffer sla name for the usage for following queries of the user 
		 */
		return null;
	}

}
