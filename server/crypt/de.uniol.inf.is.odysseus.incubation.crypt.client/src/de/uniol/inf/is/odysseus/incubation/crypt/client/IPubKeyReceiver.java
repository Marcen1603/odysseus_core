package de.uniol.inf.is.odysseus.incubation.crypt.client;

import java.security.PublicKey;
import java.util.List;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * The Receiver of an PubKey, which could be loaded out of a vault.
 * 
 * @author MarkMilster
 *
 */
public interface IPubKeyReceiver extends IKeyReceiver {

	/**
	 * This method should be called, if you receive PubKeys
	 * 
	 * @param pubKeyWrapper
	 *            The List of PubKeys
	 */
	public void onPubKeyReceived(List<KeyWrapper<PublicKey>> pubKeyWrapper);

}
