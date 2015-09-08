package de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.recovery.badast.ABaDaStRecorder;
import de.uniol.inf.is.odysseus.core.server.recovery.badast.IBaDaStRecorder;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.incomingelements.BaDaStSender;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

// XXX Don't know, why it is necessary to give the buffersize to TCPRecorders. Not nice.
/**
 * Keyword to create a BaDaSt recorder for a given source. <br />
 * Parameters are key value pairs with exactly two given keys:
 * {@value #KEY_TYPE} and {@value #KEY_SOURCENAME}. <br />
 * Such a keyword has to be executed after the source is published within the
 * {@code IDataDictionary}.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class BaDaStRecorderKeyword extends AbstractPreParserKeyword {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(BaDaStRecorderKeyword.class);

	/**
	 * All bound {@code IBaDaStRecorders} mapped to their type names.
	 */
	private static final Map<String, IBaDaStRecorder> cRecorders = Maps.newHashMap();

	/**
	 * Binds an implementation of {@code IBaDaStRecorder}.
	 * 
	 * @param recorder
	 *            An instance of the implementation.
	 */
	public static void bindRecorder(IBaDaStRecorder recorder) {
		Class<? extends IBaDaStRecorder> recorderClass = recorder.getClass();
		// Check annotation
		if (!recorderClass.isAnnotationPresent(ABaDaStRecorder.class)) {
			cLog.warn("{} could not be bound as {}, because it misses the annotation {}!",
					new Object[] { recorderClass.getSimpleName(), IBaDaStRecorder.class.getSimpleName(),
							ABaDaStRecorder.class.getSimpleName() });
		}
		String type = recorderClass.getAnnotation(ABaDaStRecorder.class).type();
		cRecorders.put(type, recorder);
	}

	/**
	 * Unbinds an implementation of {@code IBaDaStRecorder}.
	 * 
	 * @param recorder
	 *            An instance of the implementation.
	 */
	public static void unbindRecorder(IBaDaStRecorder recorder) {
		Class<? extends IBaDaStRecorder> recorderClass = recorder.getClass();
		// Check annotation
		if (recorderClass.isAnnotationPresent(ABaDaStRecorder.class)) {
			String type = recorderClass.getAnnotation(ABaDaStRecorder.class).type();
			cRecorders.remove(type);
		}
	}

	/**
	 * Gets the name.
	 * 
	 * @return A String representing the keyword.
	 */
	public static String getName() {
		return "BaDaStRecorder".toUpperCase();
	}

	/**
	 * The key for the recorder type key value pair.
	 */
	private static final String KEY_TYPE = "type";

	/**
	 * The key for the source name key value pair.
	 */
	private static final String KEY_SOURCENAME = "sourcename";

	/**
	 * Parses the Odysseus Script parameter.
	 *
	 * @param parameter
	 *            The parameter to parse.
	 * @return The value for {@link #KEY_TYPE} as E1 and the value for
	 *         {@link KEY_SOURCENAME} as E2.
	 * @throws OdysseusScriptException
	 *             if {@code parameter} is not a blank separated list of two key
	 *             value pairs or if the keys are wrong.
	 */
	private static IPair<String, String> parseParameter(String parameter) throws OdysseusScriptException {
		String typeValue, sourcenameValue;
		String[] args = parameter.split(" ");
		if (args.length != 2) {
			throw new OdysseusScriptException("BaDaStRecorderKeyword needs exactly two parameters!");
		}
		typeValue = parsePartialParameter(args[0], KEY_TYPE);
		sourcenameValue = parsePartialParameter(args[1], KEY_SOURCENAME);
		return new Pair<>(typeValue, sourcenameValue);
	}

	/**
	 * Parses a part of the Odysseus Script parameter.
	 *
	 * @param parameter
	 *            The partial parameter to parse.
	 * @param key
	 *            The expected key of the key value pair to parse.
	 * @return The value for {@code key}.
	 * @throws OdysseusScriptException
	 *             if {@code parameter} is not a key value pairs or if the key
	 *             is not {@code key}.
	 */
	private static String parsePartialParameter(String parameter, String key) throws OdysseusScriptException {
		if (!parameter.contains("=")) {
			throw new OdysseusScriptException(
					"BaDaStRecorderKeyword: " + parameter + " is not a valid key value argument! key=value");
		}
		String[] keyValue = parameter.split("=");
		if (keyValue.length != 2) {
			throw new OdysseusScriptException(
					"BaDaStRecorderKeyword: " + parameter + " is not a valid key value argument! key=value");
		} else if (!key.equals(keyValue[0].toLowerCase())) {
			throw new OdysseusScriptException("BaDaStRecorderKeyword misses the key '" + key + "'!");
		}
		return keyValue[1];
	}

	/**
	 * Retrieves the access to a given source from {@code IDataDictionary}.
	 * 
	 * @param sourcename
	 *            The name of the source.
	 * @param caller
	 *            The current session object.
	 * @return The {@code AbstractAccessAO} representing the stream or view.
	 * @throws OdysseusScriptException
	 *             if there is no stream or view within the
	 *             {@code IDataDictionary} with the given name.
	 */
	private static AbstractAccessAO getSourceAccess(String sourcename, ISession caller) throws OdysseusScriptException {
		IDataDictionary dd = DataDictionaryProvider.getDataDictionary(caller.getTenant());
		ILogicalOperator sourceAccess = dd.getViewOrStream(sourcename, caller);
		if (sourceAccess == null) {
			throw new OdysseusScriptException("BaDaStRecorderKeyword: " + sourcename + " is an unknown source name!");
		}
		// Note: this must not be the direct source, can be a rename or
		// something else.
		return getSourceAccessRecursive(new Resource(caller.getUser(), sourcename), sourceAccess);
	}

	/**
	 * Search recursively for an {@code AbstractAccessAO} with a given source
	 * name (depth-first-search).
	 * 
	 * @param sourcename
	 *            The given source name.
	 * @param topOperator
	 *            The operator to check and to search from towards sources.
	 * @return The found {@code AbstractAccessAO} or null.
	 */
	private static AbstractAccessAO getSourceAccessRecursive(Resource sourcename, ILogicalOperator topOperator) {
		if (AbstractAccessAO.class.isInstance(topOperator)
				&& ((AbstractAccessAO) topOperator).getAccessAOName().equals(sourcename)) {
			// Break condition success
			return (AbstractAccessAO) topOperator;
		}

		for (LogicalSubscription subToSource : topOperator.getSubscribedToSource()) {
			AbstractAccessAO sourceAccess = getSourceAccessRecursive(sourcename, subToSource.getTarget());
			if (sourceAccess != null) {
				// Break condition success
				return sourceAccess;
			}
		}

		// Break condition failure
		return null;
	}

	/**
	 * Puts together the configuration for the {@code IBaDaStRecorder} to
	 * create.
	 * 
	 * @param type
	 *            The type of the recorder.
	 * @param sourcename
	 *            The source name for the recorder.
	 * @param parameterKeys
	 *            All keys for the other needed parameters.
	 * @param sourceAccessParameters
	 *            The parameter mapping of the source access (
	 *            {@code ILogicalOperator}).
	 * @return The configuration as {@code Properties} object.
	 */
	private static Properties createRecorderConfig(String type, String sourcename, String[] parameterKeys,
			Map<String, String> sourceAccessParameters) {
		Properties config = new Properties();
		config.put(KEY_TYPE, type);
		config.put(KEY_SOURCENAME, sourcename);
		for (String key : parameterKeys) {
			config.put(key, sourceAccessParameters.get(key));
		}
		return config;
	}

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		// Parse type and source name (only parameters)
		IPair<String, String> typeAndSourcename = parseParameter(parameter);
		// Check, if recorder is bound
		if (!cRecorders.containsKey(typeAndSourcename.getE1())) {
			throw new OdysseusScriptException(
					"BaDaStRecorderKeyword: " + typeAndSourcename.getE1() + " is an unknown type of BaDaStRecorders!");
		}
		// Check, if DataDictionary contains source name
		AbstractAccessAO sourceAccess = getSourceAccess(typeAndSourcename.getE2(), caller);
		// Check, if source definition contains all needed options
		// (parameters of ABaDaStRecorder)
		String[] recorderParameters = cRecorders.get(typeAndSourcename.getE1()).getClass()
				.getAnnotation(ABaDaStRecorder.class).parameters();
		Set<String> sourceAccessParameters = sourceAccess.getOptionsMap().keySet();
		for (String recorderParameter : recorderParameters) {
			if (!sourceAccessParameters.contains(recorderParameter)) {
				throw new OdysseusScriptException("BaDaStRecorderKeyword: " + typeAndSourcename.getE2()
						+ " misses the parameter " + recorderParameter + "!");
			}
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller,
			Context context, IServerExecutor executor) throws OdysseusScriptException {
		// Parse type and source name (only parameters)
		IPair<String, String> typeAndSourcename = parseParameter(parameter);
		// Get the source from DataDictionary
		AbstractAccessAO sourceAccess = getSourceAccess(typeAndSourcename.getE2(), caller);
		// Check, if the recorder is already active in BaDaSt (would be the case
		// after a system error)
		String fullSourcename = new Resource(caller.getUser(), typeAndSourcename.getE2()).toString();
		String recorderName = BaDaStRecorderRegistry.getRecorder(fullSourcename);
		if (recorderName != null) {
			// Nothing to do. Recorder is running
			return null;
		}
		// Put together to configuration for the recorder
		String[] recorderParameters = cRecorders.get(typeAndSourcename.getE1()).getClass()
				.getAnnotation(ABaDaStRecorder.class).parameters();
		Map<String, String> sourceAccessParameters = sourceAccess.getOptionsMap();
		Properties config = createRecorderConfig(typeAndSourcename.getE1(), fullSourcename, recorderParameters,
				sourceAccessParameters);
		recorderName = BaDaStSender.sendCreateCommand(config);
		if (recorderName == null) {
			throw new OdysseusScriptException("BaDaStRecorderKeyword: Could not create BaDaSt recorder!");
		}
		BaDaStRecorderRegistry.register(fullSourcename, recorderName);
		BaDaStSender.sendStartCommand(recorderName);
		// XXX Execution should not be here but an IExecutorKeyword
		return null;
	}

}