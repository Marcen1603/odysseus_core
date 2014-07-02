package de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.datahandler.NullAwareTupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionSender;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator.JxtaBundleSenderAO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.wrapper.JxtaSenderWrapper;

/***
 * Physical Operator to allow for Multiple outgoing senders in dynamic load
 * balancing. Works by holding a List of Wrapper Objects for
 * {@link de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO}
 * 
 * @author Carsten Cordes
 * 
 */
@Deprecated
public class JxtaBundleSenderPO<T extends IStreamObject<?>> extends
		AbstractSink<T> {

	/**
	 * The rate heartbeats are send.
	 */
	private final int heartbeatRate = 10;

	/**
	 * The current amount of heartbeats.
	 */
	private int heartbeatCounter;

	private static final Logger LOG = LoggerFactory
			.getLogger(JxtaBundleSenderPO.class);

	/**
	 * Updates relevant values in senderList.
	 */
	private void updateChildren() {
		for(JxtaSenderWrapper<T> sender : senderList) {
			sender.addOwner(getOwner());
		}
	}
	
	/***
	 * List of Sender Wrappers
	 */
	private ArrayList<JxtaSenderWrapper<T>> senderList;

	private NullAwareTupleDataHandler dataHandler;

	public JxtaBundleSenderPO(JxtaBundleSenderAO ao)
			throws DataTransmissionException {
		this.senderList = new ArrayList<JxtaSenderWrapper<T>>();
		this.senderList.add(new JxtaSenderWrapper<T>(ao));
		this.heartbeatCounter = 0;
		updateChildren();
	}
	
	/**
	 * Constructor from JxtaSenderAO
	 * 
	 */
	public JxtaBundleSenderPO(JxtaSenderAO ao) throws DataTransmissionException {
			this.senderList = new ArrayList<JxtaSenderWrapper<T>>();
			this.senderList.add(new JxtaSenderWrapper<T>(ao));
			this.heartbeatCounter = 0;
			updateChildren();
	}

	/**
	 * Constructor from JxtaSenderPO
	 * 
	 */
	public JxtaBundleSenderPO(JxtaBundleSenderPO<T> po) {
		this.senderList = new ArrayList<JxtaSenderWrapper<T>>();
		for (JxtaSenderWrapper<T> sender : po.senderList) {
			this.senderList.add(sender);
			this.heartbeatCounter = 0;
			updateChildren();
		}
	}

	/**
	 * Clones Operator
	 * 
	 */
	@Override
	public AbstractSink<T> clone() {
		return new JxtaBundleSenderPO<T>(this);
	}

	/**
	 * Determines if two Operators are equals
	 * @param ipo other Operator
	 */
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (ipo == this) {
			return true;
		}

		return false;
	}

	/**
	 * Forwards Punctuations to all senders
	 * 
	 * @param punctuation
	 *            Punctuation to send.
	 * @param port
	 *            is ignored
	 */
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		createDataHandlerIfNeeded();
		for (JxtaSenderWrapper<T> sender : senderList) {
			sender.processPunctuation(punctuation, port);
		}
	}

	/**
	 * Calls process_next in all senders.
	 */
	@Override
	protected void process_next(T object, int port) {
		createDataHandlerIfNeeded();
		
		for (JxtaSenderWrapper<T> sender : senderList) {
			sender.process_next(object, port);
		}
	}

	/**
	 * Creates new dataHandler if none is set.
	 */
	private void createDataHandlerIfNeeded() {
		if (dataHandler == null) {
			dataHandler = (NullAwareTupleDataHandler) new NullAwareTupleDataHandler()
					.createInstance(getOutputSchema());
			LOG.debug("{} : Data Handler created", getName());
		}
	}

	/**
	 * Calls process_done in all senders.
	 */
	@Override
	protected void process_done(int port) {
		for (JxtaSenderWrapper<T> sender : senderList) {
			sender.process_done(port);
		}
	}

	/***
	 * Gets Transmission of first Sender in list.
	 * 
	 * @return Transmission of first Sender in list.
	 */
	public final ITransmissionSender getTransmission() {
		return senderList.get(0).getTransmission();
	}

	/***
	 * Gets List of Transmissions
	 * 
	 * @return List of all outgoing transmissions.
	 */
	public final ImmutableList<ITransmissionSender> getTransmissions() {
		ArrayList<ITransmissionSender> transmissions = new ArrayList<ITransmissionSender>();
		for (JxtaSenderWrapper<T> sender : senderList) {
			transmissions.add(sender.getTransmission());
		}
		return ImmutableList.copyOf(transmissions);
	}

	/***
	 * Gets peerID of first Sender in list.
	 * 
	 * @return peerID of first Sender in list.
	 */
	public String getPeerIDString() {
		return senderList.get(0).getPeerIDString();
	}

	/**
	 * Gets pipeID of first Sender in list.
	 * 
	 * @return pipeID of first Sender in list.
	 */
	public String getPipeIDString() {
		return senderList.get(0).getPipeIDString();
	}

	/**
	 * Gets uploadRate of first Sender in list.
	 * 
	 * @return uploadRate of first Sender in list.
	 */
	public double getUploadRateBytesPerSecond() {
		return senderList.get(0).getUploadRateBytesPerSecond();
	}

	/**
	 * Gets Number of sent Bytes of first Sender in list.
	 * 
	 * @return Number of sent bytes of first Sender in list.
	 */
	public long getTotalSendByteCount() {
		return senderList.get(0).getTotalSendByteCount();
	}

	/**
	 * Gets Number of Operator
	 */
	@Override
	public String getName() {
		return super.getName() + determineDestinationPeerName();
	}

	/**
	 * Gets Name of destination Peer (first sender in list)
	 *
	 */
	private String determineDestinationPeerName() {
		String peerIdString = getPeerIDString();
		if (Strings.isNullOrEmpty(peerIdString)) {
			return "";
		}

		return " ["
				+ P2PDictionary.getInstance().getRemotePeerName(
						toPeerID(peerIdString)) + "]";
	}

	/**
	 * Gets peer ID from peerIDString 
	 * @param peerIDString Peer ID String
	 * @return peerID
	 */
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
	 * Adds physical Sender Operator to List
	 * @param po Operator to add.
	 */
	public void addSender(JxtaSenderPO<T> po) {
		senderList.add(new JxtaSenderWrapper<T>(po));
		updateChildren();
	}

	/**
	 * Deletes Sender from list if list contains more than one sender.
	 * @param index Listindex of parameter to remove.
	 * @return true if deletion was successful.
	 */
	public boolean deleteSender(int index) {
		if (senderList.size() <= 1) {
			return false;
		}
		try {
			senderList.remove(index);
			updateChildren();
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		
		
	}

	/**
	 * Sends Heartbeats
	 * 
	 * @param currentPoT
	 *            Point in Time.
	 */
	@SuppressWarnings("unused")
	private void sendHeartbeats(PointInTime currentPoT) {

		final Heartbeat heartbeat = Heartbeat.createNewHeartbeat(currentPoT);

		if (++this.heartbeatCounter % this.heartbeatRate != 0) {
			for (JxtaSenderWrapper<T> sender : senderList) {
				sender.processPunctuation(heartbeat, 0);
			}
		}

	}
}
