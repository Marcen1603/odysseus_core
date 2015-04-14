package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.datahandler.NullAwareTupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.p2p_new.service.PeerDictionaryService;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionManager;
import de.uniol.inf.is.odysseus.peer.transmission.ITransmissionReceiver;
import de.uniol.inf.is.odysseus.peer.transmission.ITransmissionReceiverListener;
import de.uniol.inf.is.odysseus.peer.util.ByteBufferUtil;
import de.uniol.inf.is.odysseus.peer.util.IObservableOperator;
import de.uniol.inf.is.odysseus.peer.util.IOperatorObserver;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;

@SuppressWarnings("rawtypes")
public class JxtaReceiverPO<T extends IStreamObject> extends AbstractSource<T> implements ITransmissionReceiverListener, IObservableOperator {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaReceiverPO.class);

	private final NullAwareTupleDataHandler dataHandler;
	private ITransmissionReceiver transmission;
	private final String pipeIDString;
	private String peerIDString;

	final private List<T> suspendBuffer = new LinkedList<>();

	private final String localPeerName;

	private long totalReceivedByteCount;

	private double downloadRateBytesPerSecond;
	private long downloadRateTimestamp;
	private long downloadRateCurrentByteCount;

	private boolean buffering = false;

	// So we know if the query was running when we do recovery
	private boolean isRunning;

	public JxtaReceiverPO(JxtaReceiverAO ao) throws DataTransmissionException {
		SDFSchema schema = ao.getOutputSchema().clone();
		setOutputSchema(schema);

		dataHandler = (NullAwareTupleDataHandler) new NullAwareTupleDataHandler().createInstance(schema);

		pipeIDString = ao.getPipeID();
		peerIDString = ao.getPeerID();

		localPeerName = P2PNetworkManagerService.getInstance().getLocalPeerName();

		transmission = DataTransmissionManager.registerTransmissionReceiver(peerIDString, pipeIDString);
		transmission.addListener(this);
		transmission.open();

		isRunning = false;

		mObservers = new Vector<IOperatorObserver>();
	}

	public JxtaReceiverPO(JxtaReceiverPO<T> po) {
		super(po);
		setOutputSchema(po.getOutputSchema().clone());

		this.dataHandler = po.dataHandler;
		this.transmission = po.transmission;
		this.pipeIDString = po.pipeIDString;
		this.peerIDString = po.peerIDString;

		this.localPeerName = po.localPeerName;

		this.totalReceivedByteCount = po.totalReceivedByteCount;
		this.downloadRateBytesPerSecond = po.downloadRateBytesPerSecond;
		this.downloadRateCurrentByteCount = po.downloadRateCurrentByteCount;
		this.downloadRateTimestamp = po.downloadRateTimestamp;

		isRunning = false;

		mObservers = new Vector<IOperatorObserver>();
	}

	@Override
	public AbstractSource<T> clone() {
		return new JxtaReceiverPO<T>(this);
	}

	@Override
	protected void process_close() {
		try {
			transmission.sendClose();
			isRunning = false;
		} catch (DataTransmissionException e) {
			LOG.error("Could not send close message", e);
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		downloadRateTimestamp = System.currentTimeMillis();

		try {
			transmission.sendOpen();
			isRunning = true;
		} catch (DataTransmissionException e) {
			throw new OpenFailedException(e);
		}
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (ipo == this) {
			return true;
		}
		return false;
	}

	@Override
	public void onReceiveData(ITransmissionReceiver receiver, byte[] data) {
		totalReceivedByteCount += data.length;
		downloadRateCurrentByteCount += data.length;
		if (System.currentTimeMillis() - downloadRateTimestamp > 10 * 1000) {
			downloadRateBytesPerSecond = (downloadRateCurrentByteCount / 10.0);
			downloadRateCurrentByteCount = 0;
			downloadRateTimestamp = System.currentTimeMillis();
		}

		@SuppressWarnings("unchecked")
		T streamObject = (T) ByteBufferUtil.createStreamObject(ByteBuffer.wrap(data), dataHandler);

		Object metadata = streamObject.getMetadata();
		if (metadata instanceof ISystemLoad) {
			ISystemLoad systemLoad = (ISystemLoad) metadata;
			systemLoad.addSystemLoad(localPeerName);
		}

		process_incoming(streamObject);
	}

	private void process_incoming(T streamObject) {
		if (buffering) {
			suspendBuffer.add(streamObject);
		} else {
			clearSuspendBuffer();
			transfer(streamObject);
		}
	}

	@Override
	public void onReceivePunctuation(ITransmissionReceiver receiver, IPunctuation punc) {
		sendPunctuation(punc);
	}

	@Override
	public void onReceiveDone(ITransmissionReceiver receiver) {
		process_done();
	}

	public final ITransmissionReceiver getTransmission() {
		return transmission;
	}

	public String getPipeIDString() {
		return pipeIDString;
	}

	public String getPeerIDString() {
		return peerIDString;
	}

	public long getTotalReceivedByteCount() {
		return totalReceivedByteCount;
	}

	public double getDownloadRateBytesPerSecond() {
		return downloadRateBytesPerSecond;
	}

	@Override
	public String getName() {
		return super.getName() + determineDestinationPeerName();
	}

	private String determineDestinationPeerName() {
		if (Strings.isNullOrEmpty(peerIDString)) {
			return "";
		}

		return " [" + PeerDictionaryService.getInstance().getRemotePeerName(toPeerID(peerIDString)) + "]";
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

	public void startBuffering() {
		buffering = true;
	}

	public void stopBuffering() {
		buffering = false;
	}

	/**
	 * Updates the receiver so that is receives the data from a new peer
	 * 
	 * @param peerId
	 *            From the new peer from which this receiver should receive the
	 *            data
	 * @throws DataTransmissionException
	 */
	public void receiveFromNewPeer(String peerId) throws DataTransmissionException {
		// Info for the observers
		List<String> infoList = new ArrayList<String>();
		// The old peer id
		infoList.add(this.peerIDString);

		this.peerIDString = peerId;

		// Update transmission
		transmission.stopReceiving();
		transmission.close();
		transmission.removeListener(this);
		transmission.setPeerId(toPeerID(peerId));
		transmission.addListener(this);
		transmission.open();

		if (isRunning)
			process_open();

		// Notify the observers with the new peerId so that they can update the
		// backup-information
		infoList.add(peerId);
		infoList.add(this.pipeIDString);
		notifyObservers(infoList);
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

	private void clearSuspendBuffer() {
		if (suspendBuffer.isEmpty())
			return;

		for (T o : suspendBuffer) {
			transfer(o);
			// TODO: Thread.yield();
			// (as in ControllablePhysicalSubscription)

		}
		suspendBuffer.clear();
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
