package de.uniol.inf.is.odysseus.p2p_new.handler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SizeByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNewPlugIn;

public class JxtaProtocolHandler<T extends IStreamObject<?>> extends AbstractByteBufferHandler<T> {

	private static final Logger LOG = LoggerFactory.getLogger(SizeByteBufferHandler.class);
	private static final String HANDLER_NAME = "JxtaSizeByteBuffer";
	private static final byte NONE_BYTE = 0;
	private static final byte PUNCTUATION_BYTE = 1;
	private static final byte DATA_BYTE = 2;

	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private byte currentTypeByte = NONE_BYTE;
	private ByteBuffer messageBuffer = ByteBuffer.allocate(P2PNewPlugIn.TRANSPORT_BUFFER_SIZE);

	private JxtaByteBufferHandler<T> jxtaBufferHandler;

	public JxtaProtocolHandler() {
		super();
	}

	public JxtaProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<T> dataHandler) {
		super(direction, access, dataHandler);
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
		ByteBuffer buffer = ByteBuffer.allocate(P2PNewPlugIn.TRANSPORT_BUFFER_SIZE);
		getDataHandler().writeData(buffer, object);
		if (object.getMetadata() != null) {
			byte[] metadataBytes = ObjectByteConverter.objectToBytes(object.getMetadata());
			buffer.put(metadataBytes);
		}
		buffer.flip();

		write(buffer, DATA_BYTE);
	}

	@Override
	public void writePunctuation(IPunctuation punctuation) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(P2PNewPlugIn.TRANSPORT_BUFFER_SIZE);
		buffer.put(ObjectByteConverter.objectToBytes(punctuation));
		buffer.flip();

		write(buffer, PUNCTUATION_BYTE);
	}

	private void write(ByteBuffer buffer, byte type) throws IOException {
		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes + 5];

		// "Header"
		rawBytes[0] = type;
		insertInt(rawBytes, 1, messageSizeBytes);

		// buffer.array() returns the complete array (1024 bytes) and
		// did not apply the "real" size of the object
		buffer.get(rawBytes, 5, messageSizeBytes);

		getTransportHandler().send(rawBytes);
	}

	@Override
	public void process(ByteBuffer message) {
		try {
			while (message.remaining() > 0) {
				if (currentTypeByte == NONE_BYTE) {
					currentTypeByte = message.get();
				}

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
						currentSize = currentSize + message.remaining();
						messageBuffer.put(message.array(), message.position(), message.remaining());
						message.position(message.position() + message.remaining());
					} else {
						messageBuffer.put(message.array(), message.position(), message.remaining());
						message.position(message.position() + message.remaining());
						
						if( currentTypeByte == DATA_BYTE) {
							messageBuffer.flip();
							jxtaBufferHandler.put(messageBuffer);
							
							T objToTransfer = tryCreate(jxtaBufferHandler);
							if( objToTransfer != null ) {
								getTransfer().transfer(objToTransfer);
							}
							
						} else if( currentTypeByte == PUNCTUATION_BYTE ) {
							messageBuffer.flip();
							transferPunctuation(messageBuffer);
						}
						
						size = -1;
						sizeBuffer.clear();
						messageBuffer.clear();
						currentSize = 0;
						currentTypeByte = NONE_BYTE;
					}
				}

			}
		} catch (Throwable e) {
			LOG.error("Could not process message", e);
			
			size = -1;
			sizeBuffer.clear();
			messageBuffer.clear();
			currentSize = 0;
			currentTypeByte = NONE_BYTE;
		} 
	}

	private static <T extends IStreamObject<?>> T tryCreate(JxtaByteBufferHandler<T> handler) {
		try {
			return handler.create();
		} catch( Throwable t ) {
			LOG.error("Could not create object to transfer", t);
			return null;
		}
	}

	private void transferPunctuation(ByteBuffer messageBuffer) {
		byte[] rawData = new byte[messageBuffer.remaining()];
		messageBuffer.get(rawData, 0, rawData.length);
		
		IPunctuation punctuation = (IPunctuation)ObjectByteConverter.bytesToObject(rawData);
		if( punctuation != null ) {
			getTransfer().sendPunctuation(punctuation);
		}
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access, Map<String, String> options, IDataHandler<T> dataHandler) {
		JxtaProtocolHandler<T> instance = new JxtaProtocolHandler<T>(direction, access,dataHandler);
		instance.setOptionsMap(options);
		instance.jxtaBufferHandler = new JxtaByteBufferHandler<T>(dataHandler);
		instance.setByteOrder(options.get("byteorder"));
		return instance;
	}

	@Override
	public String getName() {
		return HANDLER_NAME;
	}

	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if(!(o instanceof JxtaProtocolHandler)) {
			return false;
		}
		JxtaProtocolHandler<?> other = (JxtaProtocolHandler<?>)o;
		if(!this.byteOrder.toString().equals(other.getByteOrder().toString())) {
			return false;
		}
		return true;
	}
}
