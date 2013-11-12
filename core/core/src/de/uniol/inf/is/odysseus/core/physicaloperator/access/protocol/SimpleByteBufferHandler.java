package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class SimpleByteBufferHandler<T> extends AbstractByteBufferHandler<T> {

	final static String NAME = "SimpleByteBuffer";
	final static Logger LOG = LoggerFactory.getLogger(SimpleByteBufferHandler.class);
	
    protected ByteBufferHandler<T> objectHandler;
	
	public SimpleByteBufferHandler() {
		super();
	}

	public SimpleByteBufferHandler(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler) {
		super(direction, access, dataHandler);
		setOptionsMap(options);
		objectHandler = new ByteBufferHandler<T>(dataHandler);
        setByteOrder(options.get("byteorder"));
	}

	@Override
	public void write(T object) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		getDataHandler().writeData(buffer, object);
		buffer.flip();

		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes];
		// buffer.array() returns the complete array (1024 bytes) and
		// did not apply the "real" size of the object
		buffer.get(rawBytes, 0, messageSizeBytes);
		getTransportHandler().send(rawBytes);		
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler) {
		return new SimpleByteBufferHandler<>(direction, access, options,
				dataHandler);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(ByteBuffer message) {
		objectHandler.clear();
		try {
			objectHandler.put(message);
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);			
		}
		T object = null;
		try {
			object = objectHandler.create();
		} catch (ClassNotFoundException | BufferUnderflowException
				| IOException e) {
			LOG.error(e.getMessage(),e);
		}
		if (object != null){
			getTransfer().transfer(object);
		}else{
			LOG.error ("Empty object");
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

}
