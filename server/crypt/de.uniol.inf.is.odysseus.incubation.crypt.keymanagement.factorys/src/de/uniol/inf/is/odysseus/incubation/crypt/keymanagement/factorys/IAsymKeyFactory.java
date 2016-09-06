/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys;

import java.time.LocalDateTime;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.ASymKeyWrapper;

/**
 * @author MarkMilster
 *
 */
public interface IAsymKeyFactory {
	
	public ASymKeyWrapper createAsymKeyPair(String algorithm, int size, LocalDateTime valid, String comment);

}
