package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys;

import java.time.LocalDateTime;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.ASymKeyWrapper;

/**
 * Factory, which can create AsymKey Pairs. It should create a PublicKey and a
 * PrivateKey
 * 
 * @author MarkMilster
 *
 */
public interface IAsymKeyFactory {

	/**
	 * Creates a AsymKeyPair, with the given settings and metadata
	 * 
	 * @param algorithm
	 *            The cryptographicAlgorithm, the keys will be used for
	 * @param size
	 *            The size of the keys. It should merge the algorithm
	 * @param valid
	 *            The date, until the keys will be vaild
	 * @param comment
	 *            Any comment
	 * @return A Wrapper, which contains the metadata, the PublicKey and the
	 *         PrivateKey
	 */
	public ASymKeyWrapper createAsymKeyPair(String algorithm, int size, LocalDateTime valid, String comment);

}
