package de.uniol.inf.is.odysseus.peer.ddc.console;

import java.util.Set;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.ddc.DDC;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DDCAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DDCAdvertisementGenerator;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DDCAdvertisementSender;

/**
 * The DDC console provides console commands for each method of
 * {@link IDistributedDataContainer}.
 * 
 * @author Michael Brand
 * 
 */
public class DDCConsole implements CommandProvider {

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
			"DDCadd key1,key2, ... value ts - Adds an entry with a multidimensional key and a given timestamp to the DDC",
			"DDCadd key value	- Adds an entry with a 1-dimensional key and the current system time to the DDC",
			"DDCadd key1,key2 value - Adds an entry with a 1-dimensional key and the current system time to the DDC" };

	/**
	 * The different commands and hints to get an entry from the DDC.
	 */
	private static final String[] USAGE_GET = {
			"DDCget key - Returns the entry for a 1-dimensional key from the DDC",
			"DDCget key1,key2,... - Returns the entry for a multidimensional key from the DDC" };

	/**
	 * The different commands and hints to remove an entry from the DDC.
	 */
	private static final String[] USAGE_REMOVE = {
			"DDCremove key - Removes the entry for a 1-dimensional key from the DDC",
			"DDCremove key1,key2,... - Removes the entry for a multidimensional key from the DDC" };

	/**
	 * The different commands and hints to check, if the DDC contains a given
	 * key.
	 */
	private static final String[] USAGE_CONTAINS_KEY = {
			"DDCcontainsKey key - Returns true, if there is an entry for a 1-dimensional key in the DDC",
			"DDCcontainsKey key1,key2,... - Returns true, if there is an entry for a multidimensional key in the DDC" };

	/**
	 * The different commands and hints to get all keys from the DDC.
	 */
	private static final String USAGE_GET_KEYS = "DDCgetKeys - Returns the keys of all entries from the DDC";

	/**
	 * The different commands and hints to get all values from the DDC.
	 */
	private static final String USAGE_GET_VALUES = "DDCgetValues - Returns the values of all entries from the DDC";

	private static final String[] USAGE_DISTRIBUTE = {
			"DDCdistribute - Distributes all DDC entries to other peers",
			"DDCdistributeChanges - Distributes DDC changes to other peers" };

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
		String[] key = optKey.get().split(DDCConsole.KEY_SEPERATOR);

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
		if (optValue.isPresent()) {

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
		DDC.getInstance().add(key, value, ts);

	}

	/**
	 * Interprets a command to get an entry from the DDC. <br />
	 * The following commands are allowed: <br />
	 * DDCget key <br />
	 * DDCget key1,key2,...
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} containing the arguments.
	 */
	public void _DDCget(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci,
				"The command interpreter must be not null!");

		// Get the key
		Optional<String> optKey = Optional.fromNullable(ci.nextArgument());
		if (!optKey.isPresent()) {

			System.err.println(DDCConsole.USAGE_GET);
			return;

		}
		String[] key = optKey.get().split(DDCConsole.KEY_SEPERATOR);

		// No more arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_GET);
			return;

		}

		try {

			System.out.println(DDC.getInstance().get(key));

		} catch (MissingDDCEntryException e) {

			System.err.println(e.getMessage());

		}

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
		String[] key = optKey.get().split(DDCConsole.KEY_SEPERATOR);

		// No more arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_REMOVE);
			return;

		}

		if (DDC.getInstance().remove(key)) {

			System.out.println("Removed entry from DDC");

		} else {

			System.out.println("No entry found");

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

		Set<String[]> keys = DDC.getInstance().getKeys();
		if (keys.isEmpty()) {

			System.out.println(DDCConsole.EMPTY_DDC);
			return;

		}

		for (String[] key : keys) {

			String output = "";
			for (String partialKey : key) {

				output += partialKey + DDCConsole.KEY_SEPERATOR;

			}
			System.out.println(output.substring(0, output.length() - 1));

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

		Set<String> values = DDC.getInstance().getValues();
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

			System.err.println(DDCConsole.USAGE_GET);
			return;

		}
		String[] key = optKey.get().split(DDCConsole.KEY_SEPERATOR);

		// No more arguments allowed
		if (ci.nextArgument() != null) {

			System.err.println(DDCConsole.USAGE_GET);
			return;

		}

		System.out.println(String.valueOf(DDC.getInstance().containsKey(key)));

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

		// the get commands
		for (String getUsage : DDCConsole.USAGE_GET) {

			output.append(TABS + getUsage + DDCConsole.LINEBREAK);

		}

		// the remove commands
		for (String removeUsage : DDCConsole.USAGE_REMOVE) {

			output.append(TABS + removeUsage + DDCConsole.LINEBREAK);

		}

		// the contains key commands
		for (String containsKeyUsage : DDCConsole.USAGE_CONTAINS_KEY) {

			output.append(TABS + containsKeyUsage + DDCConsole.LINEBREAK);

		}

		// the getKeys and getValues commands
		output.append(TABS + DDCConsole.USAGE_GET_KEYS + DDCConsole.LINEBREAK);
		output.append(TABS + DDCConsole.USAGE_GET_VALUES + DDCConsole.LINEBREAK);

		// the distribution commands
		for (String distributeUsage : DDCConsole.USAGE_DISTRIBUTE) {
			output.append(TABS + distributeUsage + DDCConsole.LINEBREAK);
		}

		return output.toString();

	}

	/**
	 * Interprets a command to distribute values from distributed data container
	 * to other peers
	 * 
	 * @param ci
	 */
	public void _DDCdistribute(CommandInterpreter ci) {
		DDC ddcInstance = DDC.getInstance();

		if (ddcInstance != null) {
			DDCAdvertisement ddcAdvertisement = DDCAdvertisementGenerator
					.getInstance().generate(ddcInstance);
			if (ddcAdvertisement != null) {
				DDCAdvertisementSender.getInstance().publishDDCAdvertisement(
						ddcAdvertisement);
				System.out.println("Initial distribution started");
			}
		}
	}

	/**
	 * Interprets a command to distribute value changes from distributed data
	 * container to other peers
	 * 
	 * @param ci
	 */
	public void _DDCdistributeChanges(CommandInterpreter ci) {
		DDCAdvertisement ddcAdvertisement = DDCAdvertisementGenerator
				.getInstance().generateChanges();
		if (ddcAdvertisement != null) {
			DDCAdvertisementSender.getInstance().publishDDCAdvertisement(
					ddcAdvertisement);
			System.out.println("Change distribution started");
		}

	}
}