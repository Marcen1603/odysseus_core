package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * This interface should be implemented by a KeyVault. There can keys be stored.
 * 
 * @author MarkMilster
 *
 */
public interface IKeyVault {

	/**
	 * Insert a key into the vault.
	 * 
	 * @param key
	 *            The key to insert
	 * @return true, if it was inserted, false otherwise
	 */
	public boolean insertKey(KeyWrapper<?> key);

	/**
	 * Returns a key with the given id
	 * 
	 * @param id
	 *            The id of the key, you want to get
	 * @return The key (with Metadata), specified by the id
	 */
	public KeyWrapper<?> getKey(int id);

}
