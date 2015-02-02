package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNewPlugIn;

public class MessageByteBuffer {

	private static final Logger LOG = LoggerFactory.getLogger(MessageByteBuffer.class);
	
	private final ByteBuffer buffer = ByteBuffer.allocate(P2PNewPlugIn.TRANSPORT_BUFFER_SIZE);
	private final ByteBuffer sizeBuffer = ByteBuffer.allocate(4);

	private int currentSize = 0;
	private int size = -1;
	
	private final List<byte[]> packets1 = Lists.newLinkedList();
	private final List<byte[]> packets2 = Lists.newLinkedList();
	private List<byte[]> packetsPointer = packets1;
	
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
					LOG.debug("CurrentSize+remaining<size");
					LOG.debug("Size:"+size+"CurrentSize:"+currentSize+" Remaining:"+message.remaining());
					currentSize = currentSize + message.remaining();
					buffer.put(message);
					
					
				} else {
					LOG.debug("CurrentSize+remaining>size");
					LOG.debug("Size:"+size+"CurrentSize:"+currentSize+" Remaining:"+message.remaining()+" Message-Position:"+message.position());
					buffer.put(message.array(), message.position(), size - currentSize);
					message.position(message.position() + ( size - currentSize) );
					
					// 2. das fertige Objekt erstellen
					buffer.flip();
					LOG.debug("Creating object. Size:" + size + " buffer.remaining():" +buffer.remaining());
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
