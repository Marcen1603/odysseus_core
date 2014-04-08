package de.uniol.inf.is.odysseus.p2p_new.data.endpoint;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.data.AbstractTransmissionSender;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;

public class EndpointDataTransmissionSender extends AbstractTransmissionSender implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(EndpointDataTransmissionSender.class);

	private final Collection<PeerID> pids = Lists.newLinkedList();
	private final int idHash;
	private final IPeerCommunicator communicator;

	public EndpointDataTransmissionSender(IPeerCommunicator communicator, String destinationPeer, String id) {
		this.communicator = communicator;
		this.communicator.addListener(this, OpenMessage.class);
		this.communicator.addListener(this, CloseMessage.class);

		if (!Strings.isNullOrEmpty(destinationPeer)) {
			pids.add(toPeerID(destinationPeer));
		}

		idHash = id.hashCode();
	}

	@Override
	public void sendData(byte[] data) throws DataTransmissionException {
		DataMessage msg = new DataMessage(idHash, data);
		try {
			sendMessageToPeers(msg);
		} catch (PeerCommunicationException e) {
			throw new DataTransmissionException("Could not send data", e);
		}
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation) throws DataTransmissionException {
		PunctuationMessage msg = new PunctuationMessage(punctuation, idHash);

		try {
			sendMessageToPeers(msg);
		} catch (PeerCommunicationException e) {
			throw new DataTransmissionException("Could not send punctuation", e);
		}
	}

	@Override
	public void sendDone() throws DataTransmissionException {
		try {
			sendMessageToPeers(new DoneMessage(idHash));
		} catch (PeerCommunicationException e) {
			throw new DataTransmissionException("Could not send done message", e);
		}
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof OpenMessage) {
			processOpenMessage(senderPeer, (OpenMessage)message);
		} else if (message instanceof CloseMessage) {
			processCloseMessage(senderPeer, (CloseMessage)message);
		}
	}

	protected void processOpenMessage(PeerID senderPeer, OpenMessage message) {
		if (message.getIdHash() == idHash) {
			synchronized (pids) {
				if (!pids.contains(senderPeer)) {
					pids.add(senderPeer);
					
					if (pids.size() == 1) {
						fireOpenEvent();
					}
				}
			}
			
			trySend(senderPeer, new OpenAckMessage(idHash));
		}
	}

	protected void processCloseMessage(PeerID senderPeer, CloseMessage message) {
		if (message.getIdHash() == idHash) {
			synchronized (pids) {
				if( pids.contains(senderPeer)) {
					pids.remove(senderPeer);
					
					if( pids.isEmpty() ) {
						fireCloseEvent();
					}
				}
			}
			
			trySend(senderPeer, new CloseAckMessage(idHash));
		}
	}
	
	protected final Collection<PeerID> getOpenedPeers() {
		return pids;
	}
	
	private void trySend(PeerID senderPeer, IMessage message) {
		try {
			communicator.send(senderPeer, message);
		} catch (PeerCommunicationException e) {
			LOG.debug("Could not send openack message", e);
		}
	}

	private void sendMessageToPeers(IMessage msg) throws PeerCommunicationException {
		synchronized (pids) {
			for (PeerID pi : pids) {
				communicator.send(pi, msg);
			}
		}
	}

	protected static PeerID toPeerID(String text) {
		try {
			final URI id = new URI(text);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}
	
}
