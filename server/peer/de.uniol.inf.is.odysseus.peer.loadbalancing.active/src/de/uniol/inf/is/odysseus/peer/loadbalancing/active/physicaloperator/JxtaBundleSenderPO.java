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
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator.JxtaBundleSenderAO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.wrapper.JxtaSenderWrapper;

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

	private ArrayList<JxtaSenderWrapper<T>> senderList;

	private NullAwareTupleDataHandler dataHandler;

	public JxtaBundleSenderPO(JxtaBundleSenderAO ao)
			throws DataTransmissionException {
		this.senderList = new ArrayList<JxtaSenderWrapper<T>>();
		this.senderList.add(new JxtaSenderWrapper<T>(ao));
		this.heartbeatCounter = 0;
	}

	public JxtaBundleSenderPO(JxtaBundleSenderPO<T> po) {
		this.senderList = new ArrayList<JxtaSenderWrapper<T>>();
		for (JxtaSenderWrapper<T> sender : po.senderList) {
			this.senderList.add(sender);
			this.heartbeatCounter = 0;
		}
	}

	@Override
	public AbstractSink<T> clone() {
		return new JxtaBundleSenderPO<T>(this);
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
		for (JxtaSenderWrapper<T> sender : senderList) {
			sender.processPunctuation(punctuation, port);
		}
	}

	@Override
	protected void process_next(T object, int port) {
		for (JxtaSenderWrapper<T> sender : senderList) {
			sender.process_next(object, port);
		}
	}

	private void createDataHandlerIfNeeded() {
		if (dataHandler == null) {
			dataHandler = (NullAwareTupleDataHandler) new NullAwareTupleDataHandler()
					.createInstance(getOutputSchema());
			LOG.debug("{} : Data Handler created", getName());
		}
	}

	@Override
	protected void process_done(int port) {
		for (JxtaSenderWrapper<T> sender : senderList) {
			sender.process_done(port);
		}
	}

	public final ITransmissionSender getTransmission() {
		return senderList.get(0).getTransmission();
	}

	public final ImmutableList<ITransmissionSender> getTransmissions() {
		ArrayList<ITransmissionSender> transmissions = new ArrayList<ITransmissionSender>();
		for (JxtaSenderWrapper<T> sender : senderList) {
			transmissions.add(sender.getTransmission());
		}
		return ImmutableList.copyOf(transmissions);
	}

	public String getPeerIDString() {
		return senderList.get(0).getPeerIDString();
	}

	public String getPipeIDString() {
		return senderList.get(0).getPipeIDString();
	}

	// TODO Methods for more than 1 sender
	public double getUploadRateBytesPerSecond() {
		return senderList.get(0).getUploadRateBytesPerSecond();
	}

	public long getTotalSendByteCount() {
		return senderList.get(0).getTotalSendByteCount();
	}

	@Override
	public String getName() {
		return super.getName() + determineDestinationPeerName();
	}

	private String determineDestinationPeerName() {
		String peerIdString = getPeerIDString();
		if (Strings.isNullOrEmpty(peerIdString)) {
			return "";
		}

		return " ["
				+ P2PDictionary.getInstance().getRemotePeerName(
						toPeerID(peerIdString)) + "]";
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
	
	public void addSender(JxtaSenderPO<T> po) {
		senderList.add(new JxtaSenderWrapper<T>(po));
	}
	
	public boolean deleteSender(int index) {
		if(senderList.size()<=1) {
			return false;
		}
		try {
		senderList.remove(index);
			return true;
		}
		catch(IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	private void sendHeartbeats(PointInTime currentPoT) {
		
		final Heartbeat heartbeat = Heartbeat.createNewHeartbeat(currentPoT);
		
			
			if(++this.heartbeatCounter % this.heartbeatRate != 0) {
				for(JxtaSenderWrapper<T> sender : senderList) {
					sender.processPunctuation(heartbeat, 0);
				}
			}
			
	}
}
