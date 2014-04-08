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
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.communication.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.p2p_new.data.AbstractTransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;

public class EndpointDataTransmissionReceiver extends AbstractTransmissionReceiver implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(EndpointDataTransmissionSender.class);

	private final IPeerCommunicator peerCommunicator;
	private final PeerID pid;
	private final int idHash;

	private RepeatingMessageSend openRepeater;
	private RepeatingMessageSend closeRepeater;
	
	public EndpointDataTransmissionReceiver(IPeerCommunicator communicator, String peerID, String id) {
		this.peerCommunicator = communicator;
		this.peerCommunicator.addListener(this, DataMessage.class);
		this.peerCommunicator.addListener(this, PunctuationMessage.class);
		this.peerCommunicator.addListener(this, OpenAckMessage.class);
		this.peerCommunicator.addListener(this, CloseAckMessage.class);
		this.peerCommunicator.addListener(this, DoneMessage.class);
		
		pid = toPeerID(peerID);
		idHash = id.hashCode();
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if( message instanceof DataMessage ) {
			DataMessage dataMessage = (DataMessage)message;
			if( dataMessage.getIdHash() == idHash ) {
				fireDataEvent(dataMessage.getData());
			}
		} else if( message instanceof PunctuationMessage ) {
			PunctuationMessage puncMessage = (PunctuationMessage)message;
			if( puncMessage.getIdHash() == idHash ) {
				firePunctuation(puncMessage.getPunctuation());
			}
			
		} else if( message instanceof DoneMessage ) {
			DoneMessage msg = (DoneMessage)message;
			if( msg.getIdHash() == idHash ) {
				fireDoneEvent();
			}
		} else if( message instanceof OpenAckMessage ) {
			OpenAckMessage msg = (OpenAckMessage)message;
			if( msg.getIdHash() == idHash ) {
				openRepeater.stopRunning();
				openRepeater = null;
			}
		} else if( message instanceof CloseAckMessage ) {
			CloseAckMessage msg = (CloseAckMessage)message;
			if( msg.getIdHash() == idHash ) {
				closeRepeater.stopRunning();
				closeRepeater = null;
			}
		}
	}
	
	@Override
	public void sendOpen() throws DataTransmissionException {
		try {
			final OpenMessage openMessage = new OpenMessage(idHash);
			peerCommunicator.send(pid, openMessage);

			openRepeater = new RepeatingMessageSend(peerCommunicator, openMessage, pid);
			openRepeater.start();
			
		} catch (PeerCommunicationException e) {
			throw new DataTransmissionException("Could not send open message", e);
		}
	}
	
	@Override
	public void sendClose() throws DataTransmissionException {
		try {
			CloseMessage closeMessage = new CloseMessage(idHash);
			peerCommunicator.send(pid, closeMessage);
			
			closeRepeater = new RepeatingMessageSend(peerCommunicator, closeMessage, pid);
			closeRepeater.start();
			
		} catch (PeerCommunicationException e) {
			throw new DataTransmissionException("Could not send close message", e);
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
