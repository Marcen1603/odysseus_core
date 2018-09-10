/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.optimization.keyword;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * @author Dennis Nowak
 *
 */
public class ParallelizationOptimizationPreParserKeyword extends AbstractPreParserKeyword {

	public final static String KEYWORD = "REPARALLELIZE";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword#validate(java.
	 * util.Map, java.lang.String,
	 * de.uniol.inf.is.odysseus.core.usermanagement.ISession,
	 * de.uniol.inf.is.odysseus.core.collection.Context,
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.executor.
	 * IServerExecutor)
	 */
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword#execute(java.
	 * util.Map, java.lang.String,
	 * de.uniol.inf.is.odysseus.core.usermanagement.ISession,
	 * de.uniol.inf.is.odysseus.core.collection.Context,
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.executor.
	 * IServerExecutor)
	 */
	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller,
			Context context, IServerExecutor executor) throws OdysseusScriptException {
		List<IExecutorCommand> commandList = new ArrayList<>();
		IExecutorCommand command;
		try{
			int queryNumber = Integer.parseInt(parameter);
			command = new ReoptimizeParallelizationCommand(caller, queryNumber);
		} catch (NumberFormatException e) {
			command = new ReoptimizeParallelizationCommand(caller, parameter);
		}
		commandList.add(command);
		return commandList;
	}

}
