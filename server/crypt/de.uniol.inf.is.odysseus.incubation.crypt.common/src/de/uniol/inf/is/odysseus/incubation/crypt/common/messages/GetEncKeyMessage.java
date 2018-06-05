package de.uniol.inf.is.odysseus.incubation.crypt.common.messages;

import java.io.Serializable;

/**
 * Message to parse the necessary data to receive an EncKey to JSON
 * 
 * @author MarkMilster
 *
 */
public class GetEncKeyMessage implements Serializable {

	private static final long serialVersionUID = 7817040383495734709L;
	private int receiverId;
	private int streamId;
	private int receiverHash;

	/**
	 * Default constructor to parse to JSON
	 */
	public GetEncKeyMessage() {

	}

	/**
	 * Constructor with parameters
	 * 
	 * @param receiverId
	 *            ID of the owner of the public key, which encrypted the
	 *            symmetric key
	 * @param streamId
	 *            ID of the crypting, the encrypted symmetric key was used for
	 * @param receiverHash
	 *            HashCode of the receiver of the encrypted symmetric key, who
	 *            wants to use the symmetric key
	 */
	public GetEncKeyMessage(int receiverId, int streamId, int receiverHash) {
		this.receiverId = receiverId;
		this.streamId = streamId;
		this.receiverHash = receiverHash;
	}

	/**
	 * Returns the ReceiverId
	 * 
	 * @return the receiverId
	 */
	public int getReceiverId() {
		return receiverId;
	}

	/**
	 * Sets the ReceiverId
	 * 
	 * @param receiverId
	 *            the receiverId to set
	 */
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * Returns the StreamId
	 * 
	 * @return the streamId
	 */
	public int getStreamId() {
		return streamId;
	}

	/**
	 * Sets the StreamId
	 * 
	 * @param streamId
	 *            the streamId to set
	 */
	public void setStreamId(int streamId) {
		this.streamId = streamId;
	}

	/**
	 * Returns the HasCode of the Receiver
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
