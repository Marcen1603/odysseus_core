package de.uniol.inf.is.odysseus.admission.status.keyword;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * The LoadSheddingPreParserKeyword manages the handling of Queries which allow load shedding.
 */
public class LoadSheddingPreParserKeyword extends AbstractPreParserKeyword {

	/**
	 * The load shedding keyword.
	 */
	public static String LOADSHEDDING = "LOADSHEDDING";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		//Nothing to validate
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller,
			Context context, IServerExecutor executor) throws OdysseusScriptException {
		
		// A new QParam is added.
		Map<String, String> qParams = new HashMap<String, String>();
		qParams.put("LOADSHEDDINGENABLED", "true");
		
		variables.put("QPARAM", qParams);
		return null;
	}

}
