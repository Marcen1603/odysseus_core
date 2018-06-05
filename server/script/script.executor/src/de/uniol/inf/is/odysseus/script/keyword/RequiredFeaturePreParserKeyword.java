package de.uniol.inf.is.odysseus.script.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.updater.FeatureUpdateUtility;

public class RequiredFeaturePreParserKeyword extends AbstractPreParserKeyword {

	public static final String REQUIRED = "REQUIRED";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		// Must be done at validate, else the following commands could cause errors
		String params[] = getSimpleParameters(parameter);
		String id = params[0];
		boolean install = true;
		if (params.length > 1) {
			install = !params[1].trim().equalsIgnoreCase("false");
		}
		if (!FeatureUpdateUtility.isFeatureInstalled(id, caller)) {
			if (install) {
				FeatureUpdateUtility.installFeature(id, caller);
			} else {
				throw new OdysseusScriptException("This script requires feature " + id + " which is not installed!");
			}
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {

		return null;
	}

}
