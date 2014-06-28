package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class LoadBalancingCopyConnectionMessage implements IMessage {

	/*
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	private boolean isSender;
	private String newPeerId;
	private String oldPipeId;
	private String newPipeId;
	
	
	public LoadBalancingCopyConnectionMessage() {
	}
	
	public LoadBalancingCopyConnectionMessage(int loadBalancingProcessId, boolean isSender, String oldPipeId, String newPipeId, String newPeerId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
		this.isSender = isSender;
		this.oldPipeId = oldPipeId;
		this.newPipeId = newPipeId;
		this.newPeerId = newPeerId;
	}


	@Override
	public byte[] toBytes() {
		/*
		 * Allocate byte Buffer:
		 * 	4 Bytes for integer loadBalancingProcessId
		 *  4 Bytes for integer Size of oldPipeId
		 *  4 Bytes for integer Size of newPipeId
		 *  4 Bytes for integer Size of newPeerId
		 *  4 Bytes for boolean (treated as Int) isSender;
		 *  oldPipeId
		 *  newPipeId
		 *  newPeerId
		 */
		
		byte[] oldPipeIdBytes = oldPipeId.getBytes();
		byte[] newPipeIdBytes = newPipeId.getBytes();
		byte[] newPeerIdBytes = newPeerId.getBytes();
		
		
		int bbsize = 4 + 4 + 4 + 4 + 4 + oldPipeIdBytes.length + newPipeIdBytes.length + newPeerIdBytes.length;
		
		ByteBuffer bb = ByteBuffer.allocate(bbsize);
		bb.putInt(loadBalancingProcessId);
		bb.putInt(oldPipeIdBytes.length);
		bb.putInt(newPipeIdBytes.length);
		bb.putInt(newPeerIdBytes.length);
		if(isSender) {
			bb.putInt(1);
		}
		else {
			bb.putInt(0);
		}
		bb.put(oldPipeIdBytes);
		bb.put(newPipeIdBytes);
		bb.put(newPeerIdBytes);
		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		loadBalancingProcessId = bb.getInt();
		int sizeOfOldPipeId = bb.getInt();
		int sizeOfNewPipeId = bb.getInt();
		int sizeOfNewPeerId = bb.getInt();
		int isSenderInt = bb.getInt();
		
		this.isSender = (isSenderInt == 1);
		
		byte[] oldPipeIdAsBytes = new byte[sizeOfOldPipeId];
		byte[] newPipeIdAsBytes = new byte[sizeOfNewPipeId];
		byte[] newPeerIdAsBytes = new byte[sizeOfNewPeerId];
		
		bb.get(oldPipeIdAsBytes);
		bb.get(newPipeIdAsBytes);
		bb.get(newPeerIdAsBytes);
		
		this.oldPipeId = new String(oldPipeIdAsBytes);
		this.newPipeId = new String(newPipeIdAsBytes);
		this.newPeerId = new String(newPeerIdAsBytes);
		
	}


	public int getLoadBalancingProcessId() {
		return loadBalancingProcessId;
	}


	public void setLoadBalancingProcessId(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}

	public boolean isSender() {
		return isSender;
	}

	public void setSender(boolean isSender) {
		this.isSender = isSender;
	}

	public String getNewPeerId() {
		return newPeerId;
	}

	public void setNewPeerId(String newPeerId) {
		this.newPeerId = newPeerId;
	}

	public String getOldPipeId() {
		return oldPipeId;
	}

	public void setOldPipeId(String oldPipeId) {
		this.oldPipeId = oldPipeId;
	}

	public String getNewPipeId() {
		return newPipeId;
	}

	public void setNewPipeId(String newPipeId) {
		this.newPipeId = newPipeId;
	}
	

}
