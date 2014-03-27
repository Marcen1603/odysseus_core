package de.uniol.inf.is.odysseus.peer.logging.impl;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.logging.IJxtaLogMessageReceiver;

public class LogMessageReceiver implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(LogMessageReceiver.class);
	
	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		LogMessage logMessage = (LogMessage)message;
		
		for( IJxtaLogMessageReceiver receiver : JxtaLogMessageReceiverRegistry.getReceivers() ) {
			try {
				receiver.logMessage(senderPeer, logMessage.getLevel(), logMessage.getLoggerName(), logMessage.getText());
			} catch( Throwable t ) {
				LOG.debug("Exception during logging jxta message");
			}
		}
	}
}
