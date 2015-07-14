package de.uniol.inf.is.odysseus.recovery.configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.IRecoveryExecutor;
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
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(RecoveryConfigKeyword.class);

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
	 * @return A string to represent the keyword in Odysseus Script.
	 */
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
		variables.put(getName(), cExecutors.get(parameter));
		return null;
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return cExecutors.keySet();
	}

	/**
	 * Determines the recovery configuration from Odysseus Script.
	 * 
	 * @param script
	 *            The given script.
	 * @return A recovery executor, if the recovery configuration is set.
	 */
	public static Optional<IRecoveryExecutor> determineRecoveryExecutor(
			String script) {
		int beginKeyword = script.indexOf(RecoveryConfigKeyword.getName());
		if (beginKeyword >= 0) {
			int endLine = script.indexOf("\n", beginKeyword);
			if (endLine == -1) {
				endLine = script.length();
			}
			String keyWordPlusParameter = script.substring(beginKeyword,
					endLine);
			String parameter = keyWordPlusParameter.substring(
					RecoveryConfigKeyword.getName().length(),
					keyWordPlusParameter.length()).trim();
			if (cExecutors.containsKey(parameter)) {
				return Optional.of(cExecutors.get(parameter));
			} else {
				cLog.error("Unknown recovery executor: " + parameter);
			}
		}
		return Optional.absent();
	}

}