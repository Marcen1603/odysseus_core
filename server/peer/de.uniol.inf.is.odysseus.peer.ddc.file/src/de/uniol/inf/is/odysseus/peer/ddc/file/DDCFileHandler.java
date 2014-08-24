package de.uniol.inf.is.odysseus.peer.ddc.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.peer.config.PeerConfiguration;
import de.uniol.inf.is.odysseus.peer.ddc.DDC;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
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
	private static final String[] TS_BRACKETS = { "\\[", "\\]" };

	/**
	 * The pattern for timestamp values in the DDC file.
	 */
	private static final String TS_PATTERN = DDCFileHandler.TS_BRACKETS[0]
			+ "\\d+" + DDCFileHandler.TS_BRACKETS[1];

	/**
	 * The properties loaded from the DDC file.
	 */
	private static Properties cProperties = new Properties();

	/**
	 * Checks, if the DDC file exists.
	 * 
	 * @return True, if the file defined by {@link #getFileName()} exists;
	 *         false, else.
	 */
	public static boolean fileExists() {

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

		DDCFileHandler.loadFromFile();
		DDCFileHandler.writeIntoDDC();

	}

	/**
	 * Adds values from {@link #cProperties} to the DDC.
	 */
	private static void writeIntoDDC() {

		for (String key : DDCFileHandler.cProperties.stringPropertyNames()) {

			String[] partialKeys = key.split(DDCFileHandler.KEY_SEPERATOR);
			IPair<String, Long> valueAndTS = DDCFileHandler
					.determineValueAndTS(DDCFileHandler.cProperties
							.getProperty(key));

			DDC.getInstance().add(partialKeys, valueAndTS.getE1(),
					valueAndTS.getE2());

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

		if (property.endsWith(DDCFileHandler.TS_PATTERN)) {

			value = property.substring(0,
					property.lastIndexOf(DDCFileHandler.TS_BRACKETS[1]));
			String ts_string = property.substring(
					property.lastIndexOf(DDCFileHandler.TS_BRACKETS[0] + 1),
					property.lastIndexOf(DDCFileHandler.TS_BRACKETS[1]));

			try {

				ts = Long.valueOf(ts_string);

			} catch (NumberFormatException e) {

				DDCFileHandler.LOG.error("Can not convert {} to long!",
						ts_string);

			}

		}

		return new Pair<String, Long>(value, Long.valueOf(ts));

	}

	/**
	 * Loads the DDC file into {@link #cProperties}.
	 * 
	 * @throws IOException
	 *             if any error occurs.
	 */
	private static void loadFromFile() throws IOException {

		try (FileInputStream stream = new FileInputStream(
				DDCFileHandler.DDC_FILE)) {

			DDCFileHandler.cProperties.load(stream);
			DDCFileHandler.LOG.debug("Loaded DDC entries from {}",
					DDCFileHandler.DDC_FILE_NAME);

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

		if (!DDCFileHandler.fileExists() && DDCFileHandler.createFile()) {

			DDCFileHandler.LOG.error("Could not create file {}",
					DDCFileHandler.DDC_FILE_NAME);

		}

		boolean changed = false;

		for (String[] key : DDC.getInstance().getKeys()) {

			try {

				String fullKey = DDCFileHandler.determineFullKey(key);
				DDCEntry entryFromDDC = DDC.getInstance().get(key);

				if (DDCFileHandler.cProperties.containsKey(fullKey)) {

					IPair<String, Long> valueAndTS = DDCFileHandler
							.determineValueAndTS(DDCFileHandler.cProperties
									.getProperty(fullKey));
					DDCEntry entryFromProperties = new DDCEntry(key,
							valueAndTS.getE1(), valueAndTS.getE2());
					if (!entryFromProperties.getValue().equals(
							entryFromDDC.getValue())
							&& entryFromProperties
									.compareTimeStamps(entryFromDDC) < 0) {

						// Not the same values and the DDC entry is newer
						DDCFileHandler.cProperties.put(fullKey, DDCFileHandler
								.determineFullValue(valueAndTS.getE1(),
										valueAndTS.getE2().longValue()));
						changed = true;

					}

				} else {

					// Complete new entry
					DDCFileHandler.cProperties.put(
							fullKey,
							DDCFileHandler.determineFullValue(
									entryFromDDC.getValue(),
									entryFromDDC.getTimeStamp()));
					changed = true;

				}

			} catch (MissingDDCEntryException e) {

				// Can not happen, because of String[] key :
				// DDC.getInstance().getKeys()
				DDCFileHandler.LOG.error("Internal error!", e);
				continue;

			}

		}

		if (changed) {

			DDCFileHandler.writeIntoFile();

		}

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
	private static String determineFullKey(String[] key) {

		String fullKey = "";
		for (String partialKey : key) {

			fullKey += partialKey + DDCFileHandler.KEY_SEPERATOR;

		}
		return fullKey.substring(0, fullKey.length() - 1);

	}

	/**
	 * Puts a DDC value and its timestamp to a single value together. Those full
	 * values are for writing into the DDC file.
	 * 
	 * @param value
	 *            The value from a DDC entry.
	 * @param ts
	 *            The timestamp from a DDC entry.
	 * @return A String containing <code>value</code> and <code>ts</code>
	 *         separated by a blank and <code>ts</code> within brackets (
	 *         {@link #TS_BRACKETS}).
	 */
	private static Object determineFullValue(String value, long ts) {

		String fullValue = value;
		fullValue += DDCFileHandler.TS_BRACKETS[0];
		fullValue += String.valueOf(ts);
		fullValue += DDCFileHandler.TS_BRACKETS[1];
		return fullValue;

	}

	/**
	 * Saves the {@link #cProperties} into the DDC file.
	 * 
	 * @throws IOException
	 *             if any error occurs.
	 */
	private static void writeIntoFile() throws IOException {

		try (FileOutputStream out = new FileOutputStream(
				DDCFileHandler.DDC_FILE)) {

			DDCFileHandler.cProperties.store(out,
					"Changed due to new or changed DDC entries");

		}

	}

}