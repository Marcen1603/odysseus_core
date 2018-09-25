package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class SimpleByteBufferHandler<T extends IStreamObject<? extends IMetaAttribute>>
		extends AbstractByteBufferHandler<T> {

	final static String NAME = "SimpleByteBuffer";
	final static Logger LOG = LoggerFactory.getLogger(SimpleByteBufferHandler.class);

	protected ByteBufferHandler<T> objectHandler;

	private static String bufferSizeOption = "buffersize";
	private int bufferSize = 1024;

	public SimpleByteBufferHandler() {
		super();
	}

	public SimpleByteBufferHandler(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		super(direction, access, dataHandler, options);
		objectHandler = new ByteBufferHandler<T>(dataHandler);
		if(options.containsKey(bufferSizeOption)) {
			this.bufferSize = options.getInt(bufferSizeOption, this.bufferSize);
		}
		// Set the byte order
		objectHandler.getByteBuffer().order(this.byteOrder);
	}

	@Override
	public void write(T object) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(this.bufferSize);
		buffer.order(this.byteOrder);
		getDataHandler().writeData(buffer, object);
		// ByteBufferUtil.toBuffer(buffer, (IStreamObject)object,
		// getDataHandler(), exportMetadata);
		buffer.flip();

		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes];
		// buffer.array() returns the complete array (1024 bytes) and
		// did not apply the "real" size of the object
		buffer.get(rawBytes, 0, messageSizeBytes);
		getTransportHandler().send(rawBytes);
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		return new SimpleByteBufferHandler<>(direction, access, options, dataHandler);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		// TODO: check if callerId is relevant
		objectHandler.clear();
		try {
			objectHandler.put(message);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		T object = null;
		try {
			object = objectHandler.create();
		} catch (ClassNotFoundException | BufferUnderflowException | IOException e) {
			LOG.error(e.getMessage(), e);
		}
		if (object != null) {
			getTransfer().transfer(object);
		} else {
			LOG.error("Empty object");
		}
	}

	@Override
	public boolean hasNext() throws IOException {
		if (getTransportHandler().getInputStream() != null)
			return getTransportHandler().getInputStream().available() > 0;
		else
			return false;
	}

	@Override
	public T getNext() throws IOException {
		InputStream input = getTransportHandler().getInputStream();
		int size = Math.min(input.available(), objectHandler.getByteBuffer().limit());
		input.read(objectHandler.getByteBuffer().array(), 0, size);

		objectHandler.getByteBuffer().position(size);

		try {
			return objectHandler.create();
		} catch (ClassNotFoundException | BufferUnderflowException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

}
