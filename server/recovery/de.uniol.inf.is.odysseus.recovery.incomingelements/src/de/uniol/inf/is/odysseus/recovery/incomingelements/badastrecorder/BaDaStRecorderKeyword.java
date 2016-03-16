package de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;

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
	static final String KEY_TYPE = "type";

	/**
	 * The key for the source name key value pair.
	 */
	static final String KEY_SOURCENAME = "sourcename";

	/**
	 * Parses the Odysseus Script parameter.
	 *
	 * @param parameter
	 *            The parameter to parse.
	 * @return The value for {@link #KEY_TYPE} as E1 and the value for
	 *         {@link KEY_SOURCENAME} as E2.
	 * @throws DataDictionaryException
	 *             if {@code parameter} is not a blank separated list of two key
	 *             value pairs or if the keys are wrong.
	 */
	private static IPair<String, String> parseParameter(String parameter) throws DataDictionaryException {
		String typeValue, sourcenameValue;
		String[] args = parameter.split(" ");
		if (args.length != 2) {
			throw new DataDictionaryException("BaDaStRecorderKeyword needs exactly two parameters!");
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
	 * @throws DataDictionaryException
	 *             if {@code parameter} is not a key value pairs or if the key
	 *             is not {@code key}.
	 */
	private static String parsePartialParameter(String parameter, String key) throws DataDictionaryException {
		if (!parameter.contains("=")) {
			throw new DataDictionaryException(
					"BaDaStRecorderKeyword: " + parameter + " is not a valid key value argument! key=value");
		}
		String[] keyValue = parameter.split("=");
		if (keyValue.length != 2) {
			throw new DataDictionaryException(
					"BaDaStRecorderKeyword: " + parameter + " is not a valid key value argument! key=value");
		} else if (!key.equals(keyValue[0].toLowerCase())) {
			throw new DataDictionaryException("BaDaStRecorderKeyword misses the key '" + key + "'!");
		}
		return keyValue[1];
	}

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context,
			IServerExecutor executor) throws DataDictionaryException {
		// Parse type and source name (only parameters)
		IPair<String, String> typeAndSourcename = parseParameter(parameter);
		// Check, if recorder is bound
		if (!cRecorders.containsKey(typeAndSourcename.getE1())) {
			throw new DataDictionaryException(
					"BaDaStRecorderKeyword: " + typeAndSourcename.getE1() + " is an unknown type of BaDaStRecorders!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller,
			Context context, IServerExecutor executor) throws DataDictionaryException {
		// Parse type and source name (only parameters)
		IPair<String, String> typeAndSourcename = parseParameter(parameter);
		Resource sourcename = new Resource(caller.getUser(), typeAndSourcename.getE2());
		List<String> recorderParameters = Arrays.asList(
				cRecorders.get(typeAndSourcename.getE1()).getClass().getAnnotation(ABaDaStRecorder.class).parameters());
		return Collections.singletonList((IExecutorCommand) new CreateBaDaStRecorderCommand(caller,
				typeAndSourcename.getE1(), sourcename, recorderParameters));
	}

}