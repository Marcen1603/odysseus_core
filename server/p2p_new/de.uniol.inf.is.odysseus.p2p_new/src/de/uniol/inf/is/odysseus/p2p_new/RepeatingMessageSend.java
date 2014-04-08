package de.uniol.inf.is.odysseus.p2p_new;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class RepeatingMessageSend extends RepeatingJobThread {

	private static final Logger LOG = LoggerFactory.getLogger(RepeatingMessageSend.class);
	
	private final IPeerCommunicator communicator;
	private final IMessage message;
	private final PeerID peerID;
	
	public RepeatingMessageSend( IPeerCommunicator communicator, IMessage message, PeerID destination ) {
		super(500, "Repeating message thread: " + message.getClass().getName());
		
		this.communicator = communicator;
		this.message = message;
		this.peerID = destination;
	}
	
	@Override
	public void doJob() {
		try {
			LOG.debug("Retrying sending message of type '{}'", message.getClass().getName());
			
			communicator.send(peerID, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not repeatedly send message");
			stopRunning();
		}
	}
}
