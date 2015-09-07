package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class FastBidMessage implements IMessage {

	
	private double bid;
	private int auctionID;
	
	
	@Override
	public byte[] toBytes() {
		ByteBuffer bb = ByteBuffer.allocate(12);
		bb.putInt(auctionID);
		bb.putDouble(bid);
		return bb.array();
	}
	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		auctionID = bb.getInt();
		bid = bb.getDouble();
	}
	
	public FastBidMessage() {
		
	}
	
	public FastBidMessage(int auctionID, double bid) {
		this.bid = bid;
		this.auctionID = auctionID;
	}
	
	public double getBid() {
		return bid;
	}
	public void setBid(double bid) {
		this.bid = bid;
	}
	public int getAuctionID() {
		return auctionID;
	}
	public void setAuctionID(int auctionID) {
		this.auctionID = auctionID;
	}
	
	
	
}
