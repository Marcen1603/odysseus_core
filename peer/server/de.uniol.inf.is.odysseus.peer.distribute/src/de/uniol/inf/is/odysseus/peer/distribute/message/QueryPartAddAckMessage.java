package de.uniol.inf.is.odysseus.peer.distribute.message;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class QueryPartAddAckMessage implements IMessage {

	private int queryPartID;
	
	public QueryPartAddAckMessage(AddQueryPartMessage addQueryPartMessage) {
		queryPartID = addQueryPartMessage.getQueryPartID();
	}
	
	public QueryPartAddAckMessage() {
		
	}

	@Override
	public byte[] toBytes() {
		return ByteBuffer.allocate(4).putInt(queryPartID).array();
	}

	@Override
	public void fromBytes(byte[] data) {
		queryPartID = ByteBuffer.wrap(data).getInt();
	}

	public int getQueryPartID() {
		return queryPartID;
	}
}
