package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNewPlugIn;

public class MessageByteBuffer {

	
	
	private final ByteBuffer buffer = ByteBuffer.allocate(P2PNewPlugIn.TRANSPORT_BUFFER_SIZE);
	private final ByteBuffer sizeBuffer = ByteBuffer.allocate(4);

	private int currentSize = 0;
	private int size = -1;
	
	private byte[] packet = null;
	
	public MessageByteBuffer() {

	}

	public void put(byte[] additionalData) {
		ByteBuffer message = ByteBuffer.wrap(additionalData);
		
		while (message.remaining() > 0) {

			if (size == -1) {
				while (sizeBuffer.position() < 4 && message.remaining() > 0) {
					sizeBuffer.put(message.get());
				}
				
				if (sizeBuffer.position() == 4) {
					sizeBuffer.flip();
					size = sizeBuffer.getInt();
				}
			}
			
			if (size != -1) {

				if (currentSize + message.remaining() < size) {
					currentSize = currentSize + message.remaining();
					buffer.put(message.get());
				} else {
					buffer.put(message.array(), message.position(), size - currentSize);
					message.position(message.position() + ( size - currentSize) );
					
					// 2. das fertige Objekt erstellen
					buffer.flip();
					packet = new byte[size];
					buffer.get(packet);
					buffer.clear();
					
					size = -1;
					sizeBuffer.clear();
					currentSize = 0;
					// Dann in der While-Schleife weiterverarbeiten
				}
			}
		}
	}

	public byte[] getPacket() {
		if( packet != null ) {
			byte[] p = packet;
			packet = null;
			return p;
		}
		
		return null;
	}
}
