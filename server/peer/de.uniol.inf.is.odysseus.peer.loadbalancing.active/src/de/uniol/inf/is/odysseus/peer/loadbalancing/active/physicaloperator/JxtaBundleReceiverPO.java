package de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.wrapper.JxtaReceiverWrapper;

/**
 * Bundles more than one receiver in a receiver List to allow for dynamic Load Balancing.
 * @author Carsten Cordes
 *
 */
@Deprecated
@SuppressWarnings("rawtypes")
public class JxtaBundleReceiverPO<T extends IStreamObject> extends
		AbstractSource<T> implements Observer {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(JxtaBundleReceiverPO.class);
	
	/**
	 * List of ReceiverWrapper Objects.
	 */
	private ArrayList<JxtaReceiverWrapper<T>> receiverList;
	
	/**
	 * Synchronizing Operator
	 */
	private final LoadBalancingSynchronizerPO synchronizer;
	
	/**
	 * Variable to determine current transfer port 
	 */
	private int transferPort = 0;

	/**
	 * Starts query. 
	 */
	@Override
	protected void process_open() {
		this.placeOutgoingSubscriptionsBehindSync();
		for (JxtaReceiverWrapper receiver : receiverList) {
			receiver.process_open();
		}
		receiverList.get(0).getSubscriptions();
	}

	/**
	 * Switches between outgoing Transfer Ports (0 or 1)
	 */
	private void switchTransferPort() {
		transferPort = (transferPort + 1) % 2;
	}

	/**
	 * Subscribes a JxtaReceiverWrapper to a Sync Operator used to union both incoming streams.
	 * @param receiver JxtaReceiverWrapper to subscribe
	 */
	@SuppressWarnings("unchecked")
	private void subscribeReceiver(JxtaReceiverWrapper<T> receiver) {
		receiver.unsubscribeFromAllSinks();
		receiverList.add(receiver);
		receiver.subscribeSink(synchronizer, transferPort, 0,
				receiver.getOutputSchema(), true, 0);
		switchTransferPort();
	}

	
	/**
	 * Constructor from JxtaReceiverAO. Can also be used with JxtaBundleReceiverAO
	 * @param ao Logical Operator
	 * @throws DataTransmissionException
	 */
	public JxtaBundleReceiverPO(JxtaReceiverAO ao)
			throws DataTransmissionException {

		ao.setOutputSchema(getOutputSchema());
		ao.addOwner(getOwner());

		receiverList = new ArrayList<JxtaReceiverWrapper<T>>();

		synchronizer = new LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>>();
		synchronizer.addListener(this);
		
		JxtaReceiverWrapper<T> firstReceiver = new JxtaReceiverWrapper<T>(ao);
		subscribeReceiver(firstReceiver);
	}

	/**
	 * Unlinks outgoing (physical) Subscriptions from this Operator and adds
	 * them behind sync operator. Done at start of Query.
	 */
	@SuppressWarnings("unchecked")
	private void placeOutgoingSubscriptionsBehindSync() {
		
		for (PhysicalSubscription<ISink<? super T>> subscription : this.getSubscriptions()) {
			this.unsubscribeSink(subscription);
			synchronizer.subscribeSink(subscription.getTarget(), subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema(),true,0);
			
		}
	}

	/**
	 * Copy Constructor
	 * @param po Physical Operator
	 */
	public JxtaBundleReceiverPO(JxtaBundleReceiverPO<T> po) {
		receiverList = po.receiverList;
		synchronizer = po.synchronizer;
		synchronizer.addListener(this);
	}

	/**
	 * Clones Operator
	 */
	@Override
	public AbstractSource<T> clone() {
		return new JxtaBundleReceiverPO<T>(this);
	}

	/***
	 * Closes a query.
	 */
	@Override
	protected void process_close() {
		for (JxtaReceiverWrapper receiver : receiverList) {
			receiver.process_close();
		}
	}

	/**
	 * Compares to another operator
	 * @param ipo other Operator
	 * @return true if operators are equal.
	 */
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (ipo == this) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return Transmission of first Receiver of first Receiver in List
	 */
	public final ITransmissionReceiver getTransmission() {
		return receiverList.get(0).getTransmission();
	}

	/**
	 * 
	 * @return PipeID as String of first Receiver in List
	 */
	public String getPipeIDString() {
		return receiverList.get(0).getPipeIDString();
	}

	/**
	 * 
	 * @return PeerID as String of first Receiver in List
	 */
	public String getPeerIDString() {
		return receiverList.get(0).getPeerIDString();
	}

	/**
	 * 
	 * @return TotalReceivedByteCount of first Receiver in List
	 */
	public long getTotalReceivedByteCount() {
		return receiverList.get(0).getTotalReceivedByteCount();
	}

	/**
	 * 
	 * @return DownloadRateBytesPerSecond of first Receiver in List
	 */
	public double getDownloadRateBytesPerSecond() {
		return receiverList.get(0).getDownloadRateBytesPerSecond();
	}

	/**
	 * @return name of Operator
	 */
	@Override
	public String getName() {
		return super.getName() + determineDestinationPeerName();
	}

	/**
	 * Determines destination peer name
	 * @return peer name
	 */
	private String determineDestinationPeerName() {
		if (Strings.isNullOrEmpty(getPeerIDString())) {
			return "";
		}

		return " ["
				+ P2PDictionary.getInstance().getRemotePeerName(
						toPeerID(getPeerIDString())) + "]";
	}

	/**
	 * Converts PeerID from String to ID.
	 * @param peerIDString
	 * @return PeerID
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
	 * Fired by Sync Operator when LoadBalancing is finished.
	 */
	@Override
	public void update(Observable synchronizer, Object arg) {
		int portToRemove = (int) arg;
		this.deleteReceiver(portToRemove);
	}

	/**
	 * Deletes Receiver from List if size of List > 1
	 * @param index Index of Receiver to delete
	 * @return true if deletion is successful.
	 */
	public boolean deleteReceiver(int index) {
		if (receiverList.size() <= 1) {
			return false;
		}
		try {
			JxtaReceiverPO<T> receiver = receiverList.get(index);
			receiverList.remove(index);
			receiver.unsubscribeFromAllSinks();
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * Adds receiver to list.
	 * @param receiver Receiver to add.
	 */
	public void addReceiver(JxtaReceiverPO<T> receiver) {
		JxtaReceiverWrapper<T> wrapper = new JxtaReceiverWrapper<T>(receiver);
		this.subscribeReceiver(wrapper);
	}

}
