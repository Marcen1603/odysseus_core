package de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator;

import java.security.Key;
import java.util.List;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.ICryptor;

/**
 * This interface will be implemented by every receiver of crypted Data. <br>
 * They have asymmetric keys.
 * 
 * @author MarkMilster
 *
 */
public interface IReceiver {

	/**
	 * Returns the symmetric key wrapper.
	 * 
	 * @return The symmetric key wrapper
	 */
	public KeyWrapper<Key> getKeyWrapper();

	/**
	 * Returns the Cryptor
	 * 
	 * @return the Cryptor
	 */
	public ICryptor getCryptor();

	/**
	 * Returns the StreamID
	 * 
	 * @return the streamID
	 */
	public int getStreamId();

	/**
	 * Returns the receiverID
	 * 
	 * @return the receiverID
	 */
	public List<Integer> getReceiverId();

	/**
	 * Specifies, if the symmetric key is received, e.g. out of a vault.
	 * 
	 * @param keyReceived
	 *            true, if the symmetric key is received <br>
	 *            false, if not
	 */
	public void setKeyReceived(boolean keyReceived);

}
