package de.uniol.inf.is.odysseus.recovery.configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

// TODO javaDoc
public class RecoveryConfigKeyword extends AbstractPreParserKeyword {

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

	public static String getName() {
		return "RECOVERYCONFIGUTRATION";
	}

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		if (!getAllowedParameters(caller).contains(parameter)) {
			throw new OdysseusScriptException("'" + parameter
					+ "' is not a valid recovery configuaration!");
		}

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		variables.put(getName(), new RecoveryExecutorConfigSettingParameter(
				parameter));
		return null;
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return cExecutors.keySet();
	}

}