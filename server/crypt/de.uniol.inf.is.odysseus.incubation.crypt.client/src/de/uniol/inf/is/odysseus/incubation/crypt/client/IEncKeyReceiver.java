package de.uniol.inf.is.odysseus.incubation.crypt.client;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;

/**
 * The Receiver of an EncKey, which could be loaded out of a vault.
 * 
 * @author MarkMilster
 *
 */
public interface IEncKeyReceiver extends IKeyReceiver {

	/**
	 * This method should be called, if you receive an EncKey
	 * 
	 * @param encKey
	 *            The encKey
	 * @param receiverHash
	 *            The hashCode of a receiver of the symmetric key, encrypted in
	 *            the EncKey to identify the receiver
	 */
	public void onEncKeyReceived(EncKeyWrapper encKey, int receiverHash);

}
