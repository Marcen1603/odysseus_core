package de.uniol.inf.is.odysseus.peer.ddc;

import java.util.List;
import java.util.Set;

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
public interface IDistributedDataContainer {

	/**
	 * Adds an entry to the DDC.
	 * 
	 * @param entry
	 *            The entry to add. <br />
	 *            Must be not null.
	 * @return True, if the entry could be added. False, if the key already
	 *         exists and the entry has the same timestamp or an older one than
	 *         the stored one.
	 */
	public boolean add(DDCEntry entry);

	/**
	 * The entry for a given key.
	 * 
	 * @param key
	 *            The multidimensional key for the searched entry. <br />
	 *            Must be not null.
	 * @return The entry for <code>key</code>.
	 * @throws if
	 *             there is no entry with <code>key</code> as key.
	 */
	public DDCEntry get(DDCKey key) throws MissingDDCEntryException;

	/**
	 * Gets the value of a DDC entry.
	 * 
	 * @param key
	 *            The multidimensional key for the searched entry. <br />
	 *            Must be not null.
	 * @return The value for <code>key</code>.
	 * @throws MissingDDCEntryException
	 *             if there is no entry with <code>key</code> as key.
	 */
	public String getValue(DDCKey key) throws MissingDDCEntryException;

	/**
	 * Gets the timestamp of a DDC entry.
	 * 
	 * @param key
	 *            The multidimensional key for the searched entry. <br />
	 *            Must be not null.
	 * @return The timestamp of the entry for <code>key</code>.
	 * @throws MissingDDCEntryException
	 *             if there is no entry with <code>key</code> as key.
	 */
	public long getTimeStamp(DDCKey key) throws MissingDDCEntryException;

	/**
	 * Gets the keys of all entries of the DDC.
	 * 
	 * @return A set containing all multidimensional keys.
	 */
	public Set<DDCKey> getKeys();
	
	/**
	 * Gets the keys of all entries of the DDC.
	 * 
	 * @return A sorted list containing all multidimensional keys.
	 */
	public List<DDCKey> getSortedKeys();

	/**
	 * Gets the values of all entries of the DDC.
	 * 
	 * @return A set containing all values.
	 */
	public Set<String> getValues();

	/**
	 * Checks, if the DDC contains a given key.
	 * 
	 * @param key
	 *            The given multidimensional key. <br />
	 *            Must be not null.
	 * @return True, if the DDC contains an entry <code>key</code> as key.
	 */
	public boolean containsKey(DDCKey key);

	/**
	 * Removes an entry.
	 * 
	 * @param key
	 *            The given multidimensional key. <br />
	 *            Must be not null.
	 * @return True, if there is an DDC entry for the given key. If this is the
	 *         case, the entry has been removed successfully. False, if there is
	 *         no DDC entry for the given key.
	 */
	public boolean remove(DDCKey key);

}