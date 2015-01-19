package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class QueryManagementMessage implements IMessage {

	public static final int REGISTER_SLAVE = 0;
	public static final int ACK_REGISTER_SLAVE = 1;
	public static final int UNREGISTER_SLAVE = 2;
	public static final int ACK_UNREGISTER_SLAVE = 3;
	public static final int CHANGE_MASTER = 4;
	public static final int ACK_CHANGE_MASTER = 5;
	
	
	
	public static QueryManagementMessage createRegisterSlaveMsg(String sharedQueryID, String peerID) {
		QueryManagementMessage message = new QueryManagementMessage();
		message.sharedQueryID = sharedQueryID;
		message.peerID = peerID;
		message.msgType = REGISTER_SLAVE;
		return message;
	}
	
	public static QueryManagementMessage createUnregisterSlaveMsg(String sharedQueryID, String peerID) {
		QueryManagementMessage message = new QueryManagementMessage();
		message.sharedQueryID = sharedQueryID;
		message.peerID = peerID;
		message.msgType = UNREGISTER_SLAVE;
		return message;
	}
	
	public static QueryManagementMessage createChangeMasterMsg(String sharedQueryID, String peerID) {
		QueryManagementMessage message = new QueryManagementMessage();
		message.sharedQueryID = sharedQueryID;
		message.peerID = peerID;
		message.msgType = CHANGE_MASTER;
		return message;
	}
	
	
	
	
	private int msgType;
	public int getMsgType() {
		return msgType;
	}



	public String getSharedQueryID() {
		return sharedQueryID;
	}



	public String getPeerID() {
		return peerID;
	}

	private String sharedQueryID;
	private String peerID;
	
	
	public QueryManagementMessage() {
		
	}
	
	

	@Override
	public byte[] toBytes() {
		
		byte[] sharedQueryIDBytes = sharedQueryID.getBytes();
		byte[] peerIDBytes = peerID.getBytes();
		
		int bbsize = 4 + 4 + sharedQueryIDBytes.length + 4 + peerIDBytes.length;
		ByteBuffer bb = ByteBuffer.allocate(bbsize);
		bb.putInt(msgType);
		bb.putInt(sharedQueryIDBytes.length);
		bb.put(sharedQueryIDBytes);
		bb.putInt(peerIDBytes.length);
		bb.put(peerIDBytes);
		
		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		this.msgType = bb.getInt();
		
		int sharedQueryIDSize = bb.getInt();
		byte[] sharedQueryIDBytes = new byte[sharedQueryIDSize];
		bb.get(sharedQueryIDBytes);
		this.sharedQueryID = new String(sharedQueryIDBytes);
		
		int peerIDSize = bb.getInt();
		byte[] peerIDBytes = new byte[peerIDSize];
		bb.get(peerIDBytes);
		this.peerID = new String(peerID);
		
	}
}
