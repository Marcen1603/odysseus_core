package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class SimpleByteBufferHandler<T> extends AbstractByteBufferHandler<T> {

	final static String NAME = "SimpleByteBuffer";
	final static Logger LOG = LoggerFactory.getLogger(SimpleByteBufferHandler.class);
	
    protected ByteBufferHandler<T> objectHandler;
	
	public SimpleByteBufferHandler() {
		super();
	}

	public SimpleByteBufferHandler(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		super(direction, access);
		setDataHandler(dataHandler);
		setTransfer(transfer);
		setOptionsMap(options);
		objectHandler = new ByteBufferHandler<T>(dataHandler);
        setByteOrder(options.get("byteorder"));
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
	}

	@Override
	public void close() throws IOException {
		getTransportHandler().close();
	}

	@Override
	public void write(T object) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		getDataHandler().writeData(buffer, object);
		buffer.flip();

		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes];
		buffer.get(rawBytes, 0, messageSizeBytes);
		getTransportHandler().send(rawBytes);
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		return new SimpleByteBufferHandler<>(direction, access, options,
				dataHandler, transfer);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void onConnect(ITransportHandler caller) {
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
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
	public void process(String[] message) {
		throw new RuntimeException("Not implemented!");
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

}
