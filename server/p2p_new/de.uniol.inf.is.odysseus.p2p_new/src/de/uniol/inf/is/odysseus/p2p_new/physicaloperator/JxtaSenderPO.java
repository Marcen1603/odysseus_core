package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.datahandler.NullAwareTupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionManager;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionSender;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionSenderListener;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

public class JxtaSenderPO<T extends IStreamObject<?>> extends AbstractSink<T> implements ITransmissionSenderListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaSenderPO.class);

	private final ITransmissionSender transmission;
	private final String pipeIDString;
	private final String peerIDString;
	
	private NullAwareTupleDataHandler dataHandler;

	private long totalSendByteCount;
	
	private double uploadRateBytesPerSecond;
	private long uploadRateTimestamp;
	private long uploadRateCurrentByteCount;

	public JxtaSenderPO(JxtaSenderAO ao) {
		pipeIDString = ao.getPipeID();
		peerIDString = ao.getPeerID();

		this.transmission = DataTransmissionManager.getInstance().registerTransmissionSender(peerIDString, pipeIDString);
		this.transmission.addListener(this);
		this.transmission.open();
	}

	public JxtaSenderPO(JxtaSenderPO<T> po) {
		super(po);

		this.dataHandler = po.dataHandler;
		this.transmission = po.transmission;
		this.peerIDString = po.peerIDString;
		this.pipeIDString = po.pipeIDString;
		
		this.totalSendByteCount = po.totalSendByteCount;
		this.uploadRateBytesPerSecond = po.uploadRateBytesPerSecond;
		this.uploadRateCurrentByteCount = po.uploadRateCurrentByteCount;
		this.uploadRateTimestamp = po.uploadRateTimestamp;
	}
	
	@Override
	public AbstractSink<T> clone() {
		return new JxtaSenderPO<T>(this);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (ipo == this) {
			return true;
		}

		return false;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		createDataHandlerIfNeeded();

		try {
			transmission.sendPunctuation(punctuation);
		} catch (DataTransmissionException e) {
			LOG.error("Could not send punctuation", e);
		}
	}

	@Override
	protected void process_next(T object, int port) {
		createDataHandlerIfNeeded();

		final ByteBuffer buffer = ByteBuffer.allocate(P2PNewPlugIn.TRANSPORT_BUFFER_SIZE);
		dataHandler.writeData(buffer, object);
		if (object.getMetadata() != null) {
			final byte[] metadataBytes = ObjectByteConverter.objectToBytes(object.getMetadata());
			buffer.put(metadataBytes);
		}
		buffer.flip();

		final int messageSizeBytes = buffer.remaining();
		final byte[] rawBytes = new byte[messageSizeBytes];

		// buffer.array() returns the complete array
		// (P2PNewPlugIn.TRANSPORT_BUFFER_SIZE bytes) and
		// did not apply the "real" size of the object
		buffer.get(rawBytes, 0, messageSizeBytes);

		totalSendByteCount += rawBytes.length;
		uploadRateCurrentByteCount += rawBytes.length;
		if( System.currentTimeMillis() - uploadRateTimestamp > 10 * 1000 ) {
			uploadRateBytesPerSecond = ( uploadRateCurrentByteCount / 10.0);
			uploadRateCurrentByteCount = 0;
			uploadRateTimestamp = System.currentTimeMillis();
		}
		
		try {
			transmission.sendData(rawBytes);
		} catch (DataTransmissionException e) {
			LOG.error("Could not send data", e);
			// TODO: proper error handling
		}
	}

	private void createDataHandlerIfNeeded() {
		if (dataHandler == null) {
			dataHandler = (NullAwareTupleDataHandler) new NullAwareTupleDataHandler().createInstance(getOutputSchema());
			LOG.debug("{} : Data Handler created", getName());
		}
	}

	@Override
	public void onReceiveOpen(ITransmissionSender sender) {
		uploadRateTimestamp = System.currentTimeMillis();
		
		Optional<Integer> optQueryID = determineQueryID(getOwner());
		if (optQueryID.isPresent()) {
			int queryID = optQueryID.get();
			LOG.debug("{} : Starting query {}", getName(), queryID);
			ServerExecutorService.getServerExecutor().startQuery(queryID, SessionManagementService.getActiveSession());
		}
	}

	@Override
	public void onReceiveClose(ITransmissionSender sender) {
		Optional<Integer> optQueryID = determineQueryID(getOwner());
		if (optQueryID.isPresent()) {
			int queryID = optQueryID.get();
			LOG.debug("{} : Stopping query {}", getName(), queryID);
			ServerExecutorService.getServerExecutor().stopQuery(queryID, SessionManagementService.getActiveSession());
		}
	}

	private static Optional<Integer> determineQueryID(List<IOperatorOwner> owner) {
		if (owner.isEmpty()) {
			return Optional.absent();
		}
		return Optional.of(owner.get(0).getID());
	}

	@Override
	protected void process_done(int port) {
		try {
			transmission.sendDone();
		} catch (DataTransmissionException e) {
			LOG.error("Could not send done message", e);
		}
	}
	
	public final ITransmissionSender getTransmission() {
		return transmission;
	}
	
	public String getPeerIDString() {
		return peerIDString;
	}
	
	public String getPipeIDString() {
		return pipeIDString;
	}
	
	public double getUploadRateBytesPerSecond() {
		return uploadRateBytesPerSecond;
	}
	
	public long getTotalSendByteCount() {
		return totalSendByteCount;
	}
}
