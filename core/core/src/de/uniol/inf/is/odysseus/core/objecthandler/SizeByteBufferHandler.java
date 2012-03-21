package de.uniol.inf.is.odysseus.core.objecthandler;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IInputDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;

public class SizeByteBufferHandler<T> extends AbstractByteBufferHandler<ByteBuffer,T> {

	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;

	public SizeByteBufferHandler(){
		
	}
	
	public SizeByteBufferHandler(SizeByteBufferHandler<T> sizeByteBufferHandler) {
		this.size = sizeByteBufferHandler.size;
		this.currentSize = sizeByteBufferHandler.currentSize;
	}

	@Override
	public void init() {
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
	}

	@Override
	public void done() {
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
	}

	@Override
	public void process(ByteBuffer buffer, IObjectHandler<T> objectHandler,
			IAccessConnectionHandler<ByteBuffer> accessHandler, ITransferHandler<T> transferHandler) throws ClassNotFoundException {
		try {
			while (buffer.remaining() > 0) {

				// size ist dann ungleich -1 wenn die vollständige
				// Größeninformation übertragen wird
				// Ansonsten schon mal soweit einlesen
				if (size == -1) {
					while (sizeBuffer.position() < 4 && buffer.remaining() > 0) {
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
						transferHandler.transfer(objectHandler.create());
						size = -1;
						sizeBuffer.clear();
						currentSize = 0;
						// Dann in der While-Schleife weiterverarbeiten
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			accessHandler.reconnect();
		}
	}
	
	@Override
	public IInputDataHandler<ByteBuffer,T> clone() {
		return new SizeByteBufferHandler<T>(this);
	}

}
