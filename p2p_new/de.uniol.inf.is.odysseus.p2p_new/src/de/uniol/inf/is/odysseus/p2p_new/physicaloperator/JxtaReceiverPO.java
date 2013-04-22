package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.List;

import net.jxta.document.AdvertisementFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.util.AbstractJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.ClientJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

@SuppressWarnings("rawtypes")
public class JxtaReceiverPO<T extends IStreamObject> extends AbstractIterableSource<T> implements IJxtaConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaReceiverPO.class);
	private static final String PIPE_NAME = "Odysseus Pipe";
	private static final byte NONE_BYTE = 0;
	private static final byte PUNCTUATION_BYTE = 1;
	private static final byte DATA_BYTE = 2;
	private static final byte CONTROL_BYTE = 3;
	
	private static final byte OPEN_SUBBYTE = 0;
	private static final byte CLOSE_SUBBYTE = 1;
	
	private static final int BUFFER_SIZE_BYTES = 1024;
	
	private byte currentTypeByte = NONE_BYTE;
	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private ByteBuffer messageBuffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
	
	private TupleDataHandler dataHandler;
	private final PipeID pipeID;
	private AbstractJxtaConnection connection;
	
	private final List<T> bufferedElements = Lists.newArrayList();

	public JxtaReceiverPO(JxtaReceiverAO ao) {
		SDFSchema schema = new SDFSchema("", ao.getSchema());
		setOutputSchema(schema);
		dataHandler = (TupleDataHandler) new TupleDataHandler().createInstance(schema);

		pipeID = convertToPipeID(ao.getPipeID());
		final PipeAdvertisement pipeAdvertisement = createPipeAdvertisement(pipeID);

		connection = new ClientJxtaConnection(pipeAdvertisement);
		tryConnectAsync();
	}
	
	public JxtaReceiverPO(JxtaReceiverPO<T> po) {
		super(po);
		
		setOutputSchema(po.getOutputSchema().clone());
		
		this.pipeID = po.pipeID;
		this.connection = po.connection;
		this.dataHandler = po.dataHandler;
	}
	
	@Override
	public AbstractSource<T> clone() {
		return new JxtaReceiverPO<T>(this);
	}

	@Override
	public boolean hasNext() {
		synchronized( bufferedElements ) {
			return !bufferedElements.isEmpty();
		}
	}

	@Override
	public void transferNext() {
		T ele = null;
		synchronized( bufferedElements ) {
			ele = bufferedElements.remove(0);
		}
		
		if( ele != null ) {
			transfer(ele);
		}
	}

	@Override
	public boolean isDone() {
		return false;
	}
	
	@Override
	protected void process_close() {
		LOG.debug("Got open()");
		
		try {			
			byte[] rawData = new byte[2];
			rawData[0] = CONTROL_BYTE;
			rawData[1] = CLOSE_SUBBYTE;
			connection.send(rawData);
		} catch (IOException e) {
			LOG.error("Could not send close() to sender");
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		LOG.debug("Got open()");
		
		try {			
			byte[] rawData = new byte[2];
			rawData[0] = CONTROL_BYTE;
			rawData[1] = OPEN_SUBBYTE;
			connection.send(rawData);
		} catch (IOException e) {
			throw new OpenFailedException(e);
		}
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}

	@Override
	public void onConnect(AbstractJxtaConnection sender) {
	}

	@Override
	public void onDisconnect(AbstractJxtaConnection sender) {
	}

	@Override
	public void onReceiveData(AbstractJxtaConnection sender, byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		processData(bb);
	}
	
	private void processData(ByteBuffer message) {
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
							
							T objToTransfer = createStreamObject(messageBuffer);
							if( objToTransfer != null ) {
								synchronized( bufferedElements ) {
									bufferedElements.add(objToTransfer);
								}
							}
							
						} else if( currentTypeByte == PUNCTUATION_BYTE ) {
							messageBuffer.flip();
							IPunctuation punctuation = createPunctuation(messageBuffer);
							if( punctuation != null ) {
								sendPunctuation(punctuation);
							}
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
	
	@SuppressWarnings("unchecked")
	private T createStreamObject(ByteBuffer byteBuffer) throws BufferUnderflowException {
		T retval = null;
		try {
			retval = (T) dataHandler.readData(byteBuffer);

			if (byteBuffer.remaining() > 0) {
				byte[] metadataBytes = new byte[byteBuffer.remaining()];
				byteBuffer.get(metadataBytes);

				IMetaAttribute metadata = (IMetaAttribute) ObjectByteConverter.bytesToObject(metadataBytes);
				retval.setMetadata(metadata);
			}
		} finally {
			byteBuffer.clear();
		}
		
		return retval;
	}
	
	private IPunctuation createPunctuation(ByteBuffer messageBuffer) {
		byte[] rawData = new byte[messageBuffer.remaining()];
		messageBuffer.get(rawData, 0, rawData.length);
		
		return (IPunctuation)ObjectByteConverter.bytesToObject(rawData);
	}

	private void tryConnectAsync() {

		connection.addListener(this);
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					connection.connect();
				} catch (final IOException ex) {
					LOG.error("Could not connect", ex);
					connection = null;
				}
			}
		});
		t.setName("Connect thread for " + connection.getPipeAdvertisement().getPipeID());
		t.setDaemon(true);
		t.start();
	}

//	private void tryDisconnectAsync() {
//		connection.removeListener(this);
//		final Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				connection.disconnect();
//				connection = null;
//			}
//		});
//		t.setName("Discconnect thread for " + connection.getPipeAdvertisement().getPipeID());
//		t.setDaemon(true);
//		t.start();
//	}

	private static PipeID convertToPipeID(String text) {
		try {
			final URI id = new URI(text);
			return PipeID.create(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not transform to pipeid: {}", text, ex);
			return null;
		}
	}

	private static PipeAdvertisement createPipeAdvertisement(PipeID pipeID) {
		final PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setName(PIPE_NAME);
		advertisement.setPipeID(pipeID);
		advertisement.setType(PipeService.UnicastType);
		LOG.info("Pipe Advertisement with id = {}", pipeID);
		return advertisement;
	}
}
