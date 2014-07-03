package de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Message to which advises Peers to duplicate a particular connection.
 * @author Carsten Cordes
 *
 */
public class LoadBalancingCopyConnectionMessage implements IMessage {

	/**
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	
	/**
	 * Determines if a sender or a receiver needs to be reproduced.
	 */
	private boolean isSender;
	
	/**
	 * New Peer to connect to.
	 */
	private String newPeerId;
	
	/**
	 * Old Pipe to substitute.
	 */
	private String oldPipeId;
	
	/**
	 * New pipe, which should substitute old pipe.
	 */
	private String newPipeId;
	
	
	/**
	 * Default constructor.
	 */
	public LoadBalancingCopyConnectionMessage() {
	}
	
	/**
	 * Constructor
	 * @param loadBalancingProcessId 
	 * @param isSender determines if Operator to copy is sender or receiver.
	 * @param oldPipeId Pipe ID to replace
	 * @param newPipeId new Pipe Id
	 * @param newPeerId new Peer Id.
	 */
	public LoadBalancingCopyConnectionMessage(int loadBalancingProcessId, boolean isSender, String oldPipeId, String newPipeId, String newPeerId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
		this.isSender = isSender;
		this.oldPipeId = oldPipeId;
		this.newPipeId = newPipeId;
		this.newPeerId = newPeerId;
	}


	@Override
	/**
	 * Returns message as byte array.
	 */
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
	/**
	 * Parses message from byte array.
	 */
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


	/**
	 * returns Load Balancing Process Id.
	 * @return load Balancing Process Id.
	 */
	public int getLoadBalancingProcessId() {
		return loadBalancingProcessId;
	}


	/**
	 * Sets load Balancing Process Id.
	 * @param loadBalancingProcessId
	 */
	public void setLoadBalancingProcessId(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
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

	/**
	 * Returns Peer Id.
	 * @return peer id.
	 */
	public String getNewPeerId() {
		return newPeerId;
	}

	/**
	 * Sets peer id.
	 * @param newPeerId
	 */
	public void setNewPeerId(String newPeerId) {
		this.newPeerId = newPeerId;
	}

	/**
	 * Returns old Pipe Id
	 * @return old Pipe ID.
	 */
	public String getOldPipeId() {
		return oldPipeId;
	}

	/**
	 * sets old pipe ID.
	 * @param oldPipeId
	 */
	public void setOldPipeId(String oldPipeId) {
		this.oldPipeId = oldPipeId;
	}

	/**
	 * Gets new pipe Id.
	 * @return
	 */
	public String getNewPipeId() {
		return newPipeId;
	}

	/**
	 * sets new pipe Id.
	 * @param newPipeId
	 */
	public void setNewPipeId(String newPipeId) {
		this.newPipeId = newPipeId;
	}
	

}
