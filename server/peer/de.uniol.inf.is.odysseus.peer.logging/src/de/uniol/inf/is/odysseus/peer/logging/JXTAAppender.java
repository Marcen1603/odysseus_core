package de.uniol.inf.is.odysseus.peer.logging;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;

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

		Collection<PeerID> logDestinations = LoggingDestinations.getInstance().getDestinations();

		try {
			if (!logDestinations.isEmpty()) {

				byte[] message = buildMessage(log);

				for (PeerID logDestination : logDestinations) {
					try {
						peerCommunicator.send(logDestination, JXTALoggingPlugIn.LOG_BYTE, message);
					} catch (PeerCommunicationException e) {
					}
				}

			} 
		} catch (Throwable t) {
		}
	}

	private static byte[] buildMessage(LoggingEvent log) {
		String strMessage = (String)log.getMessage();
		String loggerName = log.getLoggerName();
		
		byte[] message = new byte[ 4 + 4 + strMessage.length() + 4 + loggerName.length() ];
		
		insertInt(message, 0, log.getLevel().toInt());
		
		insertInt(message, 4, strMessage.length());
		System.arraycopy(strMessage.getBytes(), 0, message, 8, strMessage.length());
		
		insertInt(message, 8 + strMessage.length(), loggerName.length());
		System.arraycopy(loggerName.getBytes(), 0, message, 8 + strMessage.length() + 4, loggerName.length());
		
		return message;
	}
	
	public static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}
}
