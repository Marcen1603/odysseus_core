/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;

/**
 * @author MarkMilster
 *
 */
public interface IDbEncSymKeyVault {

	public boolean insertEncSymKey(EncKeyWrapper encKeyWrapper);

	public EncKeyWrapper getEncSymKey(int receiverID, int streamID);

}
