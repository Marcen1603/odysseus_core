/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys;

/**
 * @author MarkMilster
 *
 */
public class EncKeyWrapper extends KeyWrapper<byte[]> {

	private static final long serialVersionUID = -3166317543340394268L;

	private String algoritm;
	private int receiverId;
	private int streamId;

	/**
	 * @return the algoritm
	 */
	public String getAlgoritm() {
		return algoritm;
	}

	/**
	 * @param algoritm
	 *            the algoritm to set
	 */
	public void setAlgoritm(String algoritm) {
		this.algoritm = algoritm;
	}

	/**
	 * @return the receiverId
	 */
	public int getReceiverId() {
		return receiverId;
	}

	/**
	 * @param receiverId
	 *            the receiverId to set
	 */
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * @return the streamId
	 */
	public int getStreamId() {
		return streamId;
	}

	/**
	 * @param streamId
	 *            the streamId to set
	 */
	public void setStreamId(int streamId) {
		this.streamId = streamId;
	}

}
