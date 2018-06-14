package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.nio.ByteBuffer;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class QueryPartAddFailMessage implements IMessage {

	private int queryPartID;
	private String message;
	
	public QueryPartAddFailMessage() {
		
	}
	
	public QueryPartAddFailMessage( int queryPartID, String message ) {
		this.queryPartID = queryPartID;
		this.message = message;
		
		if( Strings.isNullOrEmpty(message) ) {
			message = "FAILED";
		}
	}
	
	@Override
	public byte[] toBytes() {
		byte[] messageBytes = message.getBytes();
		return ByteBuffer.allocate(4 + messageBytes.length).putInt(queryPartID).put(messageBytes).array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		queryPartID = bb.getInt();
		
		byte[] messageBytes = new byte[data.length - 4];
		bb.get(messageBytes);
		
		message = new String(messageBytes);
	}

	public int getQueryPartID() {
		return queryPartID;
	}
	
	public String getMessage() {
		return message;
	}
}
