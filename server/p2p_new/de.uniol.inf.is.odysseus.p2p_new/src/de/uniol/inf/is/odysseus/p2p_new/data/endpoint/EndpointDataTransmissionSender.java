package de.uniol.inf.is.odysseus.p2p_new.data.endpoint;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionSender;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionSenderListener;

public class EndpointDataTransmissionSender implements ITransmissionSender, IPeerCommunicatorListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(EndpointDataTransmissionSender.class);

	private final Collection<ITransmissionSenderListener> listeners = Lists.newArrayList();
	
	private final PeerID pid;
	private final int idHash;
	private final IPeerCommunicator communicator;

	public EndpointDataTransmissionSender(IPeerCommunicator communicator, String destinationPeer, String id) {
		this.communicator = communicator;
		this.communicator.addListener(this, OpenMessage.class);
		this.communicator.addListener(this, CloseMessage.class);
		
		pid = toPeerID(destinationPeer);
		idHash = id.hashCode();
	}

	@Override
	public void sendData(byte[] data) throws DataTransmissionException {
		DataMessage msg = new DataMessage(idHash, data);
		try {
			communicator.send(pid, msg);
		} catch (PeerCommunicationException e) {
			throw new DataTransmissionException("Could not send data", e);
		}
	}
	
	@Override
	public void sendPunctuation(IPunctuation punctuation) throws DataTransmissionException {
		PunctuationMessage msg = new PunctuationMessage(punctuation, idHash);
		
		try {
			communicator.send(pid, msg);
		} catch (PeerCommunicationException e) {
			throw new DataTransmissionException("Could not send punctuation", e);
		}
	}
	
	@Override
	public void sendDone() throws DataTransmissionException {
		try {
			communicator.send(pid, new DoneMessage(idHash));
		} catch (PeerCommunicationException e) {
			throw new DataTransmissionException("Could not send done message", e);
		}
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if( message instanceof OpenMessage ) {
			OpenMessage copenMessage = (OpenMessage)message;
			if( copenMessage.getIdHash() == idHash ) {
				fireCloseEvent();
			}
			fireOpenEvent();
		} else if( message instanceof CloseMessage ) {
			CloseMessage closeMessage = (CloseMessage)message;
			if( closeMessage.getIdHash() == idHash ) {
				fireCloseEvent();
			}
		}
	}

	private static PeerID toPeerID(String text) {
		try {
			final URI id = new URI(text);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	@Override
	public void addListener(ITransmissionSenderListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(ITransmissionSenderListener listener) {
		synchronized(listener) {
			listeners.remove(listener);
		}
	}
	
	private void fireOpenEvent() {
		synchronized( listeners ) {
			for( ITransmissionSenderListener listener : listeners ) {
				try {
					listener.onReceiveOpen(this);
				} catch( Throwable t ) {
					LOG.error("Exeption in transmission listener during open", t);
				}
			}
		}
	}
	
	private void fireCloseEvent() {
		synchronized( listeners ) {
			for( ITransmissionSenderListener listener : listeners ) {
				try {
					listener.onReceiveClose(this);
				} catch( Throwable t ) {
					LOG.error("Exeption in transmission listener during close", t);
				}
			}
		}
	}
}
