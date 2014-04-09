package de.uniol.inf.is.odysseus.p2p_new;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class RepeatingMessageSend extends RepeatingJobThread {

	private static final Logger LOG = LoggerFactory.getLogger(RepeatingMessageSend.class);
	private static final int WARNING_TIME_INTERVAL_MILLIS = 30 * 1000;
	private static final int ABORT_MILLIS = 60 * 1000;
	private static final int REPEAT_SEND_INTERVAL_MILLIS = 500;
	
	private final IPeerCommunicator communicator;
	private final IMessage message;
	private final PeerID peerID;
	
	private int sendingTime;
	private boolean warned;
	
	public RepeatingMessageSend( IPeerCommunicator communicator, IMessage message, PeerID destination ) {
		super(REPEAT_SEND_INTERVAL_MILLIS, "Repeating message thread: " + message.getClass().getName());
		
		this.communicator = communicator;
		this.message = message;
		this.peerID = destination;
	}
	
	@Override
	public void doJob() {
		try {
			LOG.debug("Retrying sending message of type '{}'", message.getClass().getName());
			
			communicator.send(peerID, message);
			
			sendingTime += getIntervalMillis();
			if( !warned && sendingTime >= WARNING_TIME_INTERVAL_MILLIS ) {
				LOG.error("RepeatingMessageThread runs more than " + WARNING_TIME_INTERVAL_MILLIS + " ms now! Message: {}", message.getClass().getName());
				warned = true;
			} else if( sendingTime > ABORT_MILLIS ) {
				LOG.error("RepeatingMessageThread runs now more than " + ABORT_MILLIS + " ms now. Abort sending message {}", message.getClass().getName());
				stopRunning();
			}
			
		} catch (PeerCommunicationException e) {
			LOG.error("Could not repeatedly send message of type {}", message.getClass().getName(), e);
			stopRunning();
		}
	}
}
