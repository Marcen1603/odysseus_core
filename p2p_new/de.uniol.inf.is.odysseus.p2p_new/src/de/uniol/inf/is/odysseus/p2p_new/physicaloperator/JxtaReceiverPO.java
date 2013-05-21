package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;

import net.jxta.document.AdvertisementFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.datahandler.NullAwareTupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.JxtaBiDiClientConnection;

@SuppressWarnings("rawtypes")
public class JxtaReceiverPO<T extends IStreamObject> extends AbstractIterableSource<T> implements IJxtaConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaReceiverPO.class);
	private static final String PIPE_NAME = "Odysseus Pipe";
	
	private static final int BUFFER_SIZE_BYTES = 1024;
	
	private byte currentTypeByte = JxtaPOUtil.NONE_BYTE;
	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private ByteBuffer messageBuffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
	private boolean isDone;
	
	private NullAwareTupleDataHandler dataHandler;
	private final PipeID pipeID;
	private IJxtaConnection connection;
	
	private final List<T> bufferedElements = Lists.newArrayList();

	public JxtaReceiverPO(JxtaReceiverAO ao) {
		SDFSchema schema = new SDFSchema(ao.getName() != null ? getName() : "", ao.getSchema());
		setOutputSchema(schema);
		dataHandler = (NullAwareTupleDataHandler) new NullAwareTupleDataHandler().createInstance(schema);

		pipeID = convertToPipeID(ao.getPipeID());
		final PipeAdvertisement pipeAdvertisement = createPipeAdvertisement(pipeID);

		connection = new JxtaBiDiClientConnection(pipeAdvertisement);
		connection.addListener(this);
		JxtaPOUtil.tryConnectAsync(connection);
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
		return isDone;
	}
	
	@Override
	protected void process_done() {
		isDone = true;
	}
	
	@Override
	protected void process_close() {
		LOG.debug("Got close()");
		
		try {			
			connection.send(JxtaPOUtil.generateControlPacket(JxtaPOUtil.CLOSE_SUBBYTE));
		} catch (IOException e) {
			LOG.error("Could not send close() to sender");
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		LOG.debug("Got open()");
		
		try {			
			connection.send(JxtaPOUtil.generateControlPacket(JxtaPOUtil.OPEN_SUBBYTE));
		} catch (IOException e) {
			throw new OpenFailedException(e);
		}
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}

	@Override
	public void onDisconnect(IJxtaConnection sender) {
		LOG.debug("Disconnect");
	}

	@Override
	public void onReceiveData(IJxtaConnection sender, byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		processData(bb);
	}
	
	private void processData(ByteBuffer message) {
		try {
			while (message.remaining() > 0) {
				if (currentTypeByte == JxtaPOUtil.NONE_BYTE) {
					currentTypeByte = message.get();
				}
				
				if( currentTypeByte == JxtaPOUtil.CONTROL_BYTE) {
					processCtrlPacket(message);
					
					// reset for next message
					currentTypeByte = JxtaPOUtil.NONE_BYTE;
					continue;
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
						
						if( currentTypeByte == JxtaPOUtil.DATA_BYTE) {
							messageBuffer.flip();
							
							T objToTransfer = JxtaPOUtil.createStreamObject(messageBuffer, dataHandler);
							if( objToTransfer != null ) {
								synchronized( bufferedElements ) {
									bufferedElements.add(objToTransfer);
								}
							}
							
						} else if( currentTypeByte == JxtaPOUtil.PUNCTUATION_BYTE ) {
							messageBuffer.flip();
							IPunctuation punctuation = JxtaPOUtil.createPunctuation(messageBuffer);
							if( punctuation != null ) {
								sendPunctuation(punctuation);
							}
						}
						
						size = -1;
						sizeBuffer.clear();
						messageBuffer.clear();
						currentSize = 0;
						currentTypeByte = JxtaPOUtil.NONE_BYTE;
					}
				}

			}
		} catch (Throwable e) {
			LOG.error("Could not process message", e);
			
			size = -1;
			sizeBuffer.clear();
			messageBuffer.clear();
			currentSize = 0;
			currentTypeByte = JxtaPOUtil.NONE_BYTE;
		} 
	}

	private void processCtrlPacket(ByteBuffer message) {
		byte subbyte = message.get();
		if( subbyte == JxtaPOUtil.DONE_SUBBYTE) {
			tryPropagateDone();
		} else {
			LOG.error("Unknown subbyte {}", subbyte);
		}
	}

	private void tryPropagateDone() {
		try {
			propagateDone();
		} catch( Throwable t ) {
			LOG.error("Exception during invoking done", t);
		}
	}

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
