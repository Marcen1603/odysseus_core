package de.uniol.inf.is.odysseus.peer.logging.impl;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.logging.JXTALoggingPlugIn;
import de.uniol.inf.is.odysseus.peer.logging.JxtaLoggingDestinations;

public class JXTAAppender extends AppenderSkeleton {

	private static final Logger LOG = LoggerFactory.getLogger(JXTAAppender.class);

	private IPeerCommunicator peerCommunicator;

	@Override
	public void close() {
		LOG.debug("Closing");
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent log) {
		if (peerCommunicator == null) {
			peerCommunicator = JXTALoggingPlugIn.getPeerCommunicator();
		}

		Collection<PeerID> logDestinations = JxtaLoggingDestinations.getDestinations();

		try {
			if (!logDestinations.isEmpty()) {

				LogMessage logMessage = new LogMessage(log);

				for (PeerID logDestination : logDestinations) {
					try {
						peerCommunicator.send(logDestination, logMessage);
					} catch (PeerCommunicationException e) {
					}
				}

			} 
		} catch (Throwable t) {
		}
	}

}
