package de.uniol.inf.is.odysseus.net.logging;

import java.util.Map;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;

public class LogMessageListener implements IOdysseusNodeCommunicatorListener {

	private final Map<IOdysseusNode, Logger> nodeLoggers = Maps.newHashMap();
	private final IOdysseusNodeCommunicator communicator;
	
	public LogMessageListener(IOdysseusNodeCommunicator communicator) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");

		this.communicator = communicator;
		
		this.communicator.addListener(this, LogMessage.class);
	}
	
	public void dispose() {
		this.communicator.removeListener(this, LogMessage.class);
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if( message instanceof LogMessage ) {
//			System.err.println("Received log message");
			
			LogMessage msg = (LogMessage)message;
		
			Logger nodeLogger = nodeLoggers.get(senderNode);
			if( nodeLogger == null ) {
				nodeLogger = LoggerFactory.getLogger("OdysseusNet.<<<"+senderNode.getName()+">>>");
				nodeLoggers.put(senderNode, nodeLogger);
			}
			
			printLogMessage(nodeLogger, msg);
		} else {
//			System.err.println("Unknown message type: " + message);
		}
	}
	
	private static void printLogMessage(Logger nodeLogger, LogMessage msg) {
		int level = msg.getLevel();
		if(level == Level.TRACE_INT && nodeLogger.isTraceEnabled()) {
			nodeLogger.trace(toMessageText(msg));
		} else if( level == Level.DEBUG_INT && nodeLogger.isDebugEnabled() ) {
			nodeLogger.debug(toMessageText(msg));
		} else if( level == Level.ERROR_INT && nodeLogger.isErrorEnabled()) {
			nodeLogger.error(toMessageText(msg));
		} else if( level == Level.FATAL_INT && nodeLogger.isErrorEnabled()) {
			nodeLogger.error(toMessageText(msg));
		} else if( level == Level.INFO_INT && nodeLogger.isInfoEnabled()) {
			nodeLogger.info(toMessageText(msg));
		} else if( level == Level.WARN_INT && nodeLogger.isWarnEnabled()) {
			nodeLogger.warn(toMessageText(msg));
		} 
	}

	private static String toMessageText(LogMessage msg) {
		return "(" + truncToClass(msg.getLoggerName()) + ") " + msg.getText();
	}

	private static String truncToClass(String loggerName) {
		int pos = loggerName.lastIndexOf(".");
		return loggerName.substring(pos + 1);
	}
}
