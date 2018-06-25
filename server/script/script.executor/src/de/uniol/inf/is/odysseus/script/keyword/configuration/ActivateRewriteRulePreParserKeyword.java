package de.uniol.inf.is.odysseus.script.keyword.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class ActivateRewriteRulePreParserKeyword extends
		AbstractPreParserKeyword {

	public static final String ACTIVATEREWRITERULE = "ACTIVATEREWRITERULE";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		@SuppressWarnings("unchecked")
		ArrayList<String> activeRules = (ArrayList<String>) variables
				.get(ACTIVATEREWRITERULE);
		if (activeRules == null) {
			activeRules = new ArrayList<>();
			variables.put(ACTIVATEREWRITERULE, activeRules);
		}
		if (parameter != null) {
			if (!activeRules.contains(parameter)) {
				activeRules.add(parameter);
			}
		}
		return null;
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {

		
		// TODO

//		try {
//			return getServerExecutor().getRewriteRules();
//		} catch (OdysseusScriptException e) {
//			e.printStackTrace();
//		}
		return null;
	}

}
