package de.uniol.inf.is.odysseus.peer.ddc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.peer.ddc.impl.DDCEntry;

/**
 * A distributed data container (DDC) is a container for String key value pairs. <br />
 * The keys can be multidimensional to represent table entries. Additionally
 * each entry contains a timestamp indicating the point in time, when the entry
 * was created or last changed. <br />
 * A DDC should be filled by either reading from a file or by advertisements
 * from other peers. The goal is, that all DDCs within a P2P network have the
 * same state.
 * 
 * @author Michael Brand
 *
 */
public class DDC {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DDC.class);

	/**
	 * The single DDC instance.
	 */
	private static DDC cInstance;

	/**
	 * The single DDC instance.
	 * 
	 * @return A DDC instance.
	 */
	public static DDC getInstance() {

		if (DDC.cInstance == null) {

			DDC.cInstance = new DDC();

		}

		return DDC.cInstance;

	}

	/**
	 * The internal data structure: DDC entries mapped to their keys.
	 */
	private Map<String[], DDCEntry> mEntries = Collections
			.synchronizedMap(new HashMap<String[], DDCEntry>());

	/**
	 * Empty default constructor.
	 */
	public DDC() {

		// Empty default constructor

	}

	/**
	 * Adds an entry to the DDC.
	 * 
	 * @param key
	 *            The multidimensional key. <br />
	 *            The key must be not null and there must be at least one
	 *            dimension of a key.
	 * @param value
	 *            The value. <br />
	 *            The value must be not null.
	 * @param ts
	 *            The timestamp indicating the point in time of creation or last
	 *            change. <br />
	 *            The timestamp must be greater zero.
	 * @return True, if the entry could be added. False, if the key already
	 *         exists and the value has the same timestamp or an older one than
	 *         the stored one.
	 */
	public boolean add(String[] key, String value, long ts) {

		DDCEntry entry = new DDCEntry(key, value, ts);

		if (this.containsKey(entry.getKey())
				&& entry.compareTimeStamps(this.mEntries.get(entry.getKey())) <= 0) {

			// there is already an entry with that key and the already stored
			// entry is newer
			DDC.LOG.debug("Discarded {} due to an older timestamp!", entry);
			return false;

		}

		// complete new or newer entry (ts)
		synchronized (this.mEntries) {

			this.mEntries.put(entry.getKey(), entry);

		}

		DDC.LOG.debug("Added {} to the DDC.", entry);
		return true;

	}

	/**
	 * Adds an entry to the DDC.
	 * 
	 * @param key
	 *            The 1-dimensional key. <br />
	 *            The key must be not null.
	 * @param value
	 *            The value. <br />
	 *            The value must be not null.
	 * @param ts
	 *            The timestamp indicating the point in time of creation or last
	 *            change. <br />
	 *            The timestamp must be greater zero.
	 * @return True, if the entry could be added. False, if the key already
	 *         exists and the value has the same timestamp or an older one than
	 *         the stored one.
	 */
	public boolean add(String key, String value, long ts) {

		return this.add(new String[] { key }, value, ts);

	}

	/**
	 * Gets an entry of the DDC.
	 * 
	 * @param key
	 *            The multidimensional key for the searched entry.
	 * @return The value for <code>key</code>.
	 * @throws MissingDDCEntryException
	 *             if there is no entry with <code>key</code> as key.
	 */
	public String get(String[] key) throws MissingDDCEntryException {

		Preconditions.checkNotNull(key,
				"The key for a DDC entry must be not null!");
		Preconditions
				.checkArgument(key.length > 0,
						"The (multidimensional) key for a DDC entry must have at least one dimension");

		if (!this.containsKey(key)) {

			throw new MissingDDCEntryException(key);

		}

		return this.mEntries.get(key).getValue();

	}

	/**
	 * Gets an entry of the DDC.
	 * 
	 * @param key
	 *            The 1-dimensional key for the searched entry.
	 * @return The value for <code>key</code>.
	 * @throws MissingDDCEntryException
	 *             if there is no entry with <code>key</code> as key.
	 */
	public String get(String key) throws MissingDDCEntryException {

		return this.get(new String[] { key });

	}

	/**
	 * Gets the keys of all entries of the DDC.
	 * 
	 * @return A set containing all multidimensional keys.
	 */
	public Set<String[]> getKeys() {

		return this.mEntries.keySet();

	}

	/**
	 * Gets the values of all entries of the DDC.
	 * 
	 * @return A set containing all values.
	 */
	public Set<String> getValues() {

		Set<String> values = Sets.newHashSet();

		for (String[] key : this.mEntries.keySet()) {

			values.add(this.mEntries.get(key).getValue());

		}

		return values;

	}

	/**
	 * Checks, if the DDC contains a given key.
	 * 
	 * @param key
	 *            The given multidimensional key.
	 * @return True, if the DDC contains an entry <code>key</code> as key.
	 */
	public boolean containsKey(String[] key) {

		return this.mEntries.containsKey(key);

	}

	/**
	 * Checks, if the DDC contains a given key.
	 * 
	 * @param key
	 *            The given 1-dimensional key.
	 * @return True, if the DDC contains an entry <code>key</code> as key.
	 */
	public boolean containsKey(String key) {

		return this.mEntries.containsKey(new String[] { key });

	}

	@Override
	public String toString() {

		String ddcString = "Current DDC:\n";

		for (String[] key : this.getKeys()) {

			ddcString += this.mEntries.get(key) + "\n";

		}

		return ddcString;

	}

	/**
	 * Removes an entry.
	 * 
	 * @param key
	 *            The given multidimensional key.
	 * @return True, if there is an DDC entry for the given key. If this is the
	 *         case, the entry has been removed successfully. False, if there is
	 *         no DDC entry for the given key.
	 */
	public boolean remove(String[] key) {

		Optional<DDCEntry> optEntry = Optional.absent();

		synchronized (this.mEntries) {

			optEntry = Optional.fromNullable(this.mEntries.remove(key));

		}

		if (optEntry.isPresent()) {

			DDC.LOG.debug("Removed {} from DDC.", optEntry.get());

		} else {

			DDC.LOG.debug("No DDC entry found for {}", key);

		}

		return optEntry.isPresent();

	}

	/**
	 * Removes an entry.
	 * 
	 * @param key
	 *            The given 1-dimensional key.
	 * @return True, if there is an DDC entry for the given key. If this is the
	 *         case, the entry has been removed successfully. False, if there is
	 *         no DDC entry for the given key.
	 */
	public boolean remove(String key) {

		return this.remove(new String[] { key });

	}

}