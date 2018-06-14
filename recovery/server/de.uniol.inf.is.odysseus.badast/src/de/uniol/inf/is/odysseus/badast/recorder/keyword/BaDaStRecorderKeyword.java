package de.uniol.inf.is.odysseus.badast.recorder.keyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.badast.recorder.ABaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.recorder.CreateBaDaStRecorderCommand;
import de.uniol.inf.is.odysseus.badast.recorder.IBaDaStRecorder;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;

/**
 * Keyword to create a BaDaSt recorder for a given source. <br />
 * Parameters are key value pairs with exactly two given keys:
 * {@link IBaDaStRecorder#TYPE_CONFIG} and
 * {@link IBaDaStRecorder#SOURCENAME_CONFIG}. <br />
 * Such a keyword has to be executed after the source is published within the
 * {@code IDataDictionary}.
 *
 * @author Michael Brand
 *
 */
public class BaDaStRecorderKeyword extends AbstractPreParserKeyword {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BaDaStRecorderKeyword.class);

	/**
	 * The name of the keyword.
	 */
	private static final String NAME = "BADASTRECORDER";

	/**
	 * All bound {@code IBaDaStRecorders} mapped to their type names.
	 */
	private static final Map<String, IBaDaStRecorder> recorders = new HashMap<>();

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
			LOG.warn("{} could not be bound as {}, because it misses the annotation {}!",
					new Object[] { recorderClass.getSimpleName(), IBaDaStRecorder.class.getSimpleName(),
							ABaDaStRecorder.class.getSimpleName() });
		}
		String type = recorderClass.getAnnotation(ABaDaStRecorder.class).type();
		recorders.put(type, recorder);
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
			recorders.remove(type);
		}
	}

	/**
	 * Gets the name.
	 *
	 * @return A String representing the keyword.
	 */
	public static String getName() {
		return NAME;
	}

	/**
	 * Parses the Odysseus Script parameter.
	 *
	 * @param parameter
	 *            The parameter to parse.
	 * @return The values for {@link IBaDaStRecorder#TYPE_CONFIG} as first
	 *         element, {@link IBaDaStRecorder#SOURCENAME_CONFIG}} as second
	 *         element, and {@link IBaDaStRecorder#VIEWNAME_CONFIG}} as third
	 *         element.
	 * @throws DataDictionaryException
	 *             if {@code parameter} is not a blank separated list of three key
	 *             value pairs or if the keys are wrong.
	 */
	private static List<String> parseParameter(String parameter) throws DataDictionaryException {
		List<String> out = new ArrayList<>();
		String[] args = parameter.split(" ");
		if (args.length != 3) {
			throw new DataDictionaryException("BaDaStRecorderKeyword needs exactly three parameters!");
		}
		out.add(parsePartialParameter(args[0], IBaDaStRecorder.TYPE_CONFIG));
		out.add(parsePartialParameter(args[1], IBaDaStRecorder.SOURCENAME_CONFIG));
		out.add(parsePartialParameter(args[2], IBaDaStRecorder.VIEWNAME_CONFIG));
		return out;
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
		// Parse type, source and view name (only parameters)
		List<String> typeSourceAndViewName = parseParameter(parameter);
		// Check, if recorder is bound
		if (!recorders.containsKey(typeSourceAndViewName.get(0))) {
			throw new DataDictionaryException(
					"BaDaStRecorderKeyword: " + typeSourceAndViewName.get(0) + " is an unknown type of BaDaStRecorders!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller,
			Context context, IServerExecutor executor) throws DataDictionaryException {
		// Parse type, source and view name (only parameters)
		List<String> typeSourceAndViewName = parseParameter(parameter);
		Resource sourcename = new Resource(caller.getUser(), typeSourceAndViewName.get(1));
		Resource viewname = new Resource(caller.getUser(), typeSourceAndViewName.get(2));
		List<String> recorderParameters = Arrays.asList(
				recorders.get(typeSourceAndViewName.get(0)).getClass().getAnnotation(ABaDaStRecorder.class).parameters());
		return Collections.singletonList((IExecutorCommand) new CreateBaDaStRecorderCommand(caller,
				typeSourceAndViewName.get(0), sourcename, viewname, recorderParameters));
	}

}