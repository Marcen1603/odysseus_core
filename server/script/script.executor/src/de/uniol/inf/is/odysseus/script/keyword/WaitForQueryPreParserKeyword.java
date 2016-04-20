package de.uniol.inf.is.odysseus.script.keyword;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.WaitForQueryCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class WaitForQueryPreParserKeyword extends AbstractPreParserKeyword {

	public static final String NAME = "WAITFORQUERY";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		String[] para = getSimpleParameters(parameter);
		Resource name = Resource.specialCreateResource(para[0], caller.getUser());
		long testPeriod = 1000;
		if (para.length > 1&&  para[1].length() > 0){
			testPeriod = Long.parseLong(para[1]);
		}
		long maxWaitingTime = -1;
		if (para.length > 2){
			maxWaitingTime = Long.parseLong(para[2]);
		}
		List<IExecutorCommand> ret = new LinkedList<>();
		ret.add(new WaitForQueryCommand(caller, name, testPeriod,maxWaitingTime));
		return ret;
	}

}
