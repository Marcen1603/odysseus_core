package de.uniol.inf.is.odysseus.p2p_new.data.endpoint;

import java.net.URI;
import java.net.URISyntaxException;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.p2p_new.data.AbstractTransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;

public class EndpointDataTransmissionReceiver extends AbstractTransmissionReceiver implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(EndpointDataTransmissionSender.class);

	private final IPeerCommunicator peerCommunicator;
	private final PeerID pid;
	private final int idHash;

	private RepeatingMessageSend openRepeater;
	private RepeatingMessageSend closeRepeater;

	public EndpointDataTransmissionReceiver(IPeerCommunicator communicator, String peerID, String id) {
		this.peerCommunicator = communicator;

		pid = toPeerID(peerID);
		idHash = id.hashCode();
	}
	
	@Override
	public void open() {
		this.peerCommunicator.addListener(this, DataMessage.class);
		this.peerCommunicator.addListener(this, PunctuationMessage.class);
		this.peerCommunicator.addListener(this, OpenAckMessage.class);
		this.peerCommunicator.addListener(this, CloseAckMessage.class);
		this.peerCommunicator.addListener(this, DoneMessage.class);
	}
	
	@Override
	public void close() {
		this.peerCommunicator.removeListener(this, DataMessage.class);
		this.peerCommunicator.removeListener(this, PunctuationMessage.class);
		this.peerCommunicator.removeListener(this, OpenAckMessage.class);
		this.peerCommunicator.removeListener(this, CloseAckMessage.class);
		this.peerCommunicator.removeListener(this, DoneMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof DataMessage) {
			DataMessage dataMessage = (DataMessage) message;
			if (dataMessage.getIdHash() == idHash) {
				fireDataEvent(dataMessage.getData());
			}
		} else if (message instanceof PunctuationMessage) {
			PunctuationMessage puncMessage = (PunctuationMessage) message;
			if (puncMessage.getIdHash() == idHash) {
				firePunctuation(puncMessage.getPunctuation());
			}

		} else if (message instanceof DoneMessage) {
			DoneMessage msg = (DoneMessage) message;
			if (msg.getIdHash() == idHash) {
				fireDoneEvent();
			}
		} else if (message instanceof OpenAckMessage) {
			OpenAckMessage msg = (OpenAckMessage) message;
			if (msg.getIdHash() == idHash) {
				LOG.debug("Got open ack from '{}'", P2PDictionary.getInstance().getRemotePeerName(senderPeer));

				openRepeater.stopRunning();
				openRepeater = null;
			}
		} else if (message instanceof CloseAckMessage) {
			CloseAckMessage msg = (CloseAckMessage) message;
			if (msg.getIdHash() == idHash) {
				LOG.debug("Got close ack from '{}'", P2PDictionary.getInstance().getRemotePeerName(senderPeer));

				closeRepeater.stopRunning();
				closeRepeater = null;
			}
		}
	}

	@Override
	public void sendOpen() throws DataTransmissionException {
		LOG.debug("Send open to peer '{}'", P2PDictionary.getInstance().getRemotePeerName(pid));
		final OpenMessage openMessage = new OpenMessage(idHash);

		openRepeater = new RepeatingMessageSend(peerCommunicator, openMessage, pid);
		openRepeater.start();
	}

	@Override
	public void sendClose() throws DataTransmissionException {
		LOG.debug("Send close to peer '{}'", P2PDictionary.getInstance().getRemotePeerName(pid));

		CloseMessage closeMessage = new CloseMessage(idHash);
		closeRepeater = new RepeatingMessageSend(peerCommunicator, closeMessage, pid);
		closeRepeater.start();
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
