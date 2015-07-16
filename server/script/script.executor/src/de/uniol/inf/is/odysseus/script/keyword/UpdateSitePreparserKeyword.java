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

public class UpdateSitePreparserKeyword extends AbstractPreParserKeyword {

	public static final String NAME = "UPDATESITE";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		// Must be done at validate, else the parser could have exceptions
		String params[] = getSimpleParameters(parameter);
		String id = params[0];
		FeatureUpdateUtility.setRepositoryLocation(id, caller);

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		return null;	
	}
}
