package de.uniol.inf.is.odysseus.incubation.crypt.common.messages;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;

/**
 * Message to send a EncKey
 * 
 * @author MarkMilster
 *
 */
public class EncKeyMessage implements Serializable {

	private static final long serialVersionUID = -6640014877826361906L;
	private EncKeyWrapper encKey;
	private int receiverHash;

	/**
	 * Default constructor to parse in JSON
	 */
	public EncKeyMessage() {

	}

	/**
	 * Constructor with parameters
	 * 
	 * @param encKey
	 *            The encKey of this message
	 * @param receiverHash
	 *            The receiver of the encrypted symmetric key<br>
	 *            The HashCode is used, so you dont have to send to many data in
	 *            JSON. With the HashCode you can identify the receiver
	 */
	public EncKeyMessage(EncKeyWrapper encKey, int receiverHash) {
		this.encKey = encKey;
		this.setReceiver(receiverHash);
	}

	/**
	 * Returns the EncKeyWrapper
	 * 
	 * @return the encKey
	 */
	public EncKeyWrapper getEncKey() {
		return encKey;
	}

	/**
	 * Sets the EncKeyWrapper
	 * 
	 * @param encKey
	 *            the encKey to set
	 */
	public void setEncKey(EncKeyWrapper encKey) {
		this.encKey = encKey;
	}

	/**
	 * Returns the HashCode of the Receiver
	 * 
	 * @return the receiver
	 */
	public int getReceiver() {
		return receiverHash;
	}

	/**
	 * Sets the HashCode of the Receiver
	 * 
	 * @param receiver
	 *            the receiver to set
	 */
	public void setReceiver(int receiverHash) {
		this.receiverHash = receiverHash;
	}

}
