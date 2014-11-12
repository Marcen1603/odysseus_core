package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.parser.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class SmartDeviceLogicRuleNamePreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "SMARTDEVICE_LOGIC_RULE_NAME";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		if (parameter.length() == 0)
			throw new OdysseusScriptException("Parameter needed for #"+KEYWORD);
		
		variables.put(KEYWORD, parameter);
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context)
			throws OdysseusScriptException {
		variables.put(KEYWORD, parameter);
		return null;
	}

}
