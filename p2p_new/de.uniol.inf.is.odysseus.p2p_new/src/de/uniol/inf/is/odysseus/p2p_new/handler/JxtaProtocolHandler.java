package de.uniol.inf.is.odysseus.p2p_new.handler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SizeByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

public class JxtaProtocolHandler<T extends IStreamObject<?>> extends AbstractByteBufferHandler<T> {
	
	private static final Logger LOG = LoggerFactory.getLogger(SizeByteBufferHandler.class);
	
	private static final String HANDLER_NAME = "JxtaSizeByteBuffer";
	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private ByteBufferHandler<T> objectHandler;

	public JxtaProtocolHandler() {
		super();
	}

	public JxtaProtocolHandler(ITransportDirection direction, IAccessPattern access) {
		super(direction, access);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
		getTransportHandler().open();
	}

	@Override
	public void onConnect(ITransportHandler caller) {

	}

	@Override
	public void close() throws IOException {
		getTransportHandler().close();
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {

	}

	@Override
	public void write(T object) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		getDataHandler().writeData(buffer, object);
		if( object.getMetadata() != null ) {
			byte[] metadataBytes = ObjectByteConverter.objectToBytes(object.getMetadata());
			buffer.put(metadataBytes);
		}
		buffer.flip();

		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes + 4];
		insertInt(rawBytes, 0, messageSizeBytes);
		// buffer.array() returns the complete array (1024 bytes) and
		// did not apply the "real" size of the object
		buffer.get(rawBytes, 4, messageSizeBytes);
		getTransportHandler().send(rawBytes);
	}



	@Override
	public void process(ByteBuffer message) {
		try {
			while (message.remaining() > 0) {

				// size ist dann ungleich -1 wenn die vollst�ndige
				// Gr��eninformation �bertragen wird
				// Ansonsten schon mal soweit einlesen
				if (size == -1) {
					while (sizeBuffer.position() < 4 && message.remaining() > 0) {
						sizeBuffer.put(message.get());
					}
					// Wenn alles �bertragen
					if (sizeBuffer.position() == 4) {
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
					}
				}
				// Es kann auch direkt nach der size noch was im Puffer
				// sein!
				// Und Size kann gesetzt worden sein
				if (size != -1) {
					// Ist das was dazukommt kleiner als die finale Gr��e?
					if (currentSize + message.remaining() < size) {
						currentSize = currentSize + message.remaining();
						objectHandler.put(message);
					} else {
						// Splitten (wir sind mitten in einem Objekt
						// 1. alles bis zur Grenze dem Handler �bergeben
						// logger.debug(" "+(size-currentSize));
						objectHandler.put(message, size - currentSize);
						// 2. das fertige Objekt weiterleiten
						getTransfer().transfer(objectHandler.create());
						size = -1;
						sizeBuffer.clear();
						currentSize = 0;
						// Dann in der While-Schleife weiterverarbeiten
					}
				}
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (BufferUnderflowException e) {
			LOG.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access, Map<String, String> options, IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		JxtaProtocolHandler<T> instance = new JxtaProtocolHandler<T>(direction, access);
		instance.setDataHandler(dataHandler);
		instance.setTransfer(transfer);
		instance.objectHandler = new JxtaByteBufferHandler<T>(dataHandler);
		instance.setByteOrder(options.get("byteorder"));
		return instance;
	}

	@Override
	public String getName() {
		return HANDLER_NAME;
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}

	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}
}
