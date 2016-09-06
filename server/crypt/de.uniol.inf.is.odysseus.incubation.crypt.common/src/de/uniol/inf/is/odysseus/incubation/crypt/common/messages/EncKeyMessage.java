/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.common.messages;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;

/**
 * @author MarkMilster
 *
 */
public class EncKeyMessage {
	
	private EncKeyWrapper encKey;
	
	public EncKeyMessage(EncKeyWrapper encKey) {
		this.encKey = encKey;
	}

	/**
	 * @return the encKey
	 */
	public EncKeyWrapper getEncKey() {
		return encKey;
	}

	/**
	 * @param encKey the encKey to set
	 */
	public void setEncKey(EncKeyWrapper encKey) {
		this.encKey = encKey;
	}

}
