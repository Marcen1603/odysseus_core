package de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class LoadBalancingDeleteConnectionMessage implements IMessage {

	/*
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	private String oldPipeId;
	private Boolean isSender;

	
	public LoadBalancingDeleteConnectionMessage() {
	}
	
	public LoadBalancingDeleteConnectionMessage(int loadBalancingProcessId, String oldPipeId, Boolean isSender) {
		this.loadBalancingProcessId = loadBalancingProcessId;
		this.oldPipeId = oldPipeId;
		this.isSender = isSender;
	
	}


	@Override
	public byte[] toBytes() {
		/*
		 * Allocate byte Buffer:
		 * 	4 Bytes for integer loadBalancingProcessId
		 *  4 Bytes for integer Size of oldPipeId
		 *  4 Bytes for boolean (treated as Int) isSender
		 */
		
		byte[] oldPipeIdBytes = oldPipeId.getBytes();
	
		
		int bbsize =  4 + 4 + 4 + oldPipeIdBytes.length;
		
		ByteBuffer bb = ByteBuffer.allocate(bbsize);
		bb.putInt(loadBalancingProcessId);
		bb.putInt(oldPipeIdBytes.length);	
		bb.put(oldPipeIdBytes);
		
		if(isSender) {
			bb.putInt(1);
		}
		else {
			bb.putInt(0);
		}
	
		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		loadBalancingProcessId = bb.getInt();
		int sizeOfOldPipeId = bb.getInt();
		int isSenderInt = bb.getInt();
		
		this.isSender = (isSenderInt == 1);
		
		byte[] oldPipeIdAsBytes = new byte[sizeOfOldPipeId];
		
		bb.get(oldPipeIdAsBytes);

		this.oldPipeId = new String(oldPipeIdAsBytes);
		
	}


	public int getLoadBalancingProcessId() {
		return loadBalancingProcessId;
	}


	public void setLoadBalancingProcessId(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}


	public String getOldPipeId() {
		return oldPipeId;
	}

	public void setOldPipeId(String oldPipeId) {
		this.oldPipeId = oldPipeId;
	}


	/**
	 * True if we look for a sender.
	 * @return
	 */
	public boolean isSender() {
		return isSender;
	}

	/**
	 * Set true if we look for a sender.
	 * @param isSender
	 */
	public void setSender(boolean isSender) {
		this.isSender = isSender;
	}

	

}
