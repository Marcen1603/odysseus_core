package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret;

import java.security.Key;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * This interface should be implemented by every Symmektric Key Vault
 * 
 * @author MarkMilster
 *
 */
public interface ISymKeyVault {

	/**
	 * Returns the symmetricKey, which is identified by the given id
	 * 
	 * @param id
	 *            The id, of the symmetric key, you want to get
	 * @return The symmetric key, identified by the given id
	 */
	public KeyWrapper<Key> getSymKey(int id);

	/**
	 * Stores the given symmetric key into the vault
	 * 
	 * @param key the symmetricKey, you want to store
	 * @return true, if the symmetric key was stored, false otherwise
	 */
	public boolean setSymKey(KeyWrapper<Key> key);

}
