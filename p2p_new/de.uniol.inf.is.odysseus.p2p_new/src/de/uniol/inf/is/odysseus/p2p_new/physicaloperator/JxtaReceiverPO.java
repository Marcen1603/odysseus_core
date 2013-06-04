package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;

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
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.datahandler.NullAwareTupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi.JxtaBiDiClientConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.direct.SocketClientConnection;

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

	private final PipeID pipeID;
	private final PipeAdvertisement pipeAdvertisement; 
	
	private NullAwareTupleDataHandler dataHandler;
	private IJxtaConnection ctrlConnection;
	private SocketClientConnection dataConnection;

	private final List<T> bufferedElements = Lists.newArrayList();

	public JxtaReceiverPO(JxtaReceiverAO ao) {
		SDFSchema schema = new SDFSchema(ao.getName() != null ? getName() : "", ao.getSchema());
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
	}

	@Override
	public AbstractSource<T> clone() {
		return new JxtaReceiverPO<T>(this);
	}

	@Override
	public boolean hasNext() {
		synchronized (bufferedElements) {
			return !bufferedElements.isEmpty();
		}
	}

	@Override
	public void transferNext() {
		T ele = null;
		synchronized (bufferedElements) {
			ele = bufferedElements.remove(0);
		}

		if (ele != null) {
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
		
		JxtaReceiverPO<?> otherReceiver = (JxtaReceiverPO<?>)ipo;
		return otherReceiver.pipeID.equals(pipeID);
	}

	@Override
	public void onDisconnect(IJxtaConnection sender) {
		if( sender instanceof JxtaBiDiClientConnection ) {
			LOG.debug("{} : JxtaConnection is lost", getName());
			stopDirectConnection();
		} else if( sender instanceof SocketClientConnection ) {
			LOG.debug("{} : Direct connection is lost", getName());
			dataConnection.removeListener(this);
			dataConnection = null;
		}
	}

	@Override
	public void onConnect(IJxtaConnection sender) {
		if( LOG.isDebugEnabled() ) {
			if( sender instanceof JxtaBiDiClientConnection ) {
				LOG.debug("{} : Connectio with JxtaSender established", getName());
			} else if( sender instanceof SocketClientConnection ) {
				LOG.debug("{} : Direct connection established", getName());
			}
		}
	}

	@Override
	public void onReceiveData(IJxtaConnection sender, byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		if( sender instanceof JxtaBiDiClientConnection ) {
			processData(bb);
		} else if( sender instanceof SocketClientConnection ) {
			processData(bb); // TODO: separate
		}
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
						messageBuffer.put(message.array(), message.position(), message.remaining());
						message.position(message.position() + message.remaining());

						if (currentTypeByte == JxtaPOUtil.DATA_BYTE) {
							messageBuffer.flip();

							T objToTransfer = JxtaPOUtil.createStreamObject(messageBuffer, dataHandler);
							if (objToTransfer != null) {
								synchronized (bufferedElements) {
									bufferedElements.add(objToTransfer);
								}
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
		int port = determinePort(message);
		LOG.debug("{} : Port is {}", getName(), port);
		PeerID peerID = determinePeerID(message);
		LOG.debug("{} : PeerID is {}", getName(), peerID.toString());
		if( !JxtaServicesProvider.getInstance().getEndpointService().isReachable(peerID, false)) {
			LOG.error("PeerID is not reachable for direct connection: {}", peerID);
			return false;
		}
		
		Optional<String> optAddress = P2PDictionary.getInstance().getRemotePeerAddress(peerID);
		if( !optAddress.isPresent() ) {
			LOG.error("Could not determine ip address of peer for direct connection");
			return false;
		}
		
		LOG.debug("{} : Address is {}", getName(), optAddress.get());
		try {
			LOG.debug("{} : Starting direct connection", getName());
			dataConnection = new SocketClientConnection(optAddress.get().substring(0, optAddress.get().indexOf(":")), port, pipeAdvertisement);
			dataConnection.addListener(this);
			dataConnection.connect();

			return true;
		} catch (IOException e) {
			LOG.error("Could not establish a direct connection to {}", optAddress.get());
			return false;
		}
	}

	private void stopDirectConnection() {
		LOG.debug("{} : Stopping direct connection", getName());
		dataConnection.removeListener(this);
		dataConnection.disconnect();
		dataConnection = null;
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

}
