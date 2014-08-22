package de.uniol.inf.is.odysseus.peer.ddc.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;

/**
 * The standard implementation of {@link IDistributedDataContainer} uses a
 * hashmap containing {@link DDCEntry}s for the internal data structure.
 * 
 * @author Michael Brand
 *
 */
public class StandardDDC implements IDistributedDataContainer {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(StandardDDC.class);

	/**
	 * The internal data structure: DDC entries mapped to their keys.
	 */
	private Map<String[], DDCEntry> mEntries = Collections
			.synchronizedMap(new HashMap<String[], DDCEntry>());

	/**
	 * Empty default constructor. <br />
	 * To be used by OSGI-DS.
	 */
	public StandardDDC() {

		// Empty default constructor

	}

	@Override
	public boolean add(String[] key, String value, long ts) {

		DDCEntry entry = new DDCEntry(key, value, ts);

		if (this.containsKey(entry.getKey())
				&& entry.compareTimeStamps(this.mEntries.get(entry.getKey())) <= 0) {

			// there is already an entry with that key and the already stored
			// entry is newer
			StandardDDC.LOG.debug("Discarded {} due to an older timestamp!",
					entry);
			return false;

		}

		// complete new or newer entry (ts)
		synchronized (this.mEntries) {

			this.mEntries.put(entry.getKey(), entry);

		}

		StandardDDC.LOG.debug("Added {} to the DDC.", entry);
		return true;

	}

	@Override
	public boolean add(String key, String value, long ts) {

		return this.add(new String[] { key }, value, ts);

	}

	@Override
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

	@Override
	public String get(String key) throws MissingDDCEntryException {

		return this.get(new String[] { key });

	}

	@Override
	public Set<String[]> getKeys() {

		return this.mEntries.keySet();

	}

	@Override
	public Set<String> getValues() {

		Set<String> values = Sets.newHashSet();

		for (String[] key : this.mEntries.keySet()) {

			values.add(this.mEntries.get(key).getValue());

		}

		return values;

	}

	@Override
	public boolean containsKey(String[] key) {

		return this.mEntries.containsKey(key);

	}

	@Override
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

	@Override
	public boolean remove(String[] key) {

		Optional<DDCEntry> optEntry = Optional.absent();

		synchronized (this.mEntries) {

			optEntry = Optional.fromNullable(this.mEntries.remove(key));

		}

		if (optEntry.isPresent()) {

			StandardDDC.LOG.debug("Removed {} from DDC.", optEntry.get());

		} else {

			StandardDDC.LOG.debug("No DDC entry found for {}", key);

		}

		return optEntry.isPresent();

	}

	@Override
	public boolean remove(String key) {

		return this.remove(new String[] { key });

	}

}