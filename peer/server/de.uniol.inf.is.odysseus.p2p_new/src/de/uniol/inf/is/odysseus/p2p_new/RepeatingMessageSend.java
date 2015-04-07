package de.uniol.inf.is.odysseus.p2p_new;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.util.RepeatingJobThread;

public class RepeatingMessageSend extends RepeatingJobThread {

	private static final Logger LOG = LoggerFactory.getLogger(RepeatingMessageSend.class);
	private static final int WARNING_TIME_INTERVAL_MILLIS = 30 * 1000;
	private static final int ABORT_MILLIS = 60 * 1000;
	private static final int REPEAT_SEND_INTERVAL_MILLIS = 1000;
	
	private final IPeerCommunicator communicator;
	private final IMessage message;
	private final PeerID peerID;
	
	public IMessage getMessage() {
		return message;
	}

	public PeerID getPeerID() {
		return peerID;
	}

	private int sendingTime;
	private boolean warned;
	private boolean quietOnError;
	
	public RepeatingMessageSend( IPeerCommunicator communicator, IMessage message, PeerID destination ) {
		this(communicator, message, destination, false);
	}
	
	public RepeatingMessageSend( IPeerCommunicator communicator, IMessage message, PeerID destination, boolean quietOnError ) {
		super(REPEAT_SEND_INTERVAL_MILLIS, "Repeating message thread: " + message.getClass().getName());
		
		this.communicator = communicator;
		this.message = message;
		this.peerID = destination;
		this.quietOnError = quietOnError;
	}
	
	@Override
	public void doJob() {
		try {
			LOG.debug("Retrying sending message of type '{}'", message.getClass().getName());
			
			communicator.send(peerID, message);
			
			sendingTime += getIntervalMillis();
			if( !warned && sendingTime >= WARNING_TIME_INTERVAL_MILLIS ) {
				if( quietOnError ) {
					LOG.debug("RepeatingMessageThread runs more than " + WARNING_TIME_INTERVAL_MILLIS + " ms now! Message: {}", message.getClass().getName());
				} else {				
					LOG.error("RepeatingMessageThread runs more than " + WARNING_TIME_INTERVAL_MILLIS + " ms now! Message: {}", message.getClass().getName());
				}
				warned = true;
			} else if( sendingTime > ABORT_MILLIS ) {
				if( quietOnError ) {
					LOG.debug("RepeatingMessageThread runs now more than " + ABORT_MILLIS + " ms now. Abort sending message {}", message.getClass().getName());
				} else {
					LOG.error("RepeatingMessageThread runs now more than " + ABORT_MILLIS + " ms now. Abort sending message {}", message.getClass().getName());
				}
				stopRunning();
			}
			
		} catch (PeerCommunicationException e) {
			if( quietOnError ) {
				LOG.debug("Could not repeatedly send message of type {}", message.getClass().getName(), e);
			} else {
				LOG.error("Could not repeatedly send message of type {}", message.getClass().getName(), e);
			}
			stopRunning();
		}
	}
}
