/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys;

import java.security.Key;
import java.time.LocalDateTime;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * @author MarkMilster
 *
 */
public interface ISymKeyFactory {
	
	public KeyWrapper<Key> createSymKey(String algorithm, LocalDateTime valid, String comment);

}
