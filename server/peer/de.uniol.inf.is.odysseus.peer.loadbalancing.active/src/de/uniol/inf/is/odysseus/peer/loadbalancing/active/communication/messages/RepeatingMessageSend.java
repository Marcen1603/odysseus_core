package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class RepeatingMessageSend extends RepeatingJobThread {
	
	ArrayList<IMessageDeliveryFailedListener> listeners;

	private static final Logger LOG = LoggerFactory.getLogger(RepeatingMessageSend.class);
	private static final int WARNING_TIME_INTERVAL_MILLIS = 30 * 1000;
	private static final int ABORT_MILLIS = 60 * 1000;
	private static final int REPEAT_SEND_INTERVAL_MILLIS = 1000;
	
	private final IPeerCommunicator communicator;
	private final IMessage message;
	private final PeerID peerID;
	
	private int sendingTime;
	private boolean warned;
	
	public RepeatingMessageSend( IPeerCommunicator communicator, IMessage message, PeerID destination) {
		super(REPEAT_SEND_INTERVAL_MILLIS, "Repeating message thread: " + message.getClass().getName());
		
		this.communicator = communicator;
		this.message = message;
		this.peerID = destination;
		this.listeners = new ArrayList<IMessageDeliveryFailedListener>();
	}
	
	public void addListener(IMessageDeliveryFailedListener listener) {
		this.listeners.add(listener);
	}
	
	public void clearListeners() {
		this.listeners.clear();
	}
	
	public void notifyListeners() {
		LOG.error("Notifying listeners");
		for(IMessageDeliveryFailedListener listener : listeners) {
			listener.update(message,peerID);
		}
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
				notifyListeners();
				stopRunning();
				
			}
			
		} catch (PeerCommunicationException e) {
			LOG.error("Could not repeatedly send message of type {}", message.getClass().getName(), e);
			notifyListeners();
			stopRunning();
			
		}
	}
	
}
