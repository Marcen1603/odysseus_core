package de.uniol.inf.is.odysseus.recovery.configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * The Odysseus-Script keyword to set an {@link IRecoveryExecutor} for a script.
 * 
 * @author Michael Brand
 *
 */
public class RecoveryExecutorPreParserKeyword extends AbstractPreParserKeyword {

	/**
	 * The name of the keyword to appear in Odysseus-Script.
	 */
	public static final String KEYWORD = "RECOVERY";

	/**
	 * All bound recovery executors.
	 */
	private static final Map<String, IRecoveryExecutor> cExecutors = Maps
			.newConcurrentMap();

	/**
	 * Binds a recovery executor.
	 * 
	 * @param executor
	 *            The recovery executor to bind.
	 */
	public static void bindRecoveryExecutor(IRecoveryExecutor executor) {
		cExecutors.put(executor.getName(), executor);
	}

	/**
	 * Unbinds a recovery executor.
	 * 
	 * @param executor
	 *            The recovery executor to unbind.
	 */
	public static void unbindRecoveryExecutor(IRecoveryExecutor executor) {
		cExecutors.remove(executor.getName());
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return cExecutors.keySet();
	}

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		if (Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException(
					"Recovery startegy name is missing!");
		}

		String executorName = parameter.trim();
		if (!getAllowedParameters(caller).contains(executorName)) {
			throw new OdysseusScriptException("Recovery startegy '"
					+ executorName + "' is not registered!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		// TODO implement RecoveryExecutorPreParserKeyword.execute
		return null;
	}

}