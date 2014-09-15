package de.uniol.inf.is.odysseus.peer.ddc.distribute.message;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class DistributedDataContainerCurrentStateResponseMessage implements
		IMessage {

	private boolean initiatingSuccessful;
	
	/**
	 * Default constructor.
	 */
	public DistributedDataContainerCurrentStateResponseMessage() {
	}

	public static DistributedDataContainerCurrentStateResponseMessage createMessage(boolean initiatingSuccessful) {
		DistributedDataContainerCurrentStateResponseMessage message = new DistributedDataContainerCurrentStateResponseMessage();
		message.initiatingSuccessful = initiatingSuccessful;
		return message;
	}

	@Override
	public byte[] toBytes() {
		ByteBuffer bb = null;
		int bbsize = 4;

		bb = ByteBuffer.allocate(bbsize);
		bb.putInt(initiatingSuccessful ? 1 : 0);
		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		initiatingSuccessful = bb.getInt() == 1 ? true : false;
	}

	public boolean isInitiatingSuccessful() {
		return initiatingSuccessful;
	}

	public void setInitiatingSuccessful(boolean initiatingSuccessful) {
		this.initiatingSuccessful = initiatingSuccessful;
	}
}
