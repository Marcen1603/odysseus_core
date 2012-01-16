package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;

public class SizeByteBufferReceiverPO<W> extends
		AbstractByteBufferReceiverPO<W> {

	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;

	public SizeByteBufferReceiverPO(IObjectHandler<W> objectHandler,
			IAccessConnection accessHandler) throws IOException {
		super(objectHandler, accessHandler);
	}

	public SizeByteBufferReceiverPO(
			SizeByteBufferReceiverPO<W> byteBufferReceiverPO) {
		super(byteBufferReceiverPO);
		size = byteBufferReceiverPO.size;
		currentSize = byteBufferReceiverPO.currentSize;

	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		sizeBuffer.clear();
		size = -1;
		super.process_open();
	}

	@Override
	protected void process_close() {
		sizeBuffer.clear();
		size = -1;
		super.process_close();
	}
	
	@Override
	public synchronized void process(ByteBuffer buffer) {
		if (opened) {
			try {
				while (buffer.remaining() > 0) {

					// size ist dann ungleich -1 wenn die vollständige
					// Größeninformation übertragen wird
					// Ansonsten schon mal soweit einlesen
					if (size == -1) {
						while (sizeBuffer.position() < 4
								&& buffer.remaining() > 0) {
							sizeBuffer.put(buffer.get());
						}
						// Wenn alles übertragen
						if (sizeBuffer.position() == 4) {
							sizeBuffer.flip();
							size = sizeBuffer.getInt();
						}
					}
					// Es kann auch direkt nach der size noch was im Puffer
					// sein!
					// Und Size kann gesetzt worden sein
					if (size != -1) {
						// Ist das was dazukommt kleiner als die finale Größe?
						if (currentSize + buffer.remaining() < size) {
							currentSize = currentSize + buffer.remaining();
							objectHandler.put(buffer);
						} else {
							// Splitten (wir sind mitten in einem Objekt
							// 1. alles bis zur Grenze dem Handler übergeben
							// logger.debug(" "+(size-currentSize));
							objectHandler.put(buffer, size - currentSize);
							// 2. das fertige Objekt weiterleiten
							transfer();
							size = -1;
							sizeBuffer.clear();
							currentSize = 0;
							// Dann in der While-Schleife weiterverarbeiten
						}
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				accessHandler.reconnect();
			}
		}
	}

	@Override
	public AbstractSource<W> clone() {
		return new SizeByteBufferReceiverPO<W>(this);
	}

}
