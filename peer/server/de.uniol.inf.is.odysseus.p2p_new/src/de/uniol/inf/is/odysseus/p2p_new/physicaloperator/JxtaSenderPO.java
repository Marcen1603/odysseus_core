package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

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
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.PeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.util.IObservableOperator;
import de.uniol.inf.is.odysseus.p2p_new.util.IOperatorObserver;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;

public class JxtaSenderPO<T extends IStreamObject<?>> extends AbstractSink<T> implements ITransmissionSenderListener,
		IObservableOperator {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaSenderPO.class);

	private ITransmissionSender transmission;
	private final String pipeIDString;
	private String peerIDString;
	private final boolean writeResourceUsage;
	private final PeerID localPeerID;
	private final String localPeerName;

	private NullAwareTupleDataHandler dataHandler;

	private long totalSendByteCount;

	private double uploadRateBytesPerSecond;
	private long uploadRateTimestamp;
	private long uploadRateCurrentByteCount;

	public JxtaSenderPO(JxtaSenderAO ao) throws DataTransmissionException {
		pipeIDString = ao.getPipeID();
		peerIDString = ao.getPeerID();
		writeResourceUsage = ao.isWriteResourceUsage();

		localPeerID = P2PNetworkManager.getInstance().getLocalPeerID();
		localPeerName = P2PNetworkManager.getInstance().getLocalPeerName();

		this.transmission = DataTransmissionManager.getInstance()
				.registerTransmissionSender(peerIDString, pipeIDString);
		this.transmission.addListener(this);
		this.transmission.open();

		mObservers = new Vector<IOperatorObserver>();
	}

	public JxtaSenderPO(JxtaSenderPO<T> po) {
		super(po);

		this.dataHandler = po.dataHandler;
		this.transmission = po.transmission;
		this.peerIDString = po.peerIDString;
		this.pipeIDString = po.pipeIDString;
		this.writeResourceUsage = po.writeResourceUsage;
		this.localPeerID = po.localPeerID;
		this.localPeerName = po.localPeerName;

		this.totalSendByteCount = po.totalSendByteCount;
		this.uploadRateBytesPerSecond = po.uploadRateBytesPerSecond;
		this.uploadRateCurrentByteCount = po.uploadRateCurrentByteCount;
		this.uploadRateTimestamp = po.uploadRateTimestamp;

		mObservers = new Vector<IOperatorObserver>();
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

		ByteBuffer buffer = ByteBuffer.allocate(P2PNewPlugIn.TRANSPORT_BUFFER_SIZE);
		dataHandler.writeData(buffer, object);

		Object metadata = object.getMetadata();
		if (metadata != null) {
			if (metadata instanceof ISystemLoad) {
				((ISystemLoad) metadata).addSystemLoad(localPeerName);
			}

			byte[] metadataBytes = ObjectByteConverter.objectToBytes(object.getMetadata());
			buffer.putInt(metadataBytes.length);
			buffer.put(metadataBytes);
		}

		Map<String, Object> metadataMap = object.getMetadataMap();
		if (!metadataMap.isEmpty()) {
			byte[] metadataMapBytes = ObjectByteConverter.objectToBytes(metadataMap);
			buffer.putInt(metadataMapBytes.length);
			buffer.put(metadataMapBytes);
		}

		buffer.flip();

		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes];

		// buffer.array() returns the complete array
		// (P2PNewPlugIn.TRANSPORT_BUFFER_SIZE bytes) and
		// did not apply the "real" size of the object
		buffer.get(rawBytes, 0, messageSizeBytes);

		totalSendByteCount += rawBytes.length;
		uploadRateCurrentByteCount += rawBytes.length;
		if (System.currentTimeMillis() - uploadRateTimestamp > 10 * 1000) {
			uploadRateBytesPerSecond = (uploadRateCurrentByteCount / 10.0);
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

	/**
	 * Updates the peerId from this sender, if there is a new peer which has the receiver to this sender. E.g. when
	 * there is a new peer due to recovery
	 * 
	 * @param peerId
	 *            The new peerId
	 */
	public void setPeerIDString(String peerId) {
		this.peerIDString = peerId;

		// Notify the observers with the new peerId so that they can update the backup-information
		List<String> infoList = new ArrayList<String>();
		infoList.add(peerId);
		infoList.add(this.pipeIDString);
		notifyObservers(infoList);
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

	@Override
	public String getName() {
		return super.getName() + determineDestinationPeerName();
	}

	private String determineDestinationPeerName() {
		if (Strings.isNullOrEmpty(peerIDString)) {
			return "";
		}

		return " [" + PeerDictionary.getInstance().getRemotePeerName(toPeerID(peerIDString)) + "]";
	}

	protected static PeerID toPeerID(String peerIDString) {
		try {
			final URI id = new URI(peerIDString);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not get id from peerIDString {}", peerIDString, ex);
			return null;
		}
	}

	/**
	 * To send the data to a new peer (e.g. in case of recovery) use this method.
	 * 
	 * @param peerId
	 *            The id of the new receiver peer
	 * @throws DataTransmissionException
	 */
	public void sendToNewPeer(String peerId) throws DataTransmissionException {
		// Info for the observers
		List<String> infoList = new ArrayList<String>();
		infoList.add(this.peerIDString);
		
		this.peerIDString = peerId;

		// Notify the observers with the new peerId so that they can update the backup-information
		infoList.add(peerId);
		infoList.add(this.pipeIDString);
		notifyObservers(infoList);

		// Don't mess with the transmission, as the receiver does all the negotiation.
		// Just remove all peers in the list, cause open messages would not be used if we don't do this 
		transmission.resetPeerList();
	}

	// For the observer-pattern
	// ------------------------

	private Vector<IOperatorObserver> mObservers;

	@Override
	public void addObserver(IOperatorObserver observer) {
		synchronized (mObservers) {
			if (!mObservers.contains(observer)) {
				mObservers.addElement(observer);
				LOG.debug("New observer added.");
			}
		}
	}

	@Override
	public void removeObserver(IOperatorObserver observer) {
		synchronized (mObservers) {
			mObservers.removeElement(observer);
		}
	}

	@Override
	public void removeObservers() {
		synchronized (mObservers) {
			mObservers.removeAllElements();
		}
	}

	@Override
	public void notifyObservers(Object arg) {
		synchronized (mObservers) {
			for (int i = 0; i < mObservers.size(); i++) {
				mObservers.elementAt(i).update(this, arg);
			}
		}
	}
}
