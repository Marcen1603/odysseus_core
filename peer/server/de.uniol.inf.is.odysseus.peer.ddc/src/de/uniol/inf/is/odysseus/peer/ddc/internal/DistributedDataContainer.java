package de.uniol.inf.is.odysseus.peer.ddc.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDDCListener;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;

/**
 * A distributed data container (DDC) is a container for String key value pairs. <br />
 * The keys can be multidimensional to represent table entries. Additionally
 * each entry contains a timestamp indicating the point in time, when the entry
 * was created or last changed. <br />
 * A DDC should be filled by either reading from a file or by advertisements
 * from other peers. The goal is, that all DDCs within a P2P network have the
 * same state. <br />
 * 
 * The listeners for the DDC will be bound by this class (OSGi-DS).
 * 
 * @author Michael Brand
 *
 */
public class DistributedDataContainer implements IDistributedDataContainer {

	/**
	 * The enumeration of possible change states of the DDC.
	 * 
	 * @author Michael Brand
	 *
	 */
	private enum ChangeState {

		added, removed;

	}

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(DistributedDataContainer.class);

	/**
	 * List of all bound DDC listeners.
	 */
	private static Collection<IDDCListener> cListeners = Lists.newArrayList();

	/**
	 * Binds a DDC listener. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param listener
	 *            The DDC listener to bind. <br />
	 *            Must be not null.
	 */
	public static void bindDDCListener(IDDCListener listener) {

		Preconditions.checkNotNull(listener,
				"The DDC listener to bind must be not null!");
		DistributedDataContainer.LOG.debug("Bound {} as a DDC listener",
				listener.getClass().getSimpleName());
		DistributedDataContainer.cListeners.add(listener);

	}

	/**
	 * Removes the binding of a DDC listener. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param listener
	 *            The DDC listener to remove. <br />
	 *            Must be not null.
	 */
	public static void unbindDDCListener(IDDCListener listener) {

		Preconditions.checkNotNull(listener,
				"The DDC listener to unbind must be not null!");
		DistributedDataContainer.cListeners.remove(listener);
		DistributedDataContainer.LOG.debug("Unbound {} as a DDC listener",
				listener.getClass().getSimpleName());

	}

	/**
	 * Notifies the listeners.
	 * 
	 * @param state
	 *            The change state of the DDC. <br />
	 *            Must be not null.
	 * @param entry
	 *            The entry causing the change of the DDC. <br />
	 *            Must be not null.
	 */
	private static void setChanged(ChangeState state, DDCEntry entry) {

		Preconditions.checkNotNull(state,
				"The change state of the DDC must be not null!");
		Preconditions.checkNotNull(entry,
				"The entry causing a change of the DDC must be not null!");

		for (IDDCListener listener : DistributedDataContainer.cListeners) {

			switch (state) {

			case added:
				listener.ddcEntryAdded(entry);
				break;
			case removed:
				listener.ddcEntryRemoved(entry);
				break;
			default:
				DistributedDataContainer.LOG.error(
						"Unknown DDC change state: {}", state);

			}

		}

	}

	/**
	 * The internal data structure: DDC entries mapped to their keys.
	 */
	private final Map<DDCKey, DDCEntry> mEntries = Collections
			.synchronizedMap(new HashMap<DDCKey, DDCEntry>());

	/**
	 * Empty default constructor.
	 */
	public DistributedDataContainer() {

		// Empty default constructor

	}

	@Override
	public boolean add(DDCEntry entry) {

		Preconditions.checkNotNull(entry,
				"The DDC entry to add must be not null!");

		if (this.containsKey(entry.getKey())
				&& entry.compareTimeStamps(this.mEntries.get(entry.getKey())) <= 0) {

			// there is already an entry with that key and the already stored
			// entry is newer
			DistributedDataContainer.LOG.debug(
					"Discarded {} due to an older timestamp!", entry);
			return false;

		}

		// complete new or newer entry (ts)
		synchronized (this.mEntries) {

			this.mEntries.put(entry.getKey(), entry);

		}

		DistributedDataContainer.LOG.debug("Added {} to the DDC.", entry);
		DistributedDataContainer.setChanged(ChangeState.added, entry);
		return true;

	}

	@Override
	public DDCEntry get(DDCKey key) throws MissingDDCEntryException {

		Preconditions.checkNotNull(key,
				"The key of the searched DDC entry must be not null!");

		if (!this.containsKey(key)) {

			throw new MissingDDCEntryException(key);

		}

		return this.mEntries.get(key);

	}

	@Override
	public String getValue(DDCKey key) throws MissingDDCEntryException {

		return this.get(key).getValue();

	}

	@Override
	public long getTimeStamp(DDCKey key) throws MissingDDCEntryException {

		return this.get(key).getTimeStamp();

	}
	
	@Override
	public Set<DDCKey> getKeys() {
		
		return this.mEntries.keySet();
		
	}

	@Override
	public List<DDCKey> getSortedKeys() {

		List<DDCKey> keysList = Lists.newArrayList();
		for (DDCKey key : this.mEntries.keySet()) {

			keysList.add(key);

		}
		Collections.sort(keysList);

		return keysList;

	}

	@Override
	public Set<String> getValues() {

		Set<String> values = Sets.newHashSet();

		for (DDCKey key : this.mEntries.keySet()) {

			values.add(this.mEntries.get(key).getValue());

		}

		return values;

	}

	@Override
	public boolean containsKey(DDCKey key) {

		return this.mEntries.containsKey(key);

	}

	@Override
	public String toString() {

		String ddcString = "Current DDC:\n";

		for (DDCKey key : this.getSortedKeys()) {

			ddcString += this.mEntries.get(key) + "\n";

		}

		return ddcString;

	}

	@Override
	public boolean remove(DDCKey key) {

		Optional<DDCEntry> optEntry = Optional.absent();

		synchronized (this.mEntries) {

			optEntry = Optional.fromNullable(this.mEntries.remove(key));

		}

		if (optEntry.isPresent()) {

			DistributedDataContainer.LOG.debug("Removed {} from DDC.",
					optEntry.get());
			DistributedDataContainer.setChanged(ChangeState.removed,
					optEntry.get());

		} else {

			DistributedDataContainer.LOG
					.debug("No DDC entry found for {}", key);

		}

		return optEntry.isPresent();

	}

}