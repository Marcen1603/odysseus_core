package de.uniol.inf.is.odysseus.p2p_new.data.endpoint;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiverListener;

public class EndpointDataTransmissionReceiver implements ITransmissionReceiver, IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(EndpointDataTransmissionSender.class);

	private final Collection<ITransmissionReceiverListener> listeners = Lists.newArrayList();
	
	private final IPeerCommunicator peerCommunicator;
	private final PeerID pid;
	private final int idHash;
	
	public EndpointDataTransmissionReceiver(IPeerCommunicator communicator, String peerID, String id) {
		this.peerCommunicator = communicator;
		this.peerCommunicator.addListener(this, DataMessage.class);
		this.peerCommunicator.addListener(this, PunctuationMessage.class);
		this.peerCommunicator.addListener(this, DoneMessage.class);
		
		pid = toPeerID(peerID);
		idHash = id.hashCode();
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if( message instanceof DataMessage ) {
			DataMessage dataMessage = (DataMessage)message;
			if( dataMessage.getIdHash() == idHash ) {
				fireListeners(dataMessage.getData());
			}
		} else if( message instanceof PunctuationMessage ) {
			PunctuationMessage puncMessage = (PunctuationMessage)message;
			if( puncMessage.getIdHash() == idHash ) {
				fireListeners(puncMessage.getPunctuation());
			}
			
		} else if( message instanceof DoneMessage ) {
			DoneMessage msg = (DoneMessage)message;
			if( msg.getIdHash() == idHash ) {
				fireDoneEvent();
			}
		}
	}
	
	@Override
	public void sendOpen() throws DataTransmissionException {
		try {
			peerCommunicator.send(pid, new OpenMessage(idHash));
		} catch (PeerCommunicationException e) {
			throw new DataTransmissionException("Could not send open message", e);
		}
	}
	
	@Override
	public void sendClose() throws DataTransmissionException {
		try {
			peerCommunicator.send(pid, new CloseMessage(idHash));
		} catch (PeerCommunicationException e) {
			throw new DataTransmissionException("Could not send close message", e);
		}
	}
	
	@Override
	public void addListener(ITransmissionReceiverListener listener) {
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(ITransmissionReceiverListener listener) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	private void fireListeners( byte[] data ) {
		synchronized( listeners ) {
			for( ITransmissionReceiverListener listener : listeners ) {
				try {
					listener.onReceiveData(this, data);
				} catch( Throwable t ) {
					LOG.error("Exeption in transmission listener", t);
				}
			}
		}
	}
	
	private void fireListeners( IPunctuation punc ) {
		synchronized( listeners ) {
			for( ITransmissionReceiverListener listener : listeners ) {
				try {
					listener.onReceivePunctuation(this, punc);
				} catch( Throwable t ) {
					LOG.error("Exeption in transmission listener", t);
				}
			}
		}
	}
	
	private void fireDoneEvent() {
		synchronized( listeners ) {
			for( ITransmissionReceiverListener listener : listeners ) {
				try {
					listener.onReceiveDone(this);
				} catch( Throwable t ) {
					LOG.error("Exeption in transmission listener", t);
				}
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
}
