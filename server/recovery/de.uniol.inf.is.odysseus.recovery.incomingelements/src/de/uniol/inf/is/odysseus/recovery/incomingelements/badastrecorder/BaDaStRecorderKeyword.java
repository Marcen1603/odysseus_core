package de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * Keyword to create a BaDaSt recorder for a given source. <br />
 * Parameters are key value pairs and must contain type=xyz. All other keys
 * depend on the chosen type.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStRecorderKeyword extends AbstractPreParserKeyword {

	/**
	 * Gets the name.
	 * 
	 * @return A String representing the keyword.
	 */
	public static String getName() {
		return "BaDaStRecorder".toUpperCase();
	}

	/**
	 * The key for the sourcename key value pair.
	 */
	private static final String KEY_SOURCENAME = "sourcename";

	/**
	 * Parses the Odysseus Script parameter.
	 * 
	 * @param parameter
	 *            The parameter to parse.
	 * @return An configuration filled with parsed key-value pairs.
	 * @throws OdysseusScriptException
	 *             if {@code parameter} is not a blank separated list of key
	 *             value pairs.
	 */
	private static Properties parseParameter(String parameter)
			throws OdysseusScriptException {
		Properties config = new Properties();
		String[] args = parameter.split(" ");
		for (String argument : args) {
			if (!argument.contains("=")) {
				throw new OdysseusScriptException(argument
						+ " is not a valid key value argument! key=value");
			}
			String[] keyValue = argument.split("=");
			config.put(keyValue[0].trim().toLowerCase(), keyValue[1].trim());
		}
		if (config.getProperty(KEY_SOURCENAME) == null) {
			throw new OdysseusScriptException("Missing key 'sourcename' for "
					+ getName());
		}
		return config;

	}

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		parseParameter(parameter);
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		Properties config = parseParameter(parameter);
		String sourcename = config.getProperty(KEY_SOURCENAME);
		String recorder = BaDaStRecorderRegistry.getRecorder(sourcename);
		if (recorder == null) {
			recorder = BaDaStSender.sendCreateCommand(config);
			if (recorder == null) {
				throw new OdysseusScriptException(
						"Could not create BaDaSt recorder!");
			}
			BaDaStRecorderRegistry.register(sourcename, recorder);
			BaDaStSender.sendStartCommand(recorder);
		}
		return null;
	}

}