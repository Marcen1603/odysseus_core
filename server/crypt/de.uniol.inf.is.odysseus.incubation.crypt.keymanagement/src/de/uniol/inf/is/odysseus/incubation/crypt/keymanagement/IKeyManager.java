package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement;

import java.security.PublicKey;
import java.util.List;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * The KeyManager is a class, who manages the keys in the public key vaults
 * 
 * @author MarkMilster
 *
 */
public interface IKeyManager {

	/**
	 * Returns the List of publicKeys with the given ids
	 * 
	 * @param id
	 *            List of ids, you want to get the publicKeys from
	 * @return List of PublicKeys (with Metadata), which match to the given ids
	 */
	public List<KeyWrapper<PublicKey>> getPublicKey(List<Integer> id);

	/**
	 * Adds a Public Key to the vault
	 * 
	 * @param key
	 *            PublicKey to add
	 * @return true, if it was added, false otherwise
	 */
	public boolean setPublicKey(KeyWrapper<PublicKey> key);

	/**
	 * Returns the EncSymKey, which match to the given parameters
	 * 
	 * @param receiverID
	 *            The id of the receiver, the key was encrypted for
	 * @param streamID
	 *            The id of the crypting, the encrypted sym key was used for
	 * @return The encKey (with metadata), which match to the given parameters
	 */
	public EncKeyWrapper getEncSymKey(int receiverID, int streamID);

	/**
	 * Adds a EncSymKey to the vault.
	 * 
	 * @param encKeyWrapper
	 *            EncSymKey to add
	 * @return ture, if it was added, false otherwise
	 */
	public boolean setEncSymKey(EncKeyWrapper encKeyWrapper);

}
