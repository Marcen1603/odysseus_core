package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterRecoveryConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * The Odysseus Script keyword to set an {@link IRecoveryExecutor} for the
 * complete script. Note, that recovery will only be activated for the queries
 * within the script, and that it is not possible to set different
 * {@link IRecoveryExecutor}s within a single scrtipt.
 * 
 * @author Michael Brand
 *
 */
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

	/**
	 * Gets the name.
	 * 
	 * @return A string to represent the keyword in Odysseus Script.
	 */
	public static String getName() {
		return ParameterRecoveryConfiguration.keyword;
	}

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		Properties cfg = new Properties();
		String name = parseParameter(parameter, cfg);
		if (!getAllowedParameters(caller).contains(name)) {
			throw new OdysseusScriptException("'" + parameter
					+ "' is not a valid recovery configuaration!");
		}

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		Properties executorCfg = new Properties();
		String executorName = parseParameter(parameter, executorCfg);
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		settings.add(new ParameterRecoveryConfiguration(executorName,
				executorCfg));
		return null;
	}

	/**
	 * Parses the Odysseus Script parameter.
	 * 
	 * @param parameter
	 *            The parameter to parse.
	 * @param config
	 *            An (empty) configuration to be filled with pared key-value
	 *            pairs.
	 * @return The name of the {@link IRecoveryExecutor} to use, which is meant
	 *         within the parameter.
	 * @throws OdysseusScriptException
	 *             if any error occurs.
	 */
	private static String parseParameter(String parameter, Properties config)
			throws OdysseusScriptException {
		String[] args = parameter.split(" ");
		String name = args[0];
		for (int i = 1; i < args.length; i++) {
			String argument = args[i];
			if (!argument.contains("=")) {
				throw new OdysseusScriptException(argument
						+ " is not a valid key value argument! key=value");
			}
			String[] keyValue = argument.split("=");
			config.put(keyValue[0].trim().toLowerCase(), keyValue[1].trim());
		}
		return name;

	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return cExecutors.keySet();
	}

}