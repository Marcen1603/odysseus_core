package de.uniol.inf.is.odysseus.peer.distribute.message;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class RemoveQueryAckMessage implements IMessage {

	private int id;
	
	public RemoveQueryAckMessage(RemoveQueryMessage removeMessage) {
		id = removeMessage.getId();
	}
	
	public RemoveQueryAckMessage() {
		
	}
	
	@Override
	public byte[] toBytes() {
		return ByteBuffer.allocate(4).putInt(id).array();
	}

	@Override
	public void fromBytes(byte[] data) {
		id = ByteBuffer.wrap(data).getInt();
	}

	public int getId() {
		return id;
	}
}
