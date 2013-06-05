package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.datahandler.NullAwareTupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi.JxtaBiDiConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi.JxtaBiDiServerConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.direct.SingleSocketServerConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.direct.SocketConnection;

public class JxtaSenderPO<T extends IStreamObject<?>> extends AbstractSink<T> implements IJxtaConnectionListener, IJxtaServerConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaSenderPO.class);
	private static final int BUFFER_SIZE_BYTES = 1024;
	private static final String PIPE_NAME = "Odysseus Pipe";

	private final PipeID pipeID;
	private final Map<IJxtaConnection, IJxtaServerConnection> connectionsOpenCalled = Maps.newHashMap();
	private final Map<IJxtaServerConnection, IJxtaConnection> dataTransmissionConnectionMap = Maps.newHashMap();

	private IJxtaServerConnection connection;
	private NullAwareTupleDataHandler dataHandler;

	public JxtaSenderPO(JxtaSenderAO ao) {
		pipeID = convertToPipeID(ao.getPipeID());
		final PipeAdvertisement pipeAdvertisement = createPipeAdvertisement(pipeID);

		try {
			LOG.debug("{} : Starting JxtaServer Connection", getName());
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
		this.connectionsOpenCalled.putAll(po.connectionsOpenCalled);
		this.dataTransmissionConnectionMap.putAll(po.dataTransmissionConnectionMap);
	}

	@Override
	public AbstractSink<T> clone() {
		return new JxtaSenderPO<T>(this);
	}

	// called by jxta bidi server connection
	@Override
	public void connectionAdded(IJxtaServerConnection sender, IJxtaConnection addedConnection) {
		if (dataHandler == null) {
			dataHandler = (NullAwareTupleDataHandler) new NullAwareTupleDataHandler().createInstance(getOutputSchema());
			LOG.debug("{} : Data Handler created", getName());

		}
		
		if( connectionsOpenCalled.containsValue(sender)) {
			LOG.debug("{} : Got connection for data transmission of type {}", getName(), addedConnection.getClass().getSimpleName());
			dataTransmissionConnectionMap.put(sender, addedConnection);
		} else {
			LOG.debug("{} : Got connection for jxta communication", getName());
			addedConnection.addListener(this);
		}
	}

	// called by jxta bidi server connection
	@Override
	public void connectionRemoved(IJxtaServerConnection sender, IJxtaConnection removedConnection) {
		if( connectionsOpenCalled.containsValue(sender)) {
			LOG.debug("{} : Lost data transmission connection", getName());
			dataTransmissionConnectionMap.remove(sender);
			
		} else {
			LOG.debug("{} : Lost connection for jxta communication", getName());
			removedConnection.removeListener(this);
		}
	}

	// called by connections of jxta 
	@Override
	public void onDisconnect(IJxtaConnection sender) {
		if (sender instanceof JxtaBiDiConnection) {
			LOG.debug("{} : Jxta connection disconnected", getName());

			stopDirectConnectionServer(sender);
		} 
	}

	// called by connections of jxta
	@Override
	public void onConnect(IJxtaConnection sender) {
		if (LOG.isDebugEnabled()) {
			if (sender instanceof JxtaBiDiConnection) {
				LOG.debug("{} : Jxta Connection established", getName());
			} else if (sender instanceof SocketConnection) {
				LOG.debug("{} : Direct connection established", getName());
			}
		}
		// do nothing
	}

	// called by Jxta
	@Override
	public void onReceiveData(IJxtaConnection sender, byte[] data) {
		if (data[0] == JxtaPOUtil.CONTROL_BYTE) {
			LOG.debug("{} : Got Control packet", getName());

			if (data[1] == JxtaPOUtil.OPEN_SUBBYTE) {
				LOG.debug("{} : Received open()", getName());

				synchronized (connectionsOpenCalled) {
					if (!connectionsOpenCalled.containsKey(sender)) {
						try {
							IJxtaServerConnection directConnectionServer = startDirectConnectionServer(sender);
							connectionsOpenCalled.put(sender, directConnectionServer);

							if (connectionsOpenCalled.size() == 1) {
								final int queryID = determineQueryID(getOwner());
								LOG.debug("{} : Starting query {}", getName(), queryID);
								ServerExecutorService.get().startQuery(queryID, SessionManagementService.getActiveSession());
							}

						} catch (IOException ex) {
							LOG.error("Could not open direct connection", ex);
						}
					} else {
						LOG.error("Open received from an already open connection");
					}
				}

			} else if (data[1] == JxtaPOUtil.CLOSE_SUBBYTE) {
				LOG.debug("{} : Received close()", getName());

				synchronized (connectionsOpenCalled) {
					if (connectionsOpenCalled.containsKey(sender)) {

						stopDirectConnectionServer(sender);
						connectionsOpenCalled.remove(sender);

						if (connectionsOpenCalled.isEmpty()) {
							final int queryID = determineQueryID(getOwner());
							LOG.debug("{} : Stopping query {}", getName(), queryID);
							ServerExecutorService.get().stopQuery(queryID, SessionManagementService.getActiveSession());
						}
					} else {
						LOG.error("Got close event from connection which hasnt called open before");
					}
				}
			} else if( data[1] == JxtaPOUtil.USE_JXTA_CONNECTION_SUBBYTE ) {
				LOG.debug("{} : Received message for using jxta connection for data transmission");
				stopDirectConnectionServer(sender);
				dataTransmissionConnectionMap.put(connection, sender);
				
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
		if (!dataTransmissionConnectionMap.isEmpty()) {
			final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
			buffer.put(ObjectByteConverter.objectToBytes(punctuation));
			buffer.flip();

			write(buffer, JxtaPOUtil.PUNCTUATION_BYTE);
		}
	}

	@Override
	protected void process_next(T object, int port) {
		if (!dataTransmissionConnectionMap.isEmpty()) {
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
		synchronized (dataTransmissionConnectionMap) {
			for (IJxtaConnection conn : dataTransmissionConnectionMap.values()) {
				try {
					conn.send(generateControlPacket);
				} catch (IOException e) {
					LOG.error("Could not send done-control-packet", e);
				}
			}
		}
	}

	private IJxtaServerConnection startDirectConnectionServer(IJxtaConnection sender) throws IOException {
		LOG.debug("{} : Starting server for direct connection", getName());
		SingleSocketServerConnection directConnectionServer = new SingleSocketServerConnection();
		directConnectionServer.addListener(this);
		directConnectionServer.start();

		LOG.debug("{} : Send connection info", getName());
		LOG.debug("{} : Port is {}", getName(), directConnectionServer.getLocalPort());
		LOG.debug("{} : PeerID is {}", getName(), P2PDictionary.getInstance().getLocalPeerID());

		sender.send(JxtaPOUtil.generateSetAddressPacket(P2PDictionary.getInstance().getLocalPeerID(), directConnectionServer.getLocalPort(), false));

//		UDPServerConnection directConnectionServer = new UDPServerConnection();
//		directConnectionServer.addListener(this);
//		directConnectionServer.start();
//
//		LOG.debug("{} : Send connection info", getName());
//		LOG.debug("{} : Port is {}", getName(), directConnectionServer.getLocalPort());
//		LOG.debug("{} : PeerID is {}", getName(), P2PDictionary.getInstance().getLocalPeerID());
//
//		sender.send(JxtaPOUtil.generateSetAddressPacket(P2PDictionary.getInstance().getLocalPeerID(), directConnectionServer.getLocalPort(), true));

		return directConnectionServer;
	}

	private void stopDirectConnectionServer(IJxtaConnection sender) {
		LOG.debug("{} : Stopping server for direct connection", getName());
		
		IJxtaServerConnection directConnectionServer = connectionsOpenCalled.get(sender);
		if( directConnectionServer != null ) {
			directConnectionServer.stop();
			directConnectionServer.removeListener(this);
	
			IJxtaConnection directConnection = dataTransmissionConnectionMap.remove(directConnectionServer);
			if( directConnection != null ) {
				directConnection.disconnect();
				directConnection.removeListener(this);
			}
		}
	}

	private void write(ByteBuffer buffer, byte type) {
		final int messageSizeBytes = buffer.remaining();
		final byte[] rawBytes = new byte[messageSizeBytes + 5];

		// "Header"
		rawBytes[0] = type;
		JxtaPOUtil.insertInt(rawBytes, 1, messageSizeBytes);

		// buffer.array() returns the complete array (1024 bytes) and
		// did not apply the "real" size of the object
		buffer.get(rawBytes, 5, messageSizeBytes);

		synchronized (dataTransmissionConnectionMap) {
			for (IJxtaConnection conn : dataTransmissionConnectionMap.values()) {
				try {
					conn.send(rawBytes);
				} catch (final Throwable t) {
					LOG.error("Could not write", t);
					
					// TODO: connection entfernen
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
}
