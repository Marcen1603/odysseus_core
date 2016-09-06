/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.client;

import java.security.PublicKey;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * @author MarkMilster
 *
 */
public interface IPubKeyReceiver {

	public void onPubKeyReceived(KeyWrapper<PublicKey> pubKeyWrapper);
	
}
