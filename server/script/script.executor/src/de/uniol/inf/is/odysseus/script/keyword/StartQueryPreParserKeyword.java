package de.uniol.inf.is.odysseus.script.keyword;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StartQueryCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class StartQueryPreParserKeyword extends AbstractPreParserKeyword {

	public static final String NAME = "STARTQUERY";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		String name = parameter;
		List<IExecutorCommand> ret = new LinkedList<>();
		ret.add(new StartQueryCommand(caller, name));
		return ret;
	}

}
