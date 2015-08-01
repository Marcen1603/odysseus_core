package de.uniol.inf.is.odysseus.badast.internal;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Properties;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.badast.ABaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.AbstractBaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.BaDaStException;
import de.uniol.inf.is.odysseus.badast.IBaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.readers.FileRecorder;
import de.uniol.inf.is.odysseus.badast.readers.TCPRecorder;

/**
 * Provider of OSGi console commands for the BaDaSt application. <br />
 * <br />
 * That commands are the following:<br />
 * - lsRecorderTypes <br />
 * - lsRecorders <br />
 * - createRecorder <br />
 * - startRecorder <br />
 * - closeRecorder <br />
 * 
 * @author Michael Brand
 *
 */
public class BaDaStCommandProvider implements CommandProvider {

	/**
	 * The key for configuration, where the recorder type is set.
	 */
	public static final String TYPE_CONFIG = AbstractBaDaStRecorder.TYPE_CONFIG;

	/**
	 * The key for configuration, where the recorder writer name is set.
	 */
	public static final String NAME_CONFIG = AbstractBaDaStRecorder.NAME_CONFIG;

	/**
	 * The key for configuration, where the source name is set.
	 */
	public static final String SOURCENAME_CONFIG = AbstractBaDaStRecorder.SOURCENAME_CONFIG;

	/**
	 * All available recorder types. Key is the type, value is a not initialized
	 * recorder of that type.
	 */
	private static final Map<String, IBaDaStRecorder<?>> cRecorderTypes = Maps
			.newHashMap();

	// Fill the mapping with all recorder types, which are not bound by
	// OSGi
	static {
		IBaDaStRecorder<String> fileRecorder = new FileRecorder();
		cRecorderTypes.put(
				FileRecorder.class.getAnnotation(ABaDaStRecorder.class).type(),
				fileRecorder);
		IBaDaStRecorder<byte[]> tcpRecorder = new TCPRecorder();
		cRecorderTypes.put(
				TCPRecorder.class.getAnnotation(ABaDaStRecorder.class).type(),
				tcpRecorder);
	}

	/**
	 * Binds a recorder.
	 * 
	 * @param recorder
	 *            The implementation of {@link IBaDaStRecorder} with an unique
	 *            type to bind.
	 */
	public static void bindReader(IBaDaStRecorder<?> recorder) {
		cRecorderTypes
				.put(recorder.getClass().getAnnotation(ABaDaStRecorder.class)
						.type(), recorder);
	}

	/**
	 * Uninds a recorder.
	 * 
	 * @param recorder
	 *            The implementation of {@link IBaDaStRecorder} to unbind.
	 */
	public static void unbindReader(IBaDaStRecorder<?> recorder) {
		cRecorderTypes.remove(recorder.getClass()
				.getAnnotation(ABaDaStRecorder.class).type());
	}

	/**
	 * All available (already created) recorder (initialized recorders, not
	 * types). Key is the name, value is an initialized recorder with that name.
	 */
	private static final Map<String, IBaDaStRecorder<?>> cRecorders = Maps
			.newHashMap();

	/**
	 * Lists all available recorder types.
	 * 
	 * @param ci
	 *            No arguments needed.
	 */
	public void _lsRecorderTypes(CommandInterpreter ci) {
		ci.println("Available types of BaDaSt recorders:\n");
		for (String type : cRecorderTypes.keySet()) {
			ci.print(TYPE_CONFIG + " = " + type + ", parameters = ");
			String[] params = cRecorderTypes.get(type).getClass()
					.getAnnotation(ABaDaStRecorder.class).parameters();
			if (params.length == 0) {
				ci.print("None");
			} else {
				for (int i = 0; i < params.length; i++) {
					ci.print(params[i]);
					if (i < params.length - 1) {
						ci.print(", ");
					}
				}
			}
			ci.println();
		}
	}

	/**
	 * Lists all available (already created) recorders (initialized recorders,
	 * not types).
	 * 
	 * @param ci
	 *            No arguments needed.
	 */
	public void _lsRecorders(CommandInterpreter ci) {
		ci.println("Available BaDaSt recorders:\n");
		if (cRecorders.isEmpty()) {
			ci.println("None");
			return;
		}
		for (String name : cRecorders.keySet()) {
			ci.println(NAME_CONFIG
					+ " = "
					+ name
					+ ", "
					+ TYPE_CONFIG
					+ " = "
					+ cRecorders.get(name).getConfig()
							.getProperty(AbstractBaDaStRecorder.TYPE_CONFIG));
		}
	}

	/**
	 * Creates and initializes a new recorder.
	 * 
	 * @param ci
	 *            All arguments should be key value pairs (key=value). Any key
	 *            should be {@link #TYPE_CONFIG} and all other needed keys
	 *            depend on the recorder type.
	 */
	public void _createRecorder(CommandInterpreter ci) {
		Properties cfg = new Properties();
		String argument;
		while ((argument = ci.nextArgument()) != null) {
			if (!argument.contains("=")) {
				ci.println(argument
						+ " is not a valid key value argument! key=value");
			}
			String[] keyValue = argument.split("=");
			cfg.put(keyValue[0].trim().toLowerCase(), keyValue[1].trim());
		}

		try {
			String type = cfg.getProperty(TYPE_CONFIG);
			validateTypeShallExist(type);
			IBaDaStRecorder<?> reader = cRecorderTypes.get(type).newInstance(
					cfg);
			validateNameShallNotExist(reader.getName());
			cRecorders.put(reader.getName(), reader);
			ci.println("Created BaDaSt recorder " + reader.getName());
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
	private void validateTypeShallExist(String type) throws BaDaStException {
		if (type == null) {
			throw new BaDaStException("Recorder type is not set!");
		} else if (!cRecorderTypes.keySet().contains(type)) {
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
	private void validateNameShallExist(String name) throws BaDaStException {
		if (!cRecorders.keySet().contains(name)) {
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
	private void validateNameShallNotExist(String name) throws BaDaStException {
		if (cRecorders.keySet().contains(name)) {
			throw new BaDaStException(name + " is an already created recorder!");
		}
	}

	/**
	 * Reads a recorder name from a given key value argument.
	 * 
	 * @param keyValueArgument
	 *            The given key value argument.
	 * @return The set name.
	 * @throws BaDaStException
	 *             if the argument is null or if it does not match "key=value",
	 *             where key is {@link #NAME_CONFIG}.
	 */
	private String readName(String keyValueArgument) throws BaDaStException {
		if (keyValueArgument == null) {
			throw new BaDaStException("Key value argument is null!");
		}
		if (!keyValueArgument.contains("=")) {
			throw new BaDaStException(keyValueArgument
					+ " is not a valid key value argument! key=value");
		}
		String[] keyValue = keyValueArgument.split("=");
		if (!keyValue[0].trim().equals(NAME_CONFIG)) {
			throw new BaDaStException(keyValueArgument
					+ " is not a valid key value argument for recorder names! "
					+ NAME_CONFIG + "=value");
		}
		return keyValue[1].trim();
	}

	/**
	 * Reads a source name from a given key value argument.
	 * 
	 * @param keyValueArgument
	 *            The given key value argument.
	 * @return The set source name.
	 * @throws BaDaStException
	 *             if the argument is null or if it does not match "key=value",
	 *             where key is {@link #SOURCENAME_CONFIG}.
	 */
	private String readSourceName(String keyValueArgument)
			throws BaDaStException {
		if (keyValueArgument == null) {
			throw new BaDaStException("Key value argument is null!");
		}
		if (!keyValueArgument.contains("=")) {
			throw new BaDaStException(keyValueArgument
					+ " is not a valid key value argument! key=value");
		}
		String[] keyValue = keyValueArgument.split("=");
		if (!keyValue[0].trim().equals(SOURCENAME_CONFIG)) {
			throw new BaDaStException(keyValueArgument
					+ " is not a valid key value argument for source names! "
					+ SOURCENAME_CONFIG + "=value");
		}
		return keyValue[1].trim();
	}

	/**
	 * Starts an existing recorder.
	 * 
	 * @param ci
	 *            Should contain one key value argument "key=value", where key
	 *            is {@link #NAME_CONFIG}.
	 */
	public void _startRecorder(final CommandInterpreter ci) {
		try {
			final String name = readName(ci.nextArgument());
			validateNameShallExist(name);
			new Thread(name) {

				@Override
				public void run() {
					try {
						cRecorders.get(name).start();
					} catch (BaDaStException e) {
						ci.println(e.getMessage());
					}
				}

			}.start();
			ci.println("Started BaDaSt recorder " + name);
		} catch (BaDaStException e) {
			ci.println(e.getMessage());
		}
	}

	/**
	 * Closes and removes an existing recorder.
	 * 
	 * @param ci
	 *            Should contain one key value argument "key=value", where key
	 *            is {@link #NAME_CONFIG}.
	 */
	public void _closeRecorder(CommandInterpreter ci) {
		String name;
		try {
			name = readName(ci.nextArgument());
			validateNameShallExist(name);
		} catch (BaDaStException e) {
			ci.println(e.getMessage());
			return;
		}

		try {
			cRecorders.get(name).close();
		} catch (Exception e) {
			ci.print("Could not close recorder!");
			ci.println(e.getMessage());
		} finally {
			cRecorders.remove(name);
			ci.println("Removed BaDaSt recorder " + name);
		}
	}

	/**
	 * Consumes a given topic from the Kafka server.
	 * 
	 * @param ci
	 *            Should contain one argument "key=value", where key is
	 *            {@link #SOURCENAME_CONFIG}.
	 */
	public void _consume(final CommandInterpreter ci) {
		final String clientid = "OSGiConsoleClient";
		try {
			final String sourcename = readSourceName(ci.nextArgument());
			new Thread(sourcename) {

				@Override
				public void run() {
					SimpleConsumer consumer = new SimpleConsumer("localhost", 9092,
							100000, 64 * 1024, clientid);
					long readOffset = 0;
					FetchRequest req = new FetchRequestBuilder()
							.clientId(clientid)
							.addFetch(sourcename, 0, 0, 100000).build();
					FetchResponse fetchResponse = consumer.fetch(req);

					long numRead = 0;
					for (MessageAndOffset messageAndOffset : fetchResponse
							.messageSet(sourcename, 0)) {
						long currentOffset = messageAndOffset.offset();
						if (currentOffset < readOffset) {
							System.out.println("Found an old offset: "
									+ currentOffset + " Expecting: "
									+ readOffset);
							continue;
						}
						readOffset = messageAndOffset.nextOffset();
						ByteBuffer payload = messageAndOffset.message()
								.payload();

						byte[] bytes = new byte[payload.limit()];
						payload.get(bytes);
						try {
							System.out.println(String.valueOf(messageAndOffset
									.offset())
									+ ": "
									+ new String(bytes, "UTF-8"));
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
			ci.println("Reading the backup of source " + sourcename
					+ " finished");
		} catch (BaDaStException e) {
			ci.println(e.getMessage());
		}
	}

	@Override
	public String getHelp() {
		final String TAB = "	";
		StringBuffer out = new StringBuffer(
				"---Backup of Data Streams (BaDaSt) Commands---\n");
		out.append(TAB
				+ "lsRecorderTypes - Lists all registered BaDaSt recorders types.\n");
		out.append(TAB + "lsRecorders - Lists all created BaDaSt recorders.\n");
		out.append(TAB
				+ "createRecorder type=xyz key1=value1 ... keyn=valuen - Creates a new BaDaSt recorder with the type of the recorder and the needed parameters for that type all as key value pairs. Needed parameters can be viewed with lsRecorderTypes\n");
		out.append(TAB
				+ "startRecorder name=xyz - Starts an existing BaDaSt recorder with the name of the recorder as a key value pair.\n");
		out.append(TAB
				+ "closeRecorder name=xyz - Closes and removes an existing BaDaSt recorder with the name of the recorder as a key value pair.\n");
		out.append(TAB
				+ "consume sourcename=xyz - Reads the backup of a given source from the Kafka server with the name of the source as a key value pair.\n");
		return out.toString();
	}

}