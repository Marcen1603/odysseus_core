package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

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

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.service.ExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.util.AbstractJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;
import de.uniol.inf.is.odysseus.p2p_new.util.ServerJxtaConnection;

public class JxtaSenderPO<T extends IStreamObject<?>> extends AbstractSink<T> implements IJxtaConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaSenderPO.class);
	private static final int BUFFER_SIZE_BYTES = 1024;
	private static final String PIPE_NAME = "Odysseus Pipe";

	private final PipeID pipeID;
	private AbstractJxtaConnection connection;
	private TupleDataHandler dataHandler;

	public JxtaSenderPO(JxtaSenderAO ao) {
		pipeID = convertToPipeID(ao.getPipeID());
		final PipeAdvertisement pipeAdvertisement = createPipeAdvertisement(pipeID);

		connection = new ServerJxtaConnection(pipeAdvertisement);
		connection.addListener(this);
		JxtaPOUtil.tryConnectAsync(connection);
	}

	public JxtaSenderPO(JxtaSenderPO<T> po) {
		super(po);

		this.pipeID = po.pipeID;
		this.connection = po.connection;
		this.dataHandler = po.dataHandler;
	}

	@Override
	public AbstractSink<T> clone() {
		return new JxtaSenderPO<T>(this);
	}

	// called by Jxta
	@Override
	public void onConnect(AbstractJxtaConnection sender) {
		LOG.debug("Connected");

		dataHandler = (TupleDataHandler) new TupleDataHandler().createInstance(getOutputSchema());
	}

	// called by Jxta
	@Override
	public void onDisconnect(AbstractJxtaConnection sender) {
		LOG.debug("Disconnnect");
	}

	// called by Jxta
	@Override
	public void onReceiveData(AbstractJxtaConnection sender, byte[] data) {
		if (data[0] == JxtaPOUtil.CONTROL_BYTE) {
			if (data[1] == JxtaPOUtil.OPEN_SUBBYTE) {
				LOG.debug("Received open()");

				final int queryID = determineQueryID(getOwner());
				ExecutorService.getServerExecutor().startQuery(queryID, SessionManagementService.getActiveSession());

			} else if (data[1] == JxtaPOUtil.CLOSE_SUBBYTE) {
				LOG.debug("Received close()");

				final int queryID = determineQueryID(getOwner());
				ExecutorService.getServerExecutor().stopQuery(queryID, SessionManagementService.getActiveSession());
			} else {
				LOG.error("Got unknown control subbyte {}", data[1]);
			}
		}
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		LOG.debug("Sending punctuation {}", punctuation);

		final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
		buffer.put(ObjectByteConverter.objectToBytes(punctuation));
		buffer.flip();

		write(buffer, JxtaPOUtil.PUNCTUATION_BYTE);
	}

	@Override
	protected void process_next(T object, int port) {
		LOG.debug("Sending tupe {}", object);

		final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
		dataHandler.writeData(buffer, object);
		if (object.getMetadata() != null) {
			final byte[] metadataBytes = ObjectByteConverter.objectToBytes(object.getMetadata());
			buffer.put(metadataBytes);
		}
		buffer.flip();

		write(buffer, JxtaPOUtil.DATA_BYTE);
	}

	private void write(ByteBuffer buffer, byte type) {
		try {
			final int messageSizeBytes = buffer.remaining();
			final byte[] rawBytes = new byte[messageSizeBytes + 5];

			// "Header"
			rawBytes[0] = type;
			insertInt(rawBytes, 1, messageSizeBytes);

			// buffer.array() returns the complete array (1024 bytes) and
			// did not apply the "real" size of the object
			buffer.get(rawBytes, 5, messageSizeBytes);

			connection.send(rawBytes);
		} catch (final Throwable t) {
			LOG.error("Could not write", t);
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
