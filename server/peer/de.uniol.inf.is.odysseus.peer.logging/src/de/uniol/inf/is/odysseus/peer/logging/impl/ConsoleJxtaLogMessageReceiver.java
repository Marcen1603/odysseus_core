package de.uniol.inf.is.odysseus.peer.logging.impl;

import org.apache.log4j.Level;

import com.google.common.base.Optional;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.logging.IJxtaLogMessageReceiver;
import de.uniol.inf.is.odysseus.peer.logging.JXTALoggingPlugIn;

public class ConsoleJxtaLogMessageReceiver implements IJxtaLogMessageReceiver {

	@Override
	public void logMessage(PeerID senderPeerID, int logLevel, String loggerName, String text) {
		String peerName = determinePeerName(senderPeerID);

		printPeerMessage(logLevel, peerName, loggerName, text);
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
