package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class ReleaseLockMessage implements IMessage{

	private int lockingID;
	
	public int getLockingID() {
		return lockingID;
	}

	public void setLockingID(int lockingID) {
		this.lockingID = lockingID;
	}

	public ReleaseLockMessage() {
		
	}
	
	public ReleaseLockMessage(int lockingID) {
		
	}
	
	@Override
	public byte[] toBytes() {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(lockingID);
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
	    ByteBuffer bb = ByteBuffer.wrap(data);
	    lockingID = bb.getInt();
	}

}
