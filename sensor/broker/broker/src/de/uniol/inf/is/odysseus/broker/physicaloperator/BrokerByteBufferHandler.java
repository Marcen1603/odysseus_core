package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.AbstractByteBufferHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IInputDataHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IObjectHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ITransferHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.SizeByteBufferHandler;

/**
* It works like {@link SizeByteBufferHandler}, but it differs between normal elements and punctuations. 
* The first four bytes (an integer) represents the type of the following bytes:
* - 0 = normal element 
* - 1 = punctuation 
* - 2 = done
* 
* A normal element consists of 4 bytes for an integer which indicates the size and multiple bytes for the raw data.
* The punctuation consists of 8 bytes for a long which represents the timestamp.
* Done means that a source has no more elements.
*/

public class BrokerByteBufferHandler<T> extends AbstractByteBufferHandler<ByteBuffer,T> {

	/** The size of the following element. */
	private int size = -1;
	
	/** The type of the following element. */
	private int type = 0;
	
	/** The size buffer. */
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	
	/** The type buffer. */
	private ByteBuffer typeBuffer = ByteBuffer.allocate(4);
	
	/** The time buffer. */
	private ByteBuffer timeBuffer = ByteBuffer.allocate(8);
	
	/** The current size. */
	private int currentSize = 0;
	
	
	@Override
	public void init() {
		sizeBuffer.clear();
		typeBuffer.clear();
		timeBuffer.clear();
		size = -1;		
	}

	@Override
	public void done() {
		sizeBuffer.clear();
		typeBuffer.clear();
		timeBuffer.clear();
		size = -1;
	}

	@Override
	public void process(ByteBuffer buffer, IObjectHandler<T> objectHandler,
			IAccessConnectionHandler<ByteBuffer> accessHandler, ITransferHandler transferHandler) {
		try {

			while (buffer.remaining() > 0) {
				if (size == -1) {
					// type
					while (typeBuffer.position() < 4 && buffer.remaining() > 0) {
						typeBuffer.put(buffer.get());
					}
					if (typeBuffer.position() == 4) {
						typeBuffer.flip();
						type = typeBuffer.getInt();
					}

					// size
					while (sizeBuffer.position() < 4 && buffer.remaining() > 0) {
						sizeBuffer.put(buffer.get());
					}
					if (sizeBuffer.position() == 4) {
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
					}
				}
				if (size != -1) {
					if (type == 0) {
						if (currentSize + buffer.remaining() <= size) {
							currentSize = currentSize + buffer.remaining();
							objectHandler.put(buffer);
						} else {
							objectHandler.put(buffer, size - currentSize);
							transferHandler.transfer();
						}
					} else {
						if (type == 1) {

							if (currentSize + buffer.remaining() <= size) {
								currentSize = currentSize + buffer.remaining();
								timeBuffer.put(buffer);
							} else {
								for (int i = 0; i < (size - currentSize); i++) {
									timeBuffer.put(buffer.get());
								}
								timeBuffer.flip();
								long time = timeBuffer.getLong();
								transferHandler.sendPunctuation(new PointInTime(time));
								size = -1;
								sizeBuffer.clear();
								typeBuffer.clear();
								timeBuffer.clear();
								currentSize = 0;
							}

						} else {
							done();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public IInputDataHandler<ByteBuffer, T> clone() {
		return new BrokerByteBufferHandler<T>();
	}

}
