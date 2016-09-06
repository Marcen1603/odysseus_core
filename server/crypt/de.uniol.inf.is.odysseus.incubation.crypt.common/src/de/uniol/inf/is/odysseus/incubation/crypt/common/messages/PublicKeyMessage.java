/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.common.messages;

import java.security.PublicKey;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * @author MarkMilster
 *
 */
public class PublicKeyMessage {
	
	private KeyWrapper<PublicKey> publicKey;
	
	public PublicKeyMessage(KeyWrapper<PublicKey> publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * @return the publicKey
	 */
	public KeyWrapper<PublicKey> getPublicKey() {
		return publicKey;
	}

	/**
	 * @param publicKey the publicKey to set
	 */
	public void setPublicKey(KeyWrapper<PublicKey> publicKey) {
		this.publicKey = publicKey;
	}

}
