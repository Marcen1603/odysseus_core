/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * @author MarkMilster
 *
 */
public interface IKeyVault {
	
	public boolean insertKey(KeyWrapper<?> key);
	public KeyWrapper<?> getKey(int id);

}
