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
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator.JxtaBundleReceiverAO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.wrapper.JxtaReceiverWrapper;

@SuppressWarnings("rawtypes")
public class JxtaBundleReceiverPO<T extends IStreamObject> extends
		AbstractSource<T> implements Observer {

	private static final Logger LOG = LoggerFactory
			.getLogger(JxtaBundleReceiverPO.class);
	private ArrayList<JxtaReceiverWrapper<T>> receiverList;
	private LoadBalancingSynchronizerPO synchronizer;
	private int transferPort = 0;

	@Override
	protected void process_open() {
		for (JxtaReceiverWrapper receiver : receiverList) {
			receiver.process_open();
		}
	}

	private void switchTransferPort() {
		transferPort = (transferPort + 1) % 2;
	}

	@SuppressWarnings("unchecked")
	private void subscribeReceiver(JxtaReceiverWrapper<T> receiver) {
		for (PhysicalSubscription<ISink<? super T>> subscription : receiver
				.getSubscriptions()) {
			subscription.setDone(true);
		}
		receiverList.add(receiver);
		receiver.subscribeSink(synchronizer, transferPort, 0,
				receiver.getOutputSchema(), true, 0);
		switchTransferPort();
	}

	@SuppressWarnings("unchecked")
	public JxtaBundleReceiverPO(JxtaBundleReceiverAO ao)
			throws DataTransmissionException {
		receiverList = new ArrayList<JxtaReceiverWrapper<T>>();
		JxtaReceiverWrapper<T> firstReceiver = new JxtaReceiverWrapper<T>(ao);
		synchronizer = new LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>>(
				new TITransferArea<IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval>>());
		synchronizer.addListener(this);
		for (PhysicalSubscription<ISink<? super T>> subscription : firstReceiver
				.getSubscriptions()) {
			synchronizer.subscribeSink(subscription.getTarget(),
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema(),
					true, 0);
			subscription.setDone(true);
		}
		subscribeReceiver(firstReceiver);

	}

	public JxtaBundleReceiverPO(JxtaBundleReceiverPO<T> po) {
		receiverList = po.receiverList;
		synchronizer = po.synchronizer;
		synchronizer.addListener(this);
	}

	@Override
	public AbstractSource<T> clone() {
		return new JxtaBundleReceiverPO<T>(this);
	}

	@Override
	protected void process_close() {
		for (JxtaReceiverWrapper receiver : receiverList) {
			receiver.process_close();
		}
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (ipo == this) {
			return true;
		}
		return false;
	}

	public final ITransmissionReceiver getTransmission() {
		return receiverList.get(0).getTransmission();
	}

	public String getPipeIDString() {
		return receiverList.get(0).getPipeIDString();
	}

	public String getPeerIDString() {
		return receiverList.get(0).getPeerIDString();
	}

	public long getTotalReceivedByteCount() {
		return receiverList.get(0).getTotalReceivedByteCount();
	}

	public double getDownloadRateBytesPerSecond() {
		return receiverList.get(0).getDownloadRateBytesPerSecond();
	}

	@Override
	public String getName() {
		return super.getName() + determineDestinationPeerName();
	}

	private String determineDestinationPeerName() {
		if (Strings.isNullOrEmpty(getPeerIDString())) {
			return "";
		}

		return " ["
				+ P2PDictionary.getInstance().getRemotePeerName(
						toPeerID(getPeerIDString())) + "]";
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

	@Override
	public void update(Observable synchronizer, Object arg) {
		int portToRemove = (int) arg;
		this.deleteReceiver(portToRemove);
	}

	public boolean deleteReceiver(int index) {
		if (receiverList.size() <= 1) {
			return false;
		}
		try {
			receiverList.remove(index);
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public void addReceiver(JxtaReceiverPO<T> receiver) {
		JxtaReceiverWrapper<T> wrapper = new JxtaReceiverWrapper<T>(receiver);
		this.subscribeReceiver(wrapper);
	}

}
