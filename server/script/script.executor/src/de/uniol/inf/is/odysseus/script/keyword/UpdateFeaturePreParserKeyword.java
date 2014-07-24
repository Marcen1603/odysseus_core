package de.uniol.inf.is.odysseus.script.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.updater.FeatureUpdateUtility;

public class UpdateFeaturePreParserKeyword extends AbstractPreParserKeyword {

	public static final String UPDATE = "UPDATE";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		FeatureUpdateUtility.checkForAndInstallUpdates(caller);
		return null;
	}
}
