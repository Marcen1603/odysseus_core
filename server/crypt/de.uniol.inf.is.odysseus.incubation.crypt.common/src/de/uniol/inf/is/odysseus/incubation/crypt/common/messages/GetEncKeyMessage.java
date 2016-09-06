/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.common.messages;

/**
 * @author MarkMilster
 *
 */
public class GetEncKeyMessage {

	private int receiverId;
	private int streamId;

	public GetEncKeyMessage(int receiverId, int streamId) {
		this.receiverId = receiverId;
		this.streamId = streamId;
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
