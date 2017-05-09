package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret;

import java.security.PrivateKey;
import java.util.List;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * This interface should be implemented by every Private Key Vault
 * 
 * @author MarkMilster
 *
 */
public interface IPrivKeyVault {

	/**
	 * Returns all PrivateKeys, identified by the given ids
	 * 
	 * @param id
	 *            ids, of the keys, you want to get
	 * @return The PrivateKeys, identified by the ids
	 */
	public List<KeyWrapper<PrivateKey>> getPrivateKey(List<Integer> id);

	/**
	 * Stores the given PrivateKeys
	 * 
	 * @param privateKey
	 *            privateKeys to store
	 * @return true, if the privateKeys were stored, false otherwise
	 */
	public boolean setPrivateKey(KeyWrapper<PrivateKey> privateKey);

}
