package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.communication;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class PeerContractMessage implements IMessage {

	private double price;
	

	public double getPrice() {
		return price;
	}



	public PeerContractMessage(double price) {
		this.price = price;
	}
	
	public PeerContractMessage() {
		
	}
	

	@Override
	public byte[] toBytes() {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putDouble(price);
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		price = bb.getDouble();
	}

}
