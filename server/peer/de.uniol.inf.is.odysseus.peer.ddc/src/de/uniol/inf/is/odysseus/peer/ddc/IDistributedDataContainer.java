package de.uniol.inf.is.odysseus.peer.ddc;

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
	public boolean add(String[] key, String value, long ts);
	
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
	public boolean add(String key, String value, long ts);

	/**
	 * Gets an entry of the DDC.
	 * 
	 * @param key
	 *            The multidimensional key for the searched entry.
	 * @return The value for <code>key</code>.
	 * @throws MissingDDCEntryException
	 *             if there is no entry with <code>key</code> as key.
	 */
	public String get(String[] key) throws MissingDDCEntryException;

	/**
	 * Gets an entry of the DDC.
	 * 
	 * @param key
	 *            The 1-dimensional key for the searched entry.
	 * @return The value for <code>key</code>.
	 * @throws MissingDDCEntryException
	 *             if there is no entry with <code>key</code> as key.
	 */
	public String get(String key) throws MissingDDCEntryException;

	/**
	 * Gets the keys of all entries of the DDC.
	 * 
	 * @return A set containing all multidimensional keys.
	 */
	public Set<String[]> getKeys();

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
	 *            The given multidimensional key.
	 * @return True, if the DDC contains an entry <code>key</code> as key.
	 */
	public boolean containsKey(String[] key);

	/**
	 * Checks, if the DDC contains a given key.
	 * 
	 * @param key
	 *            The given 1-dimensional key.
	 * @return True, if the DDC contains an entry <code>key</code> as key.
	 */
	public boolean containsKey(String key);

	/**
	 * Removes an entry.
	 * 
	 * @param key
	 *            The given multidimensional key.
	 * @return True, if there is an DDC entry for the given key. If this is the
	 *         case, the entry has been removed successfully. False, if there is
	 *         no DDC entry for the given key.
	 */
	public boolean remove(String[] key);

	/**
	 * Removes an entry.
	 * 
	 * @param key
	 *            The given 1-dimensional key.
	 * @return True, if there is an DDC entry for the given key. If this is the
	 *         case, the entry has been removed successfully. False, if there is
	 *         no DDC entry for the given key.
	 */
	public boolean remove(String key);

}