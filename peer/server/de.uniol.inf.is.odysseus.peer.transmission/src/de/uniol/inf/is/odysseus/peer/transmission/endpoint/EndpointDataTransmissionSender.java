package de.uniol.inf.is.odysseus.peer.transmission.endpoint;

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
import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.transmission.AbstractTransmissionSender;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;
import de.uniol.inf.is.odysseus.peer.transmission.socket.PortAckMessage;

public class EndpointDataTransmissionSender extends AbstractTransmissionSender implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(EndpointDataTransmissionSender.class);

	private final Collection<PeerID> pids = Lists.newLinkedList();
	private final int idHash;
	private final IPeerCommunicator communicator;

	public EndpointDataTransmissionSender(IPeerCommunicator communicator, String destinationPeer, String id) {
		this.communicator = communicator;

		if (!Strings.isNullOrEmpty(destinationPeer)) {
			pids.add(toPeerID(destinationPeer));
		}

		idHash = id.hashCode();
	}

	@Override
	public void open() {
		LOG.debug("Open called...");
		for(PeerID pid : pids) {
			LOG.debug(pid.toString());
		}
		this.communicator.addListener(this, OpenMessage.class);
		this.communicator.addListener(this, CloseMessage.class);
		this.communicator.addListener(this, PortAckMessage.class);
	}

	@Override
	public void close() {
		LOG.debug("Close called...");
		for(PeerID pid : pids) {
			LOG.debug(pid.toString());
		}
		this.communicator.removeListener(this, OpenMessage.class);
		this.communicator.removeListener(this, CloseMessage.class);
		this.communicator.removeListener(this, PortAckMessage.class);
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
			OpenMessage openMessage = (OpenMessage) message;
			if (openMessage.getIdHash() == idHash) {
				LOG.debug("Sender with following PIDS got OPEN-Message:");
				for(PeerID pid : pids) {
					LOG.debug(pid.toString());
				}
				processOpenMessage(senderPeer, openMessage);
			}
		} else if (message instanceof CloseMessage) {
			CloseMessage closeMessage = (CloseMessage) message;
			if (closeMessage.getIdHash() == idHash) {
				processCloseMessage(senderPeer, closeMessage);
			}
		} 
	}

	protected void processOpenMessage(PeerID senderPeer, OpenMessage message) {
		LOG.debug("Got open message from '{}'", senderPeer);

		trySend(senderPeer, new OpenAckMessage(idHash));

		synchronized (pids) {
			if (pids.size() == 1 && pids.contains(senderPeer)) {
				LOG.debug("Call open event");
				fireOpenEvent();
			} else if (!pids.contains(senderPeer)) {
				pids.add(senderPeer);

				if (pids.size() == 1) {
					LOG.debug("Call open event");
					fireOpenEvent();
				}
			}
		}
	}

	protected void processCloseMessage(PeerID senderPeer, CloseMessage message) {
		LOG.debug("Got close message from '{}'", senderPeer);

		trySend(senderPeer, new CloseAckMessage(idHash));
		
		synchronized (pids) {
			if (pids.contains(senderPeer)) {
				pids.remove(senderPeer);

				if (pids.isEmpty()) {
					LOG.debug("Call close event");
					fireCloseEvent();
				}
			}
		}
	}

	protected final Collection<PeerID> getOpenedPeers() {
		return pids;
	}

	private void trySend(PeerID senderPeer, IMessage message) {
		try {
			LOG.debug("Trying to send message {}", message.getClass().getName());
			
			communicator.send(senderPeer, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send openack message", e);
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

	protected final int getId() {
		return idHash;
	}
	
	protected IPeerCommunicator getCommunicator() {
		return communicator;
	}

	@Override
	public void resetPeerList() {
		while (pids.iterator().hasNext()) {
			Object o = pids.iterator().next();
			pids.remove(o);
		}
	}

	@Override
	public void flushBuffers() {
		// TODO Auto-generated method stub
		
	}

}