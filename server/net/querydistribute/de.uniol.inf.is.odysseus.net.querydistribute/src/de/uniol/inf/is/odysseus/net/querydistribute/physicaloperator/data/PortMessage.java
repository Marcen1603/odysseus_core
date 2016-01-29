package de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator.data;

import java.nio.ByteBuffer;
import java.util.UUID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;

public class PortMessage implements IMessage {

	private int port;
	private UUID connectionID;
	
	public PortMessage() {
		
	}
	
	public PortMessage( UUID connectionID, int port ) {
		Preconditions.checkNotNull(connectionID, "connectionID must not be null!");

		this.port = port;
		this.connectionID = connectionID;
	}
	
	@Override
	public byte[] toBytes() {
		String connectionIDString = connectionID.toString();
		ByteBuffer bb = ByteBuffer.allocate(4 + connectionIDString.length() + 4);
		
		bb.putInt(port);
		MessageUtils.putString(bb, connectionIDString);
		
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		port = bb.getInt();
		String connectionIDString = MessageUtils.getString(bb);
		
		connectionID = UUID.fromString(connectionIDString);
	}

	public int getPort() {
		return port;
	}
	
	public UUID getConnectionID() {
		return connectionID;
	}
}
