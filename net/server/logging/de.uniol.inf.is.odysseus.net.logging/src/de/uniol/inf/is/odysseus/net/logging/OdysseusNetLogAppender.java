package de.uniol.inf.is.odysseus.net.logging;

import java.util.Collection;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;

public class OdysseusNetLogAppender extends AppenderSkeleton {

	private final IOdysseusNodeCommunicator communicator;

	public OdysseusNetLogAppender(IOdysseusNodeCommunicator communicator) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");

		this.communicator = communicator;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent log) {
		Collection<IOdysseusNode> logDestinations = LoggingDestinations.getDestinations();

		try {
			if (!logDestinations.isEmpty()) {
				LogMessage logMessage = new LogMessage(log);
//				System.err.println("Sending log message: " + log);
				
				for (IOdysseusNode logDestination : logDestinations) {
					try {
						communicator.send(logDestination, logMessage);
					} catch (OdysseusNodeCommunicationException e) {
//						System.err.println("Could not send log message to node " + logDestination);
//						System.err.println(e);
					}
				}

			}
		} catch (Throwable t) {
		}
	}

}
