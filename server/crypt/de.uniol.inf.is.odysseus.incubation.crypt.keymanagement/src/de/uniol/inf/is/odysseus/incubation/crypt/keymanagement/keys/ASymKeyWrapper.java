package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Wrapper for ASymKeys.<br>
 * This wrapper contains a PrivateKeyWrapper and a PublicKeyWrapper, but no
 * metadata. The metadata is stored in the wrappers.
 * 
 * @author MarkMilster
 *
 */
public class ASymKeyWrapper {

	private KeyWrapper<PrivateKey> privateKeyWrapper;
	private KeyWrapper<PublicKey> publicKeyWrapper;

	/**
	 * Returns the PrivateKeyWrapper
	 * 
	 * @return the privateKeyWrapper
	 */
	public KeyWrapper<PrivateKey> getPrivateKeyWrapper() {
		return privateKeyWrapper;
	}

	/**
	 * Sets the PrivateKeyWrapper.
	 * 
	 * @param privateKeyWrapper
	 *            the privateKeyWrapper to set
	 */
	public void setPrivateKeyWrapper(KeyWrapper<PrivateKey> privateKeyWrapper) {
		this.privateKeyWrapper = privateKeyWrapper;
	}

	/**
	 * Returns the PublicKeyWrapper
	 * 
	 * @return the publicKeyWrapper
	 */
	public KeyWrapper<PublicKey> getPublicKeyWrapper() {
		return publicKeyWrapper;
	}

	/**
	 * Sets the PublicKeyWrapper
	 * 
	 * @param publicKeyWrapper
	 *            the publicKeyWrapper to set
	 */
	public void setPublicKeyWrapper(KeyWrapper<PublicKey> publicKeyWrapper) {
		this.publicKeyWrapper = publicKeyWrapper;
	}

}
