package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.communication;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class PeerContractMessage implements IMessage {

	private double minPrice;
	private double maxPrice;
	
	public double getMinPrice() {
		return minPrice;
	}


	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}


	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}



	public PeerContractMessage(double minPrice, double maxPrice) {
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}
	
	public PeerContractMessage() {
		
	}
	

	@Override
	public byte[] toBytes() {
		ByteBuffer bb = ByteBuffer.allocate(8+8);
		bb.putDouble(minPrice);
		bb.putDouble(maxPrice);
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		minPrice = bb.getDouble();
		maxPrice = bb.getDouble();
		
	}

}
