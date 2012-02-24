package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.sla.SLADictionary;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class SLAPreParserKeyword extends AbstractPreParserKeyword {

	public static final String SLA = "SLA";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller)
			throws OdysseusScriptException {
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
	public Object execute(Map<String, Object> variables, String parameter, ISession caller)
			throws OdysseusScriptException {
		/*
		 * buffer sla name for the usage for following queries of the user
		 */
		SLADictionary.getInstance().setCurrentSLA(caller,
				parameter);

		return null;
	}

}
