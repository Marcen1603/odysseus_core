/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.client;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;

/**
 * @author MarkMilster
 *
 */
public interface IEncKeyReceiver {
	
	public void onEncKeyReceived(EncKeyWrapper encKey);
	
}
