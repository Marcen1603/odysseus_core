package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.datahandler.NullAwareTupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi.JxtaBiDiClientConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.direct.SocketClientConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.udp.UDPConnection;

@SuppressWarnings("rawtypes")
public class JxtaReceiverPO<T extends IStreamObject> extends AbstractSource<T> implements IJxtaConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaReceiverPO.class);

	private static final boolean FORCE_JXTA_DATA_TRANSMISSION = determineForceJxtaSetting();
	private static final String FORCE_JXTA_TRANSMISSION_SYS_PROPERTY = "peer.forcejxta";
	private static final String PIPE_NAME = "Odysseus Pipe";
	private static final int BUFFER_SIZE_BYTES = 4096;

	private byte currentTypeByte = JxtaPOUtil.NONE_BYTE;
	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private ByteBuffer messageBuffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);

	private final PipeID pipeID;
	private final PipeAdvertisement pipeAdvertisement; 
	
	private NullAwareTupleDataHandler dataHandler;
	private IJxtaConnection ctrlConnection;
	private IJxtaConnection dataConnection;
	
	private long receivedBytes;
	private double receivedBytesPerSecond;
	private long receivedBytesPerSecondCounter;
	private long receivedBytesPerSecondTimestamp;
	private JxtaConnectionType connectionType = JxtaConnectionType.NONE;
	private String connectedPeerName = "<not set>";
	
	public JxtaReceiverPO(JxtaReceiverAO ao) {
		SDFSchema schema = ao.getOutputSchema().clone();
		setOutputSchema(schema);
		dataHandler = (NullAwareTupleDataHandler) new NullAwareTupleDataHandler().createInstance(schema);

		pipeID = convertToPipeID(ao.getPipeID());
		pipeAdvertisement = createPipeAdvertisement(pipeID);

		ctrlConnection = new JxtaBiDiClientConnection(pipeAdvertisement);
		ctrlConnection.addListener(this);
		LOG.debug("{} : JxtaBiDiConnection created, waiting for connection", getName());
		JxtaPOUtil.tryConnectAsync(ctrlConnection);
	}

	public JxtaReceiverPO(JxtaReceiverPO<T> po) {
		super(po);

		setOutputSchema(po.getOutputSchema().clone());

		this.pipeID = po.pipeID;
		this.pipeAdvertisement = po.pipeAdvertisement;
		this.ctrlConnection = po.ctrlConnection;
		this.dataHandler = po.dataHandler;
		this.connectionType = po.connectionType;
	}

	@Override
	public AbstractSource<T> clone() {
		return new JxtaReceiverPO<T>(this);
	}


	@Override
	protected void process_close() {
		LOG.debug("{} : Got close()", getName());

		try {
			ctrlConnection.send(JxtaPOUtil.generateControlPacket(JxtaPOUtil.CLOSE_SUBBYTE));
		} catch (IOException e) {
			LOG.error("Could not send close() to sender");
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		LOG.debug("{} : Got open()", getName());

		try {
			ctrlConnection.send(JxtaPOUtil.generateControlPacket(JxtaPOUtil.OPEN_SUBBYTE));
		} catch (IOException e) {
			throw new OpenFailedException(e);
		}
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if( !(ipo instanceof JxtaReceiverPO)) {
			return false;
		}
		if( ipo == this ) {
			return true;
		}
		
		JxtaReceiverPO<?> otherReceiver = (JxtaReceiverPO<?>)ipo;
		return otherReceiver.pipeID.equals(pipeID);
	}

	@Override
	public void onDisconnect(IJxtaConnection sender) {
		if (sender == ctrlConnection) {
			LOG.debug("{} : JxtaConnection (ctrl) is lost", getName());
			stopDirectConnection();
		} else if (sender == dataConnection) {
			LOG.debug("{} : Direct connection (data) is lost", getName());
			dataConnection.removeListener(this);
			dataConnection = null;
			connectionType = JxtaConnectionType.NONE;
			connectedPeerName = "";
		}
	}

	@Override
	public void onConnect(IJxtaConnection sender) {
		if (sender == ctrlConnection) {
			LOG.debug("{} : Connection with JxtaSender established", getName());

		} else if (sender == dataConnection) {
			LOG.debug("{} : Direct connection established", getName());
			if (sender instanceof UDPConnection) {
				connectionType = JxtaConnectionType.UDP;
			} else if (sender instanceof SocketClientConnection) {
				connectionType = JxtaConnectionType.TCP;
			} else {
				connectionType = JxtaConnectionType.JXTA;
			}
		}
	}

	@Override
	public void onReceiveData(IJxtaConnection sender, byte[] data) {
		if( isOpen() ) {
			receivedBytes += data.length;
			receivedBytesPerSecondCounter += data.length;
			final long currentTime = System.currentTimeMillis();
			final long timeDelta = currentTime - receivedBytesPerSecondTimestamp;
			if( timeDelta > 1000 ) {
				receivedBytesPerSecond = receivedBytesPerSecondCounter / (timeDelta / 1000.0);
				receivedBytesPerSecondCounter = 0;
				receivedBytesPerSecondTimestamp = currentTime;
			}
			
			ByteBuffer bb = ByteBuffer.wrap(data);
			processData(bb);
		}
	}
	
	public final long getReceivedByteCount() {
		return receivedBytes;
	}
	
	public final double getReceivedByteDataRate() {
		return receivedBytesPerSecond;
	}
	
	public final PipeAdvertisement getPipeAdvertisement() {
		return pipeAdvertisement;
	}
	
	public final JxtaConnectionType getConnectionType() {
		return connectionType;
	}
	
	public final String getConnectedPeerName() {
		return connectedPeerName;
	}

	private void processData(ByteBuffer message) {
		try {
			while (message.remaining() > 0) {

				if (currentTypeByte == JxtaPOUtil.NONE_BYTE) {
					currentTypeByte = message.get();
				}

				if (currentTypeByte == JxtaPOUtil.CONTROL_BYTE) {
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
						messageBuffer.put(message.array(), message.position(), size - currentSize);
						message.position(message.position() + (size - currentSize));

						if (currentTypeByte == JxtaPOUtil.DATA_BYTE) {
							messageBuffer.flip();

							T objToTransfer = JxtaPOUtil.createStreamObject(messageBuffer, dataHandler);
							if (objToTransfer != null) {
								transfer(objToTransfer);
							}

						} else if (currentTypeByte == JxtaPOUtil.PUNCTUATION_BYTE) {
							messageBuffer.flip();
							IPunctuation punctuation = JxtaPOUtil.createPunctuation(messageBuffer);
							if (punctuation != null) {
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
			} // while
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
		LOG.debug("{} : Got Control-Message", getName());
		byte subbyte = message.get();
		
		if (subbyte == JxtaPOUtil.DONE_SUBBYTE) {
			LOG.debug("{} : PropagateDone-Message", getName());
			tryPropagateDone();
			
		} else if (subbyte == JxtaPOUtil.CONNECTION_DATA_SUBBYTE) {
			if(!startDirectConnection(message)) {
				LOG.error("Direct connection NOT established");
				try {
					ctrlConnection.send(JxtaPOUtil.generateControlPacket(JxtaPOUtil.USE_JXTA_CONNECTION_SUBBYTE));
				} catch (IOException e) {
					LOG.error("Could not send message for starting data transmission with jxta-connection");
				}
			}
			
		} else {
			LOG.error("Unknown subbyte {}", subbyte);
		}
	}

	private boolean startDirectConnection(ByteBuffer message) {
		LOG.debug("{} : Got connection info for direct connection", getName());
		boolean useUDP = message.get() == 1 ? true : false;
		LOG.debug("{} : We should use UDP as direct connection", getName());
		int port = determinePort(message);
		LOG.debug("{} : Port is {}", getName(), port);
		PeerID peerID = determinePeerID(message);
		LOG.debug("{} : PeerID is {}", getName(), peerID.toString());
		connectedPeerName = determinePeerName(peerID);
		LOG.debug("{} : PeerName is {}", getName(), connectedPeerName);
		
		if( FORCE_JXTA_DATA_TRANSMISSION ) {
			return false;
		}
		
		if( !JxtaServicesProvider.getInstance().getEndpointService().isReachable(peerID, false)) {
			LOG.error("PeerID is not reachable for direct connection: {}", peerID);
			return false;
		}
		
		Optional<String> optAddress = P2PDictionary.getInstance().getRemotePeerAddress(peerID);
		if( !optAddress.isPresent() ) {
			LOG.error("Could not determine ip address of peer for direct connection");
			return false;
		}
		
		final String realAddress = optAddress.get().substring(0, optAddress.get().indexOf(":"));
		LOG.debug("{} : Address is {}", getName(), realAddress);
		try {
			LOG.debug("{} : Starting direct connection", getName());
			if( useUDP ) {
				dataConnection = new UDPConnection(realAddress, port);
				LOG.debug("{} : Using UDP", getName());
			} else {
				dataConnection = new SocketClientConnection(realAddress, port, pipeAdvertisement);
				LOG.debug("{} : Using TCP", getName());
			}
			
			dataConnection.addListener(this);
			dataConnection.connect();

			return true;
		} catch (IOException e) {
			LOG.error("Could not establish a direct connection to {}", optAddress.get());
			return false;
		}
	}

	private static String determinePeerName(PeerID peerID) {
		Optional<String> optPeerName = P2PDictionary.getInstance().getRemotePeerName(peerID);
		return optPeerName.isPresent() ? optPeerName.get() : "<unknown>";
	}

	private void stopDirectConnection() {
		LOG.debug("{} : Stopping direct connection", getName());
		dataConnection.removeListener(this);
		dataConnection.disconnect();
		dataConnection = null;
		
		connectionType = JxtaConnectionType.NONE;
	}

	private void tryPropagateDone() {
		try {
			propagateDone();
		} catch (Throwable t) {
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
	
	private static ID toID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
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

	private static PeerID determinePeerID(ByteBuffer message) {
		final int size = message.getInt();
		byte[] bytes = new byte[size];
		message.get(bytes);
		return (PeerID) toID(new String(bytes));
	}

	private static int determinePort(ByteBuffer message) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		for (int i = 0; i < 4; i++) {
			bb.put(message.get());
		}
		bb.flip();
		return bb.getInt();
	}

	private static boolean determineForceJxtaSetting() {
		try {
			return Boolean.valueOf(determineForceJxtaSettingString());
		} catch( Throwable t ) {
			LOG.error("Could not determine stting for forcing jxta data stream transmission connection", t);
			return false;
		}
	}

	private static String determineForceJxtaSettingString() {
		String peerGroupName = System.getProperty(FORCE_JXTA_TRANSMISSION_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerGroupName)) {
			return peerGroupName;
		}

		peerGroupName = OdysseusConfiguration.get(FORCE_JXTA_TRANSMISSION_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerGroupName)) {
			return peerGroupName;
		}

		return "false";
	}
}
