package de.uniol.inf.is.odysseus.badast.application;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.badast.BaDaStException;
import de.uniol.inf.is.odysseus.badast.recorder.ABaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.recorder.AbstractBaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.recorder.IBaDaStRecorder;
import de.uniol.inf.is.odysseus.core.server.console.Help;
import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

/**
 * Provider of OSGi console commands for the BaDaSt application. <br />
 * <br />
 * That commands are the following:<br />
 * - startBaDaSt <br />
 * - lsRecorderTypes <br />
 * - lsRecorders <br />
 * - createRecorder <br />
 * - startRecorder <br />
 * - closeRecorder <br />
 * - startBaDaSt <br />
 * - stopBaDaSt <br />
 *
 * @author Michael Brand
 *
 */
public class BaDaStCommandProvider implements CommandProvider {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BaDaStCommandProvider.class);

	/**
	 * The key for configuration, where the command to execute is set.
	 */
	protected static final String COMMAND_CONFIG = "command";

	/**
	 * All bound {@code IBaDaStRecorders} mapped to their type names.
	 */
	private static final Map<String, IBaDaStRecorder> recorderTypes = new HashMap<>();

	/**
	 * Binds an implementation of {@code IBaDaStRecorder}.
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
		recorderTypes.put(type, recorder);
	}

	/**
	 * Unbinds an implementation of {@code IBaDaStRecorder}.
	 */
	public static void unbindRecorder(IBaDaStRecorder recorder) {
		Class<? extends IBaDaStRecorder> recorderClass = recorder.getClass();
		// Check annotation
		if (recorderClass.isAnnotationPresent(ABaDaStRecorder.class)) {
			String type = recorderClass.getAnnotation(ABaDaStRecorder.class).type();
			recorderTypes.remove(type);
		}
	}

	/**
	 * All available (already created) recorder (initialized recorders, not
	 * types). Key is the name, value is an initialized recorder with that name.
	 */
	static final Map<String, IBaDaStRecorder> recorders = new HashMap<>();

	/**
	 * Lists all available recorder types.
	 */
	private static String lsRecorderTypes() {
		StringBuffer out = new StringBuffer();
		for (String type : recorderTypes.keySet()) {
			out.append(IBaDaStRecorder.TYPE_CONFIG + " = " + type + ", parameters = ");
			String[] params = recorderTypes.get(type).getClass().getAnnotation(ABaDaStRecorder.class).parameters();
			if (params.length == 0) {
				out.append("None");
			} else {
				for (int i = 0; i < params.length; i++) {
					out.append(params[i]);
					if (i < params.length - 1) {
						out.append(", ");
					}
				}
			}
			out.append("\n");
		}
		return out.toString();
	}

	/**
	 * Lists all available recorder types.
	 */
	@Help(description = "Lists all registered BaDaSt recorders types.")
	public void _lsRecorderTypes(CommandInterpreter ci) {
		ci.println("Available types of BaDaSt recorders:\n");
		ci.println(lsRecorderTypes());
	}

	/**
	 * Lists all available (already created) recorders (initialized recorders,
	 * not types).
	 */
	private static String lsRecorders() {
		StringBuffer out = new StringBuffer();
		for (String name : recorders.keySet()) {
			out.append("name = " + name + ", " + IBaDaStRecorder.TYPE_CONFIG + " = "
					+ recorders.get(name).getConfig().getProperty(AbstractBaDaStRecorder.TYPE_CONFIG) + "\n");
		}
		return out.toString();
	}

	/**
	 * Lists all available (already created) recorders (initialized recorders,
	 * not types).
	 */
	@Help(description = "Lists all created BaDaSt recorders.")
	public void _lsRecorders(CommandInterpreter ci) {
		ci.println("Available BaDaSt recorders:\n");
		if (recorders.isEmpty()) {
			ci.println("None");
		} else {
			ci.println(lsRecorders());
		}
	}

	/**
	 * Parses arguments.
	 *
	 * @param ci
	 *            All arguments should be key value pairs (key=value).
	 */
	private static Properties parse(CommandInterpreter ci) throws BaDaStException {
		Set<String> arguments = new HashSet<>();
		String argument;
		while ((argument = ci.nextArgument()) != null) {
			arguments.add(argument);
		}
		String[] args = new String[arguments.size()];
		arguments.toArray(args);
		return parse(args);
	}

	/**
	 * Parses arguments.
	 *
	 * @param line
	 *            All arguments should be key value pairs (key=value), each
	 *            separated by a blank.
	 */
	public static Properties parse(String line) throws BaDaStException {
		String[] args = line.trim().split(" ");
		return parse(args);
	}

	/**
	 * Parses arguments.
	 *
	 * @param line
	 *            All arguments should be key value pairs (key=value).
	 */
	private static Properties parse(String[] args) throws BaDaStException {
		Properties cfg = new Properties();
		for (String argument : args) {
			if (!argument.contains("=")) {
				throw new BaDaStException(argument + " is not a valid key value argument! key=value");
			}
			String[] keyValue = argument.split("=");
			cfg.put(keyValue[0].trim().toLowerCase(), keyValue[1].trim());
		}
		return cfg;
	}

	/**
	 * Creates and initializes a new recorder.
	 *
	 * @param cfg
	 *            Any key should be {@link #TYPE_CONFIG} and all other needed
	 *            keys depend on the recorder type.
	 * @return A success message or failure message.
	 */
	private static String createRecorder(Properties cfg) {

		String type;
		try {
			type = cfg.getProperty(IBaDaStRecorder.TYPE_CONFIG);
			validateTypeShallExist(type);
		} catch (BaDaStException e) {
			return e.getMessage();
		}

		StringBuffer out = new StringBuffer();
		try (IBaDaStRecorder recorder = recorderTypes.get(type).newInstance(cfg)) {
			validateNameShallNotExist(recorder.getName());
			recorders.put(recorder.getName(), recorder);
			out.append("Created BaDaSt recorder " + recorder.getName());
		} catch (Exception e) {
			return e.getMessage();
		}
		return out.toString();
	}

	/**
	 * Creates and initializes a new recorder.
	 *
	 * @param ci
	 *            All arguments should be key value pairs (key=value). Any key
	 *            should be {@link #TYPE_CONFIG} and all other needed keys
	 *            depend on the recorder type.
	 */
	@Help(description = "Creates a new BaDaSt recorder with the type of the recorder and the needed parameters for that type all as key value pairs. Needed parameters can be viewed with lsRecorderTypes.", parameter = "type=xyz key1=value1 ... keyn=valuen")
	public void _createRecorder(CommandInterpreter ci) {
		try {
			ci.println(createRecorder(parse(ci)));
		} catch (BaDaStException e) {
			ci.println(e.getMessage());
		}
	}

	/**
	 * Checks, if a given recorder type is known.
	 *
	 * @param type
	 *            The type to check.
	 * @throws BaDaStException
	 *             if {@link #cRecorderTypes} does not contain the type as a
	 *             key.
	 */
	private static void validateTypeShallExist(String type) throws BaDaStException {
		if (type == null) {
			throw new BaDaStException("Recorder type is not set!");
		} else if (!recorderTypes.keySet().contains(type)) {
			throw new BaDaStException(type + " is not a known recorder type!");
		}
	}

	/**
	 * Checks, if a given recorder name is known.
	 *
	 * @param name
	 *            The name to check.
	 * @throws BaDaStException
	 *             if {@link #cRecorders} does not contain the name as a key.
	 */
	private static void validateNameShallExist(String name) throws BaDaStException {
		if (!recorders.keySet().contains(name)) {
			throw new BaDaStException(name + " is not a known recorder!");
		}
	}

	/**
	 * Checks, if a given recorder type is still free to use.
	 *
	 * @param name
	 *            The name to check.
	 * @throws BaDaStException
	 *             if {@link #cRecorders} does contain the type as a key.
	 */
	private static void validateNameShallNotExist(String name) throws BaDaStException {
		if (recorders.keySet().contains(name)) {
			throw new BaDaStException(name + " is an already created recorder!");
		}
	}

	/**
	 * Starts an existing recorder.
	 *
	 * @param ci
	 *            Should contain one key value argument "key=value", where key
	 *            is {@link #NAME_CONFIG}.
	 * @return A success message or failure message.
	 */
	private static String startRecorder(Properties cfg) {
		final StringBuffer out = new StringBuffer();
		try {
			final String name = cfg.getProperty("name");
			validateNameShallExist(name);
			new Thread(name) {

				@Override
				public void run() {
					try {
						recorders.get(name).start();
					} catch (BaDaStException e) {
						e.printStackTrace();
					}
				}

			}.start();
			out.append("Started BaDaSt recorder " + name);
		} catch (BaDaStException e) {
			out.append(e.getMessage());
		}
		return out.toString();
	}

	/**
	 * Starts an existing recorder.
	 *
	 * @param ci
	 *            Should contain one key value argument "key=value", where key
	 *            is {@link #NAME_CONFIG}.
	 */
	@Help(description = "Starts an existing BaDaSt recorder with the name of the recorder as a key value pair.", parameter = "name=xyz")
	public void _startRecorder(final CommandInterpreter ci) {
		try {
			ci.println(startRecorder(parse(ci)));
		} catch (BaDaStException e) {
			ci.println(e.getMessage());
		}
	}

	/**
	 * Closes and removes an existing recorder.
	 *
	 * @param cfg
	 *            Should contain one key value argument "key=value", where key
	 *            is {@link #NAME_CONFIG}.
	 * @return A success message or failure message.
	 */
	private static String closeRecorder(Properties cfg) {
		StringBuffer out = new StringBuffer();
		String name = cfg.getProperty("name");
		try {
			validateNameShallExist(name);
		} catch (BaDaStException e) {
			out.append(e.getMessage());
			return out.toString();
		}

		try {
			recorders.get(name).close();
		} catch (Exception e) {
			out.append("Could not close recorder! " + e.getMessage() + "\n");
		} finally {
			recorders.remove(name);
			out.append("Removed BaDaSt recorder " + name);
		}
		return out.toString();
	}

	/**
	 * Closes and removes an existing recorder.
	 *
	 * @param ci
	 *            Should contain one key value argument "key=value", where key
	 *            is {@link #NAME_CONFIG}.
	 */
	@Help(description = "Closes and removes an existing BaDaSt recorder with the name of the recorder as a key value pair.", parameter = "name=xyz")
	public void _closeRecorder(CommandInterpreter ci) {
		try {
			ci.println(closeRecorder(parse(ci)));
		} catch (BaDaStException e) {
			ci.println(e.getMessage());
		}
	}

	/**
	 * Closes and removes all existing recorders.
	 *
	 * @param ci
	 *            Should contain no argument.
	 */
	@Help(description = "Closes and removes all existing BaDaSt recorders.", parameter = "none")
	public void _closeAllRecorders(CommandInterpreter ci) {
		recorders.values().stream().forEach(recorder -> {
			try {
				recorder.close();
			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		});
		recorders.clear();
	}

	@Help(description = "Starts the BaDaSt application.")
	public void _startBaDaSt(CommandInterpreter ci) {
		try {
			BaDaStApplication.getInstance().start();
		} catch (Exception e) {
			ci.println("Could not start BaDaSt application!");
			ci.println(e.getMessage());
		}
	}

	@Help(description = "Stops the BaDaSt application.")
	public void _stopBaDaSt(CommandInterpreter ci) {
		try {
			BaDaStApplication.getInstance().stop();
		} catch (Exception e) {
			ci.println("Could not stop BaDaSt application!");
			ci.println(e.getMessage());
		}
	}

	@Override
	public String getHelp() {
		final String TAB = "        ";
		StringBuffer out = new StringBuffer("---Backup of Data Streams (BaDaSt) Commands---\n");
		// Code from OdysseusConsole
		TreeMap<String, Help> methodHelps = new TreeMap<String, Help>();
		for (Method method : this.getClass().getMethods()) {
			String methodName = method.getName();

			if (methodName.startsWith("_")) {
				if (method.isAnnotationPresent(Help.class)) {
					methodHelps.put(methodName.substring(1), method.getAnnotation(Help.class));
				}
			}
		}
		for (Map.Entry<String, Help> curHelp : methodHelps.entrySet()) {
			out.append(TAB);
			out.append(curHelp.getKey());
			Help help = curHelp.getValue();
			if (!help.parameter().isEmpty()) {
				out.append(" ");
				out.append(help.parameter());
			}
			out.append(" - ");
			out.append(help.description());
			out.append("\n");
		}
		return out.toString();
	}

	/**
	 * Executes a given BaDaSt command
	 *
	 * @param command
	 *            The command: either lsRecorderTypes, lsRecorders,
	 *            createRecorder, startRecorder or closeRecorder.
	 * @param args
	 *            The arguments for the command.
	 * @return A success or failure message.
	 * @throws BaDaStException
	 *             if the command in not known.
	 */
	public static String execute(String command, Properties args) throws BaDaStException {
		switch (command) {
		case "lsRecorderTypes":
			return lsRecorderTypes();
		case "lsRecorders":
			return lsRecorders();
		case "createRecorder":
			return createRecorder(args);
		case "startRecorder":
			return startRecorder(args);
		case "closeRecorder":
			return closeRecorder(args);
		default:
			throw new BaDaStException(command + " is an unknown BaDaSt command!");
		}
	}

	/**
	 * Consumes a given topic from the Kafka server.
	 *
	 * @param ci
	 *            Should contain two arguments "key=value", where key 1 is
	 *            {@link #SOURCENAME_CONFIG} and key 2 is {@code offset}.
	 */
	@Help(description = "Reads the backup of a given source from the Kafka server with the name of the source as a key value pair.", parameter = "sourcename=xyz offset=i")
	public void _consumeFromKafka(final CommandInterpreter ci) {
		try {
			final String clientid = "OSGiConsoleClient";
			Properties cfg = parse(ci);
			final String sourcename = cfg.getProperty(IBaDaStRecorder.SOURCENAME_CONFIG);
			final long offset = Long.parseLong(cfg.getProperty("offset", "0"));
			new Thread(sourcename) {

				@Override
				public void run() {
					SimpleConsumer consumer = new SimpleConsumer("localhost", 9092, 100000, 64 * 1024, clientid);
					long readOffset = offset;
					FetchRequest req = new FetchRequestBuilder().clientId(clientid)
							.addFetch(sourcename, 0, readOffset, 100000).build();
					FetchResponse fetchResponse = consumer.fetch(req);

					long numRead = 0;
					for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(sourcename, 0)) {
						long currentOffset = messageAndOffset.offset();
						if (currentOffset < readOffset) {
							System.out.println("Found an old offset: " + currentOffset + " Expecting: " + readOffset);
							continue;
						}
						readOffset = messageAndOffset.nextOffset();
						ByteBuffer payload = messageAndOffset.message().payload();

						byte[] bytes = new byte[payload.limit()];
						payload.get(bytes);
						try {
							System.out.println(
									String.valueOf(messageAndOffset.offset()) + ": " + new String(bytes, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							ci.print("Could not encode message!");
							ci.println(e.getMessage());
						}
						numRead++;
					}

					if (numRead == 0) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException ie) {
							ci.println(ie.getMessage());
						}
					}
				}

			}.start();
			ci.println("Reading the backup of source " + sourcename + " finished");
		} catch (BaDaStException e) {
			ci.println(e.getMessage());
		}
	}

}