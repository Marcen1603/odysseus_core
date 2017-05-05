package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys;

import java.security.Key;
import java.time.LocalDateTime;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * A Factory, which can create Keys for symmetric cryptographic algorithms
 * 
 * @author MarkMilster
 *
 */
public interface ISymKeyFactory {

	/**
	 * Creates a symmetric key with Metadata
	 * 
	 * @param algorithm
	 *            The cryptographic algorithm, the key will be used for
	 * @param valid
	 *            The date, until the key will be vaild
	 * @param comment
	 *            Any comment
	 * @return A Wrapper, which contains the symmetric key and the metadata
	 */
	public KeyWrapper<Key> createSymKey(String algorithm, LocalDateTime valid, String comment);

}
