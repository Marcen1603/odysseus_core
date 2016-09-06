/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement;

import java.security.PublicKey;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * @author MarkMilster
 *
 */
public interface IKeyManager {

	public KeyWrapper<PublicKey> getPublicKey(int id);

	public boolean setPublicKey(KeyWrapper<PublicKey> key);

	public EncKeyWrapper getEncSymKey(int receiverID, int streamID);

	public boolean setEncSymKey(EncKeyWrapper encKeyWrapper);

}
