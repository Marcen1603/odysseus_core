package de.uniol.inf.is.odysseus.peer.ddc.console;

import java.util.Set;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DistributedDataContainerAdvertisementGenerator;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DistributedDataContainerAdvertisementSender;

/**
 * The DDC console provides console commands for each method of
 * {@link IDistributedDataContainer}.
 * 
 * @author Michael Brand
 * 
 */
public class DDCConsole implements CommandProvider {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DDCConsole.class);

	/**
	 * The separator for multidimensional DDC entry keys.
	 */
	private static final String KEY_SEPERATOR = ",";

	/**
	 * A line break.
	 */
	private static final String LINEBREAK = "\n";

	/**
	 * The message for an empty DDC.
	 */
	private static final String EMPTY_DDC = "The DDC is empty";

	/**
	 * The different commands and hints to add an entry to the DDC.
	 */
	private static final String[] USAGE_ADD = {
			"DDCadd key value ts - Adds an entry with a 1-dimensional key and a given timestamp to the DDC",
			"DDCadd key1,key2,... value ts - Adds an entry with a multidimensional key and a given timestamp to the DDC",
			"DDCadd key value	- Adds an entry with a 1-dimensional key and the current system time to the DDC",
			"DDCadd key1,key2 value - Adds an entry with a 1-dimensional key and the current system time to the DDC" };

	/**
	 * The different commands and hints to get an entry from the DDC.
	 */
	private static final String[] USAGE_GET_ENTRY = {
			"DDCgetEntry key - Gets an entry with a 1-dimensional key from the DDC",
			"DDCgetEntry key1,key2,... - Gets an entry with a 1-dimensional key from the DDC" };

	/**
	 * The different commands and hints to get the value of an entry from the
	 * DDC.
	 */
	private static final String[] USAGE_GET_VALUE = {
			"DDCgetValue key - Gets the value of an entry with a 1-dimensional key from the DDC",
			"DDCgetValue key1,key2,... - Gets the value of an entry with a 1-dimensional key from the DDC" };

	/**
	 * The different commands and hints to get the timestamp of an entry from
	 * the DDC.
	 */
	private static final String[] USAGE_GET_TIMESTAMP = {
			"DDCgetTimestamp key - Gets the timestamp of an entry with a 1-dimensional key from the DDC",
			"DDCgetTimestamp key1,key2,... - Gets the timestamp of an entry with a 1-dimensional key from the DDC" };

	/**
	 * The command and hint to get all keys from the DDC.
	 */
	private static final String USAGE_GET_KEYS = "DDCgetKeys - Returns the keys of all entries from the DDC";

	/**
	 * The command and hint to get all values from the DDC.
	 */
	private static final String USAGE_GET_VALUES = "DDCgetValues - Returns the values of all entries from the DDC";

	/**
	 * The different commands and hints to check, if the DDC contains a given
	 * key.
	 */
	private static final String[] USAGE_CONTAINS_KEY = {
			"DDCcontainsKey key - Returns true, if there is an entry for a 1-dimensional key in the DDC",
			"DDCcontainsKey key1,key2,... - Returns true, if there is an entry for a multidimensional key in the DDC" };

	/**
	 * The different commands and hints to remove an entry from the DDC.
	 */
	private static final String[] USAGE_REMOVE = {
			"DDCremove key - Removes the entry for a 1-dimensional key from the DDC",
			"DDCremove key1,key2,... - Removes the entry for a multidimensional key from the DDC" };

	/**
	 * The command and hint to get all values from the DDC.
	 */
	private static final String USAGE_GET_ALL_ENTRIES = "DDCgetAllEntries - Returns the complete entries from the DDC";

	/**
	 * The different commands and hints to distribute values from the DDC.
	 */
	private static final String[] USAGE_DISTRIBUTE = {
			"DDCdistribute - Distributes all DDC entries to other peers",
			"DDCdistributeChanges - Distributes DDC changes to other peers" };

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
		DDCConsole.ddc = ddc;
		DDCConsole.LOG.debug("Bound {} as a DDC", ddc.getClass()
				.getSimpleName());

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
		if (DDCConsole.ddc == ddc) {

			DDCConsole.ddc = null;
			DDCConsole.LOG.debug("Unbound {} as a DDC", ddc.getClass()
					.getSimpleName());

		}

	}

	@Override
	public String getHelp() {

		final String TABS = "	";
		StringBuilder output = new StringBuilder();
		output.append("---DDC commands---" + DDCConsole.LINEBREAK);

		// the add commands
		for (String addUsage : DDCConsole.USAGE_ADD) {

			output.append(TABS + addUsage + DDCConsole.LINEBREAK);

		}

		// the get entry commands
		for (String getEntryUsage : DDCConsole.USAGE_GET_ENTRY) {

			output.append(TABS + getEntryUsage + DDCConsole.LINEBREAK);

		}

		// the get value commands
		for (String getValueUsage : DDCConsole.USAGE_GET_VALUE) {

			output.append(TABS + getValueUsage + DDCConsole.LINEBREAK);

		}

		// the get timestamp commands
		for (String getTimestampUsage : DDCConsole.USAGE_GET_TIMESTAMP) {

			output.append(TABS + getTimestampUsage + DDCConsole.LINEBREAK);

		}

		// the get keys command
		output.append(TABS + DDCConsole.USAGE_GET_KEYS + DDCConsole.LINEBREAK);

		// the get values command
		output.append(TABS + DDCConsole.USAGE_GET_VALUES + DDCConsole.LINEBREAK);

		// the contains key commands
		for (String containsKeyUsage : DDCConsole.USAGE_CONTAINS_KEY) {

			output.append(TABS + containsKeyUsage + DDCConsole.LINEBREAK);

		}

		// the remove commands
		for (String removeUsage : DDCConsole.USAGE_REMOVE) {

			output.append(TABS + removeUsage + DDCConsole.LINEBREAK);

		}

		// the get all entries command
		output.append(TABS + DDCConsole.USAGE_GET_ALL_ENTRIES
				+ DDCConsole.LINEBREAK);

		// the distribution commands
		for (String distributeUsage : DDCConsole.USAGE_DISTRIBUTE) {

			output.append(TABS + distributeUsage + DDCConsole.LINEBREAK);

		}

		return output.toString();

	}

	/**
	 * Interprets a command to add an entry to the DDC. <br />
	 * The following commands are allowed: <br />
	 * DDCadd key value TS <br />
	 * DDCadd key1,key2,... value TS <br />
	 * DDCadd key value <br />
	 * DDCadd key1,key2,... value
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCadd(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// Get the key
		Optional<String> optKey = Optional.fromNullable(ci.nextArgument());
		if (!optKey.isPresent()) {

			System.err.println(DDCConsole.USAGE_ADD);
			return;

		}
		DDCKey key = new DDCKey(optKey.get().split(DDCConsole.KEY_SEPERATOR));

		// Get the value
		Optional<String> optValue = Optional.fromNullable(ci.nextArgument());
		if (!optValue.isPresent()) {

			System.err.println(DDCConsole.USAGE_ADD);
			return;

		}
		String value = optValue.get();

		// Get the optional timestamp
		Long ts;
		Optional<String> optTS_string = Optional
				.fromNullable(ci.nextArgument());
		if (optTS_string.isPresent()) {

			try {

				ts = Long.valueOf(optTS_string.get());

			} catch (NumberFormatException e) {

				System.err.println("The given timestamp " + optTS_string.get()
						+ " is no valid timestamp");
				return;

			}

		} else {

			ts = System.currentTimeMillis();

		}

		// No more arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_ADD);
			return;

		}

		// Add to DDC
		if(DDCConsole.ddc.add(new DDCEntry(key, value, ts))) {
			
			System.out.println("Added entry to DDC");

		} else {

			System.out.println("No entry found");

		}

	}

	/**
	 * Interprets a command to get an entry from the DDC. <br />
	 * The following commands are allowed: <br />
	 * DDCgetEntry key <br />
	 * DDCgetEntry key1,key2,...
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCgetEntry(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// Get the key
		Optional<String> optKey = Optional.fromNullable(ci.nextArgument());
		if (!optKey.isPresent()) {

			System.err.println(DDCConsole.USAGE_GET_ENTRY);
			return;

		}
		DDCKey key = new DDCKey(optKey.get().split(DDCConsole.KEY_SEPERATOR));

		// No more arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_GET_ENTRY);
			return;

		}

		// Get from DDC
		try {

			System.out.println(DDCConsole.ddc.get(key));

		} catch (MissingDDCEntryException e) {

			System.out.println(e.getMessage());

		}

	}

	/**
	 * Interprets a command to get the value of an entry from the DDC. <br />
	 * The following commands are allowed: <br />
	 * DDCgetValue key <br />
	 * DDCgetValue key1,key2,...
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCgetValue(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// Get the key
		Optional<String> optKey = Optional.fromNullable(ci.nextArgument());
		if (!optKey.isPresent()) {

			System.err.println(DDCConsole.USAGE_GET_VALUE);
			return;

		}
		DDCKey key = new DDCKey(optKey.get().split(DDCConsole.KEY_SEPERATOR));

		// No more arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_GET_VALUE);
			return;

		}

		// Get from DDC
		try {

			System.out.println(DDCConsole.ddc.getValue(key));

		} catch (MissingDDCEntryException e) {

			System.out.println(e.getMessage());

		}

	}

	/**
	 * Interprets a command to get the timestamp of an entry from the DDC. <br />
	 * The following commands are allowed: <br />
	 * DDCgetTimestamp key <br />
	 * DDCgetTimestamp key1,key2,...
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCgetTimestamp(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// Get the key
		Optional<String> optKey = Optional.fromNullable(ci.nextArgument());
		if (!optKey.isPresent()) {

			System.err.println(DDCConsole.USAGE_GET_TIMESTAMP);
			return;

		}
		DDCKey key = new DDCKey(optKey.get().split(DDCConsole.KEY_SEPERATOR));

		// No more arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_GET_TIMESTAMP);
			return;

		}

		// Get from DDC
		try {

			System.out.println(DDCConsole.ddc.getTimeStamp(key));

		} catch (MissingDDCEntryException e) {

			System.out.println(e.getMessage());

		}

	}

	/**
	 * Interprets a command to get all keys from the DDC. <br />
	 * This command allows no arguments.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCgetKeys(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// No arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_GET_KEYS);
			return;

		}

		// Get the keys from DDC
		Set<DDCKey> keys = DDCConsole.ddc.getKeys();
		if (keys.isEmpty()) {

			System.out.println(DDCConsole.EMPTY_DDC);
			return;

		}

		for (DDCKey key : keys) {

			System.out.println(key);

		}

	}

	/**
	 * Interprets a command to get all values from the DDC. <br />
	 * This command allows no arguments.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCgetValues(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// No arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_GET_VALUES);
			return;

		}

		// Get the values from DDC
		Set<String> values = DDCConsole.ddc.getValues();
		if (values.isEmpty()) {

			System.out.println(DDCConsole.EMPTY_DDC);
			return;

		}

		for (String value : values) {

			System.out.println(value);

		}

	}

	/**
	 * Interprets a command to check if the DDC contains a given key. <br />
	 * The following commands are allowed: <br />
	 * DDCcontainsKey key <br />
	 * DDCcontainsKey key1,key2,...
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCcontainsKey(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// Get the key
		Optional<String> optKey = Optional.fromNullable(ci.nextArgument());
		if (!optKey.isPresent()) {

			System.err.println(DDCConsole.USAGE_CONTAINS_KEY);
			return;

		}
		DDCKey key = new DDCKey(optKey.get().split(DDCConsole.KEY_SEPERATOR));

		// No more arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_CONTAINS_KEY);
			return;

		}

		System.out.println(String.valueOf(DDCConsole.ddc.containsKey(key)));

	}

	/**
	 * Interprets a command to remove an entry from the DDC. <br />
	 * The following commands are allowed: <br />
	 * DDCremove key <br />
	 * DDCremove key1,key2,...
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCremove(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// Get the key
		Optional<String> optKey = Optional.fromNullable(ci.nextArgument());
		if (!optKey.isPresent()) {

			System.err.println(DDCConsole.USAGE_REMOVE);
			return;

		}
		DDCKey key = new DDCKey(optKey.get().split(DDCConsole.KEY_SEPERATOR));

		// No more arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_REMOVE);
			return;

		}

		if (DDCConsole.ddc.remove(key)) {

			System.out.println("Removed entry from DDC");

		} else {

			System.out.println("No entry found");

		}

	}

	/**
	 * Interprets a command to get all complete entries from the DDC. <br />
	 * This command allows no arguments.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCgetAllEntries(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// No arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_GET_ALL_ENTRIES);
			return;

		}

		System.out.println(DDCConsole.ddc.toString());

	}

	/**
	 * Interprets a command to distribute values from distributed data container
	 * to other peers. <br />
	 * This command allows no arguments.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCdistribute(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// No arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_DISTRIBUTE[0]);
			return;

		}

		DistributedDataContainerAdvertisement ddcAdvertisement = DistributedDataContainerAdvertisementGenerator
				.getInstance().generate();
		DistributedDataContainerAdvertisementSender.getInstance()
				.publishDDCAdvertisement(ddcAdvertisement);
		System.out.println("Initial distribution started");

	}

	/**
	 * Interprets a command to distribute value changes from distributed data
	 * container to other peers. <br />
	 * This command allows no arguments.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCdistributeChanges(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// No arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_DISTRIBUTE[0]);
			return;

		}

		DistributedDataContainerAdvertisement ddcAdvertisement = DistributedDataContainerAdvertisementGenerator
				.getInstance().generateChanges();
		DistributedDataContainerAdvertisementSender.getInstance()
				.publishDDCAdvertisement(ddcAdvertisement);
		System.out.println("Change distribution started");

	}
}