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
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.JxtaBiDiServerConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

public class JxtaSenderPO<T extends IStreamObject<?>> extends AbstractSink<T> implements IJxtaConnectionListener, IJxtaServerConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaSenderPO.class);
	private static final int BUFFER_SIZE_BYTES = 1024;
	private static final String PIPE_NAME = "Odysseus Pipe";

	private final PipeID pipeID;
	private final List<IJxtaConnection> connectionsOpenCalled = Lists.newArrayList();
	private IJxtaServerConnection connection;
	private NullAwareTupleDataHandler dataHandler;
	
	public JxtaSenderPO(JxtaSenderAO ao) {
		pipeID = convertToPipeID(ao.getPipeID());
		final PipeAdvertisement pipeAdvertisement = createPipeAdvertisement(pipeID);

		try {
			connection = new JxtaBiDiServerConnection(pipeAdvertisement);
			connection.addListener(this);
			connection.start();
		} catch (IOException e) {
			LOG.error("Could not create connection", e);
			connection = null;
		}
	}

	public JxtaSenderPO(JxtaSenderPO<T> po) {
		super(po);
		
		this.pipeID = po.pipeID;
		this.connection = po.connection;
		this.dataHandler = po.dataHandler;
		this.connectionsOpenCalled.addAll(po.connectionsOpenCalled);
	}

	@Override
	public AbstractSink<T> clone() {
		return new JxtaSenderPO<T>(this);
	}

	@Override
	public void connectionAdded(IJxtaServerConnection sender, IJxtaConnection addedConnection) {
		if( dataHandler == null ) {
			dataHandler = (NullAwareTupleDataHandler) new NullAwareTupleDataHandler().createInstance(getOutputSchema());
		}
		
		addedConnection.addListener(this);
	}

	@Override
	public void connectionRemoved(IJxtaServerConnection sender, IJxtaConnection removedConnection) {
		removedConnection.removeListener(this);
		
	}

	@Override
	public void onDisconnect(IJxtaConnection sender) {
		LOG.debug("Disconnected");
	}
	

	@Override
	public void onConnect(IJxtaConnection sender) {
		LOG.debug("Connected");
	}
	// called by Jxta
	@Override
	public void onReceiveData(IJxtaConnection sender, byte[] data) {
		if (data[0] == JxtaPOUtil.CONTROL_BYTE) {
			if (data[1] == JxtaPOUtil.OPEN_SUBBYTE) {
				LOG.debug("Received open()");
				
				synchronized( connectionsOpenCalled ) {
					connectionsOpenCalled.add(sender);
	
					if( connectionsOpenCalled.size() == 1 ) {
						final int queryID = determineQueryID(getOwner());
						ServerExecutorService.get().startQuery(queryID, SessionManagementService.getActiveSession());
					}
				}

			} else if (data[1] == JxtaPOUtil.CLOSE_SUBBYTE) {
				LOG.debug("Received close()");

				synchronized( connectionsOpenCalled ) {
					connectionsOpenCalled.remove(sender);
					if( connectionsOpenCalled.isEmpty() ) {
						final int queryID = determineQueryID(getOwner());
						ServerExecutorService.get().stopQuery(queryID, SessionManagementService.getActiveSession());
					}
				}
				
			} else {
				LOG.error("Got unknown control subbyte {}", data[1]);
			}
		}
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (ipo == this) {
			return true;
		}

		if (!(ipo instanceof JxtaSenderPO)) {
			return false;
		}

		JxtaSenderPO<?> po = (JxtaSenderPO<?>) ipo;
		return po.pipeID.equals(pipeID);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if( !connectionsOpenCalled.isEmpty() ) {
			final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
			buffer.put(ObjectByteConverter.objectToBytes(punctuation));
			buffer.flip();
	
			write(buffer, JxtaPOUtil.PUNCTUATION_BYTE);
		}
	}

	@Override
	protected void process_next(T object, int port) {
		if( !connectionsOpenCalled.isEmpty() ) {
			final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
			dataHandler.writeData(buffer, object);
			if (object.getMetadata() != null) {
				final byte[] metadataBytes = ObjectByteConverter.objectToBytes(object.getMetadata());
				buffer.put(metadataBytes);
			}
			buffer.flip();
	
			write(buffer, JxtaPOUtil.DATA_BYTE);
		}
	}
	
	@Override
	protected void process_done(int port) {
		byte[] generateControlPacket = JxtaPOUtil.generateControlPacket(JxtaPOUtil.DONE_SUBBYTE);
		synchronized( connectionsOpenCalled ) {
			for( IJxtaConnection conn : connectionsOpenCalled ) {
				try {
					conn.send(generateControlPacket);
				} catch (IOException e) {
					LOG.error("Could not send done-control-packet", e);
				}
			}
		}
	}

	private void write(ByteBuffer buffer, byte type) {
		final int messageSizeBytes = buffer.remaining();
		final byte[] rawBytes = new byte[messageSizeBytes + 5];

		// "Header"
		rawBytes[0] = type;
		insertInt(rawBytes, 1, messageSizeBytes);

		// buffer.array() returns the complete array (1024 bytes) and
		// did not apply the "real" size of the object
		buffer.get(rawBytes, 5, messageSizeBytes);

		synchronized (connectionsOpenCalled) {
			for (IJxtaConnection conn : connectionsOpenCalled) {
				try {
					conn.send(rawBytes);
				} catch (final Throwable t) {
					LOG.error("Could not write", t);
				}
			}
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

	private static int determineQueryID(List<IOperatorOwner> owner) {
		return owner.get(0).getID();
	}

	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}
}
