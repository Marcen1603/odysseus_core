package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys;

/**
 * Wrapper for EncKeys with metadata. 
 * 
 * @author MarkMilster
 *
 */
public class EncKeyWrapper extends KeyWrapper<byte[]> {

	private static final long serialVersionUID = -3166317543340394268L;

	private String algoritm;
	private int receiverId;
	private int streamId;

	/**
	 * Returns the symmetric algorithm of the encrypted key.
	 * 
	 * @return the algoritm
	 */
	public String getAlgoritm() {
		return algoritm;
	}

	/**
	 * Sets the algorithm
	 * 
	 * @param algoritm
	 *            the algoritm to set
	 */
	public void setAlgoritm(String algoritm) {
		this.algoritm = algoritm;
	}

	/**
	 * Returns the ReceiverId of the EncKey
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
	 * Returns the StreamId of the crypting with the encrypted SymKey
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

}
