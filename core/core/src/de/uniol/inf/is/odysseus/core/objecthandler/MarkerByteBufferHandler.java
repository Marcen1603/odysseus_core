package de.uniol.inf.is.odysseus.core.objecthandler;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IInputDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;

public class MarkerByteBufferHandler<T> extends AbstractByteBufferHandler<ByteBuffer,T> {

	final private byte start;
	final private byte end;

	public MarkerByteBufferHandler(byte start, byte end){
		this.start = start;
		this.end = end;
	}
	
	public MarkerByteBufferHandler(
			MarkerByteBufferHandler<T> markerByteBufferHandler) {
		super();
		this.start = markerByteBufferHandler.start;
		this.end = markerByteBufferHandler.end;
	}

	@Override
	public void init() {
	}

	@Override
	public void done() {
	}

	@Override
	public void process(ByteBuffer buffer, IObjectHandler<T> objectHandler,
			IAccessConnectionHandler<ByteBuffer> accessHandler, ITransferHandler<T> transferHandler) throws ClassNotFoundException {
		try {
			int pos = 0;
			while (buffer.remaining() > 0) {
				byte value = buffer.get();
				if (value == end) {
					int endPosition = buffer.position() - 1;
					buffer.position(pos);
					objectHandler.put(buffer, endPosition - pos);
					buffer.position(endPosition + 1);
					pos = buffer.position();
					transferHandler.transfer(objectHandler.create());
				}
				if (value == start) {
					objectHandler.clear();
					pos = buffer.position();
				}
			}
			if (pos >= 0) {
				buffer.position(pos);
				objectHandler.put(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
			accessHandler.reconnect();
		}

	}
	
	@Override
	public IInputDataHandler<ByteBuffer,T> clone() {
		return new MarkerByteBufferHandler<T>(this);
	}

}
