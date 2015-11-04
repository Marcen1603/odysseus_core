package de.uniol.inf.is.odysseus.net.connect.socket;

import java.nio.ByteBuffer;
import java.util.List;

import com.google.common.collect.Lists;

public class MessageByteBuffer {

	private static final int BUFFER_SIZE = 640*480*3+(1+4);
	
	private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
	private final ByteBuffer sizeBuffer = ByteBuffer.allocate(4);

	private int currentSize = 0;
	private int size = -1;
	
	private final List<byte[]> packets1 = Lists.newLinkedList();
	private final List<byte[]> packets2 = Lists.newLinkedList();
	private List<byte[]> packetsPointer = packets1;
	
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
					buffer.put(message);
					
					
				} else {
					buffer.put(message.array(), message.position(), size - currentSize);
					message.position(message.position() + ( size - currentSize) );
					
					// 2. das fertige Objekt erstellen
					buffer.flip();
					byte[] packet = new byte[size];
					buffer.get(packet);
					packetsPointer.add(packet);
					
					buffer.clear();
					
					size = -1;
					sizeBuffer.clear();
					currentSize = 0;
					// Dann in der While-Schleife weiterverarbeiten
				}
			}
		}
	}

	public List<byte[]> getPackets() {
		List<byte[]> result = packetsPointer;
		
		if( packetsPointer == packets1 ) {
			packetsPointer = packets2;
		} else {
			packetsPointer = packets1;
		}
		packetsPointer.clear();
		
		return result;
	}
}
