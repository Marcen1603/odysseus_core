package de.uniol.inf.is.odysseus.net.logging;

import org.apache.log4j.spi.LoggingEvent;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class LogMessage implements IMessage {

	private int level;
	private String loggerName;
	private String text;
	
	public LogMessage() {
	}
	
	public LogMessage( LoggingEvent logEvent ) {
		level = logEvent.getLevel().toInt();
		loggerName = logEvent.getLoggerName();
		text = (String)logEvent.getMessage();
	}
	
	@Override
	public byte[] toBytes() {
		byte[] message = new byte[ 4 + 4 + text.length() + 4 + loggerName.length() ];
		
		insertInt(message, 0, level);
		
		insertInt(message, 4, text.length());
		System.arraycopy(text.getBytes(), 0, message, 8, text.length());
		
		insertInt(message, 8 + text.length(), loggerName.length());
		System.arraycopy(loggerName.getBytes(), 0, message, 8 + text.length() + 4, loggerName.length());
		
		return message;
	}

	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}
	
	@Override
	public void fromBytes(byte[] data) {
		level = byteArrayToInt(data, 0);
		
		int msgLength = byteArrayToInt(data, 4); 
		byte[] msgBytes = new byte[msgLength];
		System.arraycopy(data, 8, msgBytes, 0, msgLength);
		text = new String(msgBytes);
		
		int loggerLength = byteArrayToInt(data, 8 + msgLength);
		byte[] moggerBytes = new byte[loggerLength];
		System.arraycopy(data, 8 + msgLength + 4, moggerBytes, 0, loggerLength);
		loggerName = new String(moggerBytes);
	}

	private static int byteArrayToInt(byte[] b, int offset) {
		return b[3 + offset] & 0xFF | (b[2 + offset] & 0xFF) << 8 | (b[1 + offset] & 0xFF) << 16 | (b[0 + offset] & 0xFF) << 24;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getLoggerName() {
		return loggerName;
	}
	
	public String getText() {
		return text;
	}
}
