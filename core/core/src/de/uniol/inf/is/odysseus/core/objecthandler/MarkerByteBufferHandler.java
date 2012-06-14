package de.uniol.inf.is.odysseus.core.objecthandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IInputDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;

public class MarkerByteBufferHandler<T> extends AbstractByteBufferHandler<ByteBuffer,T> {

	private byte start;
	private byte end;
	
	public MarkerByteBufferHandler() {
	}

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
	
	
	@Override
	public IInputDataHandler<ByteBuffer, T> getInstance(
			Map<String, String> option) {
		MarkerByteBufferHandler<T> ret = new MarkerByteBufferHandler<T>(Byte.parseByte(option.get("start")), Byte.parseByte(option.get("end")));
		ret.setByteOrder(option.get("byteorder"));
		return ret;
	}

	@Override
	public String getName() {
		return "MarkerByteBufferHandler";
	}
}
