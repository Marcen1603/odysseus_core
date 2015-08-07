package de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.configuration;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.BaDaStRecorderRegistry;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.BaDaStSender;
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
		String sourcename = config.getProperty("sourcename");
		String recorder = BaDaStSender.sendCreateCommand(config);
		BaDaStRecorderRegistry.register(sourcename, recorder);
		return null;
	}

}