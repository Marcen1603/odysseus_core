package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;

/**
 * A KeyVault, which uses a database to store encrypted keys.
 * 
 * @author MarkMilster
 *
 */
public interface IDbEncSymKeyVault {

	/**
	 * Inserts an encrypted Key to the database.
	 * 
	 * @param encKeyWrapper
	 *            The encKey to insert
	 * @return true, if it was inserted, false otherwise
	 */
	public boolean insertEncSymKey(EncKeyWrapper encKeyWrapper);

	/**
	 * Returns the EncryptedKey (with Metadata), which is identified by the
	 * parameters
	 * 
	 * @param receiverID
	 *            The receiver of the encrypted key. Which his public key it was
	 *            encrypted.
	 * @param streamID
	 *            The id of the crypting, the encrypted sym Key was used for
	 * @return The specified Key
	 */
	public EncKeyWrapper getEncSymKey(int receiverID, int streamID);

}
