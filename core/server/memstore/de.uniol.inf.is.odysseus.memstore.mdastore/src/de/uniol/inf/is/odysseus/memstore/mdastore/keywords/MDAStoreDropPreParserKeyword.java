package de.uniol.inf.is.odysseus.memstore.mdastore.keywords;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.memstore.mdastore.commands.DropMDAStoreCommand;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class MDAStoreDropPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "MDASTORE_DROP";

	
	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		if (Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("MDAStore name is missing!");
		}		
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		List<IExecutorCommand> cmds = new LinkedList<>();
		cmds.add(new DropMDAStoreCommand(caller, parameter));
		return cmds;
	}

}
