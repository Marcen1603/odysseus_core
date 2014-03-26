package de.uniol.inf.is.odysseus.peer.logging;

import net.jxta.peer.PeerID;

import org.apache.log4j.Level;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;

public class LogMessageReceiver implements IPeerCommunicatorListener {

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, int messageID, byte[] message) {
		int logLevel = byteArrayToInt(message, 0);
			
		int msgLength = byteArrayToInt(message, 4); 
		byte[] msgBytes = new byte[msgLength];
		System.arraycopy(message, 8, msgBytes, 0, msgLength);
		String msg = new String(msgBytes);
		
		int loggerLength = byteArrayToInt(message, 8 + msgLength);
		byte[] moggerBytes = new byte[loggerLength];
		System.arraycopy(message, 8 + msgLength + 4, moggerBytes, 0, loggerLength);
		String logger = new String(moggerBytes);

		String peerName = determinePeerName(senderPeer);

		printPeerMessage(logLevel, peerName, logger, msg);
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

	public static int byteArrayToInt(byte[] b, int offset) {
		return b[3 + offset] & 0xFF | (b[2 + offset] & 0xFF) << 8 | (b[1 + offset] & 0xFF) << 16 | (b[0 + offset] & 0xFF) << 24;
	}
}
