package de.uniol.inf.is.odysseus.script.keyword;

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

public class DeActivateRewriteRulePreParserKeyword extends
		AbstractPreParserKeyword {

	public static final String DEACTIVATEREWRITERULE = "DEACTIVATEREWRITERULE";


	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		
	}

	@Override
	public List<IExecutorCommand>  execute(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		@SuppressWarnings("unchecked")
		ArrayList<String> inactiveRules = (ArrayList<String>) variables
				.get(DEACTIVATEREWRITERULE);
		if (inactiveRules == null) {
			inactiveRules = new ArrayList<>();
			variables.put(DEACTIVATEREWRITERULE, inactiveRules);
		}
		if (parameter != null) {
			if (!inactiveRules.contains(parameter)) {
				inactiveRules.add(parameter);
			}
		}
		return null;
	}
	
	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		// TODO
//		try {
//			return 	getServerExecutor().getRewriteRules();
//		} catch (OdysseusScriptException e) {
//			e.printStackTrace();
//		}
		return null;
	}

}
