package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author MarkMilster
 *
 */
public class ASymKeyWrapper {

	private KeyWrapper<PrivateKey> privateKeyWrapper;
	private KeyWrapper<PublicKey> publicKeyWrapper;

	/**
	 * @return the privateKeyWrapper
	 */
	public KeyWrapper<PrivateKey> getPrivateKeyWrapper() {
		return privateKeyWrapper;
	}

	/**
	 * @param privateKeyWrapper
	 *            the privateKeyWrapper to set
	 */
	public void setPrivateKeyWrapper(KeyWrapper<PrivateKey> privateKeyWrapper) {
		this.privateKeyWrapper = privateKeyWrapper;
	}

	/**
	 * @return the publicKeyWrapper
	 */
	public KeyWrapper<PublicKey> getPublicKeyWrapper() {
		return publicKeyWrapper;
	}

	/**
	 * @param publicKeyWrapper
	 *            the publicKeyWrapper to set
	 */
	public void setPublicKeyWrapper(KeyWrapper<PublicKey> publicKeyWrapper) {
		this.publicKeyWrapper = publicKeyWrapper;
	}

}
