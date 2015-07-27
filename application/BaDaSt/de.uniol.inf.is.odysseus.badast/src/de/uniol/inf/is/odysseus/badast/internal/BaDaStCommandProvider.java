package de.uniol.inf.is.odysseus.badast.internal;

import java.util.Map;
import java.util.Properties;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.badast.ABaDaStReader;
import de.uniol.inf.is.odysseus.badast.AbstractBaDaStReader;
import de.uniol.inf.is.odysseus.badast.BaDaStException;
import de.uniol.inf.is.odysseus.badast.IBaDaStReader;
import de.uniol.inf.is.odysseus.badast.readers.BaDaStFileReader;

/**
 * Provider of OSGi console commands for the BaDaSt application. <br />
 * <br />
 * That commands are the following:<br />
 * - lsReaderTypes <br />
 * - lsReaders <br />
 * - createReader <br />
 * - startReader <br />
 * - closeReader <br />
 * 
 * @author Michael Brand
 *
 */
public class BaDaStCommandProvider implements CommandProvider {

	/**
	 * The key for configuration, where the reader type is set.
	 */
	public static final String TYPE_CONFIG = AbstractBaDaStReader.TYPE_CONFIG;

	/**
	 * The key for configuration, where the reader name is set.
	 */
	public static final String NAME_CONFIG = AbstractBaDaStReader.NAME_CONFIG;

	/**
	 * All available reader types. Key is the type, value is a not initialized
	 * reader of that type.
	 */
	private static final Map<String, IBaDaStReader<?>> cReaderTypes = Maps
			.newHashMap();

	// Fill the mapping with all reader types, which are not bound by OSGi
	static {
		IBaDaStReader<String> fileReader = new BaDaStFileReader();
		cReaderTypes.put(
				BaDaStFileReader.class.getAnnotation(ABaDaStReader.class)
						.type(), fileReader);
	}

	/**
	 * Binds a BaDaSt reader.
	 * 
	 * @param reader
	 *            The implementation of {@link IBaDaStReader} with an unique
	 *            type to bind.
	 */
	public static void bindReader(IBaDaStReader<?> reader) {
		cReaderTypes.put(reader.getClass().getAnnotation(ABaDaStReader.class)
				.type(), reader);
	}

	/**
	 * Uninds a BaDaSt reader.
	 * 
	 * @param reader
	 *            The implementation of {@link IBaDaStReader} to unbind.
	 */
	public static void unbindReader(IBaDaStReader<?> reader) {
		cReaderTypes.remove(reader.getClass()
				.getAnnotation(ABaDaStReader.class).type());
	}

	/**
	 * All available (already created) readers (initialized readers, not types).
	 * Key is the name, value is an initialized reader with that name.
	 */
	private static final Map<String, IBaDaStReader<?>> cReaders = Maps
			.newHashMap();

	/**
	 * Lists all available reader types.
	 * 
	 * @param ci
	 *            No arguments needed.
	 */
	public void _lsReaderTypes(CommandInterpreter ci) {
		ci.println("Available types of BaDaSt readers:\n");
		for (String type : cReaderTypes.keySet()) {
			ci.print(TYPE_CONFIG + " = " + type + ", parameters = ");
			String[] params = cReaderTypes.get(type).getClass()
					.getAnnotation(ABaDaStReader.class).parameters();
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
	 * Lists all available (already created) readers (initialized readers, not
	 * types).
	 * 
	 * @param ci
	 *            No arguments needed.
	 */
	public void _lsReaders(CommandInterpreter ci) {
		ci.println("Available BaDaSt readers:\n");
		if (cReaders.isEmpty()) {
			ci.println("None");
			return;
		}
		for (String name : cReaders.keySet()) {
			ci.println(NAME_CONFIG
					+ " = "
					+ name
					+ ", "
					+ TYPE_CONFIG
					+ " = "
					+ cReaders.get(name).getConfig()
							.getProperty(AbstractBaDaStReader.TYPE_CONFIG));
		}
	}

	/**
	 * Creates and initializes a new reader.
	 * 
	 * @param ci
	 *            All arguments should be key value pairs (key=value). Any key
	 *            should be {@link #TYPE_CONFIG} and all other needed keys
	 *            depend on the reader type.
	 */
	public void _createReader(CommandInterpreter ci) {
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
			IBaDaStReader<?> reader = cReaderTypes.get(type).newInstance();
			reader.initialize(cfg);
			validateNameShallNotExist(reader.getName());
			cReaders.put(reader.getName(), reader);
			ci.println("Created BaDaSt reader " + reader.getName());
		} catch (BaDaStException e) {
			ci.printStackTrace(e);
		}
	}

	/**
	 * Checks, if a given reader type is known.
	 * 
	 * @param type
	 *            The type to check.
	 * @throws BaDaStException
	 *             if {@link #cReaderTypes} does not contain the type as a key.
	 */
	private void validateTypeShallExist(String type) throws BaDaStException {
		if (type == null) {
			throw new BaDaStException("BaDaSt reader type is not set!");
		} else if (!cReaderTypes.keySet().contains(type)) {
			throw new BaDaStException(type
					+ " is not a known BaDaSt reader type!");
		}
	}

	/**
	 * Checks, if a given reader name is known.
	 * 
	 * @param name
	 *            The name to check.
	 * @throws BaDaStException
	 *             if {@link #cReaders} does not contain the name as a key.
	 */
	private void validateNameShallExist(String name) throws BaDaStException {
		if (!cReaders.keySet().contains(name)) {
			throw new BaDaStException(name + " is not a known BaDaSt reader!");
		}
	}

	/**
	 * Checks, if a given reader type is still free to use.
	 * 
	 * @param name
	 *            The name to check.
	 * @throws BaDaStException
	 *             if {@link #cReaders} does contain the type as a key.
	 */
	private void validateNameShallNotExist(String name) throws BaDaStException {
		if (cReaders.keySet().contains(name)) {
			throw new BaDaStException(name
					+ " is an already created BaDaSt reader!");
		}
	}

	/**
	 * Reads a reader name from a given key value argument.
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
					+ " is not a valid key value argument for reader names! "
					+ NAME_CONFIG + "=value");
		}
		return keyValue[1].trim();
	}

	/**
	 * Starts an existing reader.
	 * 
	 * @param ci
	 *            Should contain one key value argument "key=value", where key
	 *            is {@link #NAME_CONFIG}.
	 */
	public void _startReader(final CommandInterpreter ci) {
		try {
			final String name = readName(ci.nextArgument());
			validateNameShallExist(name);
			new Thread(name) {

				@Override
				public void run() {
					try {
						cReaders.get(name).startReading();
					} catch (BaDaStException e) {
						ci.printStackTrace(e);
					}
				}

			}.start();
			ci.println("Started BaDaSt reader " + name);
		} catch (BaDaStException e) {
			ci.printStackTrace(e);
		}
	}

	/**
	 * Closes and removes an existing reader.
	 * 
	 * @param ci
	 *            Should contain one key value argument "key=value", where key
	 *            is {@link #NAME_CONFIG}.
	 */
	public void _closeReader(CommandInterpreter ci) {
		String name;
		try {
			name = readName(ci.nextArgument());
			validateNameShallExist(name);
		} catch (BaDaStException e) {
			ci.printStackTrace(e);
			return;
		}

		try {
			cReaders.get(name).close();
		} catch (Exception e) {
			ci.print("Could not close reader!");
			ci.printStackTrace(e);
		} finally {
			cReaders.remove(name);
			ci.println("Removed BaDaSt reader " + name);
		}
	}

	// TODO just a test
	public void _testConsumer(CommandInterpreter ci) {
		kafka.javaapi.consumer.SimpleConsumer consumer = new kafka.javaapi.consumer.SimpleConsumer(
				"0", 9092, 100000, 64 * 1024, "TestClient");
		long readOffset = 0;
		kafka.api.FetchRequest req = new kafka.api.FetchRequestBuilder()
				.clientId("TestClient").addFetch("test", 0, 0, 100000).build();
		kafka.javaapi.FetchResponse fetchResponse = consumer.fetch(req);

		long numRead = 0;
		for (kafka.message.MessageAndOffset messageAndOffset : fetchResponse
				.messageSet("test", 0)) {
			long currentOffset = messageAndOffset.offset();
			if (currentOffset < readOffset) {
				System.out.println("Found an old offset: " + currentOffset
						+ " Expecting: " + readOffset);
				continue;
			}
			readOffset = messageAndOffset.nextOffset();
			java.nio.ByteBuffer payload = messageAndOffset.message().payload();

			byte[] bytes = new byte[payload.limit()];
			payload.get(bytes);
			try {
				System.out.println(String.valueOf(messageAndOffset.offset())
						+ ": " + new String(bytes, "UTF-8"));
			} catch (java.io.UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			numRead++;
		}

		if (numRead == 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
			}
		}
	}

	@Override
	public String getHelp() {
		final String TAB = "	";
		StringBuffer out = new StringBuffer(
				"---Backup of Data Streams (BaDaSt) Commands---\n");
		out.append(TAB
				+ "lsReaderTypes - Lists all registered BaDaSt reader types.\n");
		out.append(TAB + "lsReaders - Lists all created BaDaSt readers.\n");
		out.append(TAB
				+ "createReader type=xyz key1=value1 ... keyn=valuen - Creates a new BaDaSt reader with the type of the BaDaSt reader and the needed parameters for that type all as key value pairs. Needed parameters can be viewed with lsReaderTypes\n");
		out.append(TAB
				+ "startReader name=xyz - Starts an existing BaDaSt reader with the name of the BaDaSt reader as a key value pair.\n");
		out.append(TAB
				+ "closeReader name=xyz - Closes and removes an existing BaDaSt reader with the name of the BaDaSt reader as a key value pair.\n");
		return out.toString();
	}

}