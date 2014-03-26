package de.uniol.inf.is.odysseus.peer.logging;

import net.jxta.peer.PeerID;

import org.apache.log4j.Level;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;

public class LogMessageReceiver implements IPeerCommunicatorListener {

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		LogMessage logMessage = (LogMessage)message;
		
		String peerName = determinePeerName(senderPeer);

		printPeerMessage(logMessage.getLevel(), peerName, logMessage.getLoggerName(), logMessage.getText());
	}

	private static void printPeerMessage(int logLevel, String peerName, String loggerName, String messageText) {
		String levelStr = toLevel(logLevel);

		System.out.println(peerName + " [" + levelStr + "] " + truncToClass(loggerName) + " - " + messageText);
	}

	private static String truncToClass(String loggerName) {
		int pos = loggerName.lastIndexOf(".");
		return loggerName.substring(pos + 1);
	}

	private static String toLevel(int logLevel) {
		switch (logLevel) {
		case Level.TRACE_INT:
			return "TRACE";

		case Level.DEBUG_INT:
			return "DEBUG";

		case Level.ERROR_INT:
		case Level.FATAL_INT:
			return "ERROR";

		case Level.INFO_INT:
			return "INFO";

		case Level.WARN_INT:
			return "WARN";

		default:
			return "MSG";
		}
	}

	private static String determinePeerName(PeerID pid) {
		Optional<String> optPeerName = JXTALoggingPlugIn.getP2PDictionary().getRemotePeerName(pid);
		return optPeerName.isPresent() ? "<" + optPeerName.get() + ">" : "<unknown>";
	}
}
