package de.uniol.inf.is.odysseus.peer.ddc.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.peer.config.PeerConfiguration;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;

/**
 * A DDC file handler can be used to load DDC entries from a specific file and
 * to store them in the DDC.
 * 
 * @author Michael Brand
 *
 */
public class DDCFileHandler {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(DDCFileHandler.class);

	/**
	 * The name of the DDC file.
	 */
	public static final String DDC_FILE_NAME = PeerConfiguration.ODYSSEUS_HOME_DIR
			+ "datacontainer.ddc";

	/**
	 * The DDC file.
	 */
	private static final File DDC_FILE = new File(DDCFileHandler.DDC_FILE_NAME);

	/**
	 * The separator for multidimensional DDC entry keys.
	 */
	private static final String KEY_SEPERATOR = ",";

	/**
	 * The brackets including timestamps in the DDC file.
	 */
	private static final String[] TS_BRACKETS = { "[", "]" };

	/**
	 * The prefix for timestamps in the DDC file.
	 */
	private static final String TS_PREFIX = "ts: ";

	/**
	 * The DDC.
	 */
	private static IDistributedDataContainer ddc;

	/**
	 * Binds a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to bind. <br />
	 *            Must be not null.
	 */
	public static void bindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		DDCFileHandler.ddc = ddc;
		DDCFileHandler.LOG.debug("Bound {} as a DDC", ddc.getClass()
				.getSimpleName());

		// TODO workaround to ensure that the DDC distributor is already
		// listening
		if (!DDCFileHandler.cListeners.isEmpty()) {

			try {

				DDCFileHandler.load();

			} catch (IOException e) {

				DDCFileHandler.LOG.error("Could not load DDC file!", e);

			}

		}

	}

	/**
	 * Removes the binding for a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		if (DDCFileHandler.ddc == ddc) {

			try {

				DDCFileHandler.save();

			} catch (IOException e) {

				DDCFileHandler.LOG.error("Could not save DDC file!", e);

			}

			DDCFileHandler.ddc = null;
			DDCFileHandler.LOG.debug("Unbound {} as a DDC", ddc.getClass()
					.getSimpleName());

		}

	}

	/**
	 * List of all bound DDC listeners.
	 */
	private static Collection<IDDCFileHandlerListener> cListeners = Lists
			.newArrayList();

	/**
	 * Binds a listener. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param listener
	 *            The listener to bind. <br />
	 *            Must be not null.
	 */
	public static void bindListener(IDDCFileHandlerListener listener) {

		Preconditions.checkNotNull(listener,
				"The DDC listener to bind must be not null!");
		DDCFileHandler.LOG.debug("Bound {} as a listener", listener.getClass()
				.getSimpleName());
		DDCFileHandler.cListeners.add(listener);

		// TODO workaround to ensure that the DDC distributor is already
		// listening
		if (DDCFileHandler.ddc != null) {

			try {

				DDCFileHandler.load();

			} catch (IOException e) {

				DDCFileHandler.LOG.error("Could not load DDC file!", e);

			}

		}

	}

	/**
	 * Removes the binding of a listener. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param listener
	 *            The listener to remove. <br />
	 *            Must be not null.
	 */
	public static void unbindListener(IDDCFileHandlerListener listener) {

		Preconditions.checkNotNull(listener,
				"The DDC listener to unbind must be not null!");
		DDCFileHandler.cListeners.remove(listener);
		DDCFileHandler.LOG.debug("Unbound {} as a listener", listener
				.getClass().getSimpleName());

	}

	/**
	 * Notifies the listeners.
	 */
	private static void setChanged() {

		for (IDDCFileHandlerListener listener : DDCFileHandler.cListeners) {

			listener.ddcFileLoaded();

		}

	}

	/**
	 * Checks, if the DDC file exists.
	 * 
	 * @return True, if the file defined by {@link #getFileName()} exists;
	 *         false, else.
	 */
	private static boolean fileExists() {

		return DDC_FILE.exists();

	}

	/**
	 * Loads the DDC entries from the DDC file and stores them in the DDC. <br />
	 * Note that an entry will not be stored in the DDC, if the timestamp of the
	 * last file change is older than an already existing entry in the DDC
	 * having the same key.
	 * 
	 * @throws IOException
	 *             if the file defined by {@link #getFileName()} does not exist
	 *             or <br />
	 *             if any error occurs while reading the file.
	 */
	public static void load() throws IOException {

		if (!DDCFileHandler.fileExists() && !DDCFileHandler.createFile()) {

			DDCFileHandler.LOG.error("Could not create file {}",
					DDCFileHandler.DDC_FILE_NAME);
			return;

		}

		Properties properties = DDCFileHandler.loadFromFile();
		DDCFileHandler.writeIntoDDC(properties);
		DDCFileHandler.setChanged();

	}

	/**
	 * Adds entries to the DDC.
	 * 
	 * @param properties
	 *            The properties containing the entries. <br />
	 *            Must be not null.
	 */
	private static void writeIntoDDC(Properties properties) {

		Preconditions.checkNotNull(properties,
				"The properties must be not null!");
		Preconditions.checkNotNull(DDCFileHandler.ddc, "A DDC must be bound!");

		for (String key : properties.stringPropertyNames()) {

			String[] partialKeys = key.split(DDCFileHandler.KEY_SEPERATOR);
			IPair<String, Long> valueAndTS = DDCFileHandler
					.determineValueAndTS(properties.getProperty(key));

			DDCFileHandler.ddc.add(new DDCEntry(new DDCKey(partialKeys),
					valueAndTS.getE1(), valueAndTS.getE2()));

		}

	}

	/**
	 * Determine the value and the timestamp from a DDC property.
	 * 
	 * @param property
	 *            The DDC property must look as follows: <br />
	 *            value or <br />
	 *            value [timestamp]
	 * @return A pair containing the value as String and the timestamp as Long.
	 *         The timestamp is either given within <code>property</code> or, if
	 *         not, the date of the last file change.
	 */
	private static IPair<String, Long> determineValueAndTS(String property) {

		String value = property;
		long ts = DDCFileHandler.DDC_FILE.lastModified();

		if (property.endsWith(DDCFileHandler.TS_BRACKETS[1])) {

			int beginIndex = property.lastIndexOf(DDCFileHandler.TS_PREFIX);

			if (beginIndex >= 0) {

				String ts_string = property.substring(beginIndex
						+ DDCFileHandler.TS_PREFIX.length(),
						property.length() - 1);
				if (ts_string.matches("\\d+")) {

					value = property
							.substring(0, property
									.lastIndexOf(DDCFileHandler.TS_BRACKETS[0]));

					try {

						ts = Long.valueOf(ts_string);

					} catch (NumberFormatException e) {

						DDCFileHandler.LOG.error("Can not convert {} to long!",
								ts_string);

					}

				}

			}

		}

		return new Pair<String, Long>(value, Long.valueOf(ts));

	}

	/**
	 * Loads the DDC file into properties.
	 * 
	 * @return Properties containing all entries.
	 * @throws IOException
	 *             if any error occurs.
	 */
	private static Properties loadFromFile() throws IOException {

		try (FileInputStream stream = new FileInputStream(
				DDCFileHandler.DDC_FILE)) {

			Properties properties = new Properties();
			properties.load(stream);
			DDCFileHandler.LOG.debug("Loaded DDC entries from {}",
					DDCFileHandler.DDC_FILE_NAME);
			return properties;

		}

	}

	/**
	 * Saves the DDC entries in the DDC file. <br />
	 * Note that an entry will be stored with its timestamp, if the timestamp of
	 * the last file change is older than the timestamp of the entry.
	 * 
	 * @throws IOException
	 *             if any error occurs.
	 */
	public static void save() throws IOException {

		Preconditions.checkNotNull(DDCFileHandler.ddc, "A DDC must be bound!");

		if (!DDCFileHandler.fileExists() && DDCFileHandler.createFile()) {

			DDCFileHandler.LOG.error("Could not create file {}",
					DDCFileHandler.DDC_FILE_NAME);

		}

		DDCFileHandler.writeIntoFile();

	}

	/**
	 * Created the DDC file.
	 * 
	 * @return True, if the file creation was successful.
	 * @throws IOException
	 *             may be thrown by {@link File#createNewFile()}.
	 */
	private static boolean createFile() throws IOException {

		File directory = DDCFileHandler.DDC_FILE.getParentFile();

		if (directory == null
				|| (directory != null && !directory.mkdirs() && !DDCFileHandler.DDC_FILE
						.createNewFile())) {

			return false;

		}

		DDCFileHandler.LOG.debug("Created new File {}",
				DDCFileHandler.DDC_FILE_NAME);
		return true;

	}

	/**
	 * Puts an array of partial keys to a single key together. Those full keys
	 * are for writing into the DDC file.
	 * 
	 * @param key
	 *            The key from a DDC entry.
	 * @return A String containing all partial keys divided by
	 *         {@link #KEY_SEPERATOR}.
	 */
	private static String determineFullKey(DDCKey key) {

		String fullKey = "";
		for (String partialKey : key.get()) {

			fullKey += partialKey + DDCFileHandler.KEY_SEPERATOR;

		}
		return fullKey.substring(0, fullKey.length() - 1);

	}

	/**
	 * Puts a DDC value and its timestamp to a single value together. Those full
	 * values are for writing into the DDC file.
	 * 
	 * @param entry
	 *            The DDC entry. <br />
	 *            Must be not null.
	 * @return A String containing <code>value</code> and <code>ts</code>
	 *         separated by a blank and <code>ts</code> within brackets (
	 *         {@link #TS_BRACKETS}).
	 */
	private static String determineFullValue(DDCEntry entry) {

		String fullValue = entry.getValue();
		fullValue += DDCFileHandler.TS_BRACKETS[0];
		fullValue += DDCFileHandler.TS_PREFIX;
		fullValue += String.valueOf(entry.getTimeStamp());
		fullValue += DDCFileHandler.TS_BRACKETS[1];
		return fullValue;

	}

	/**
	 * Writes the DDC into the DDC file.
	 * 
	 * @throws IOException
	 *             if any error occurs.
	 */
	private static void writeIntoFile() throws IOException {

		StringBuffer content = new StringBuffer();

		for (DDCKey key : DDCFileHandler.ddc.getSortedKeys()) {

			try {

				content.append(DDCFileHandler.determineFullKey(key));
				content.append(" = ");
				content.append(DDCFileHandler
						.determineFullValue(DDCFileHandler.ddc.get(key)));
				content.append("\n");

			} catch (MissingDDCEntryException e) {

				// should not happen
				DDCFileHandler.LOG.error(e.getMessage());

			}

		}

		try (FileWriter out = new FileWriter(DDCFileHandler.DDC_FILE)) {

			out.write(content.toString());

		}

	}

}