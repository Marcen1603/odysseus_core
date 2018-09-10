package de.uniol.inf.is.odysseus.rcp.logging;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public final class RCPLogEntry implements Comparable<RCPLogEntry> {

	private static final int DEFAULT_LINE_NUMBER = -1;
	private static final String NULL_MESSAGE = "null";

	private final String loggerName;
	private final Level level;
	private final String message;
	
	private final String className; 
	private final String simpleClassName;
	private final String methodName;
	private final int lineNumber;
	
	private final String threadName;
	private final long timestamp;
	
	private final Throwable throwable;
	private final String throwableString;

	public RCPLogEntry( LoggingEvent event ) {
		Preconditions.checkNotNull(event, "Logging event to create rcp log entry must not be null!");
		
		loggerName = event.getLoggerName();
		level = event.getLevel();
		
		message = toMessageString(event.getMessage());
		LocationInfo locInfo = event.getLocationInformation();
		
		className = locInfo.getClassName();
		simpleClassName = toSimpleName(className);
		methodName = locInfo.getMethodName();
		lineNumber = toLineNumber(locInfo.getLineNumber());
		
		threadName = event.getThreadName();
		timestamp = event.getTimeStamp();
		
		ThrowableInformation throwableInformation = event.getThrowableInformation();
		if( throwableInformation != null ) {
			throwableString = toString(throwableInformation.getThrowableStrRep());
			throwable = throwableInformation.getThrowable();
		} else {
			throwableString = "";
			throwable = null;
		}
	}
	
	private static String toString(String[] throwableStrRep) {
		if( throwableStrRep == null || throwableStrRep.length == 0 ) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		for( String str : throwableStrRep ) {
			sb.append(str).append("\n");
		}
		return sb.toString();
	}

	private static String toSimpleName(String className) {
		int pos = className.lastIndexOf(".");
		return pos != -1 ? className.substring(pos + 1) : className;
	}

	private static int toLineNumber(String lineNumberString) {
		if( !Strings.isNullOrEmpty(lineNumberString)) {
			try {
				return Integer.valueOf(lineNumberString);
			} catch( Throwable t ) {
			}
		}
		return DEFAULT_LINE_NUMBER;
	}

	private static String toMessageString(Object msg) {
		if( msg == null ) {
			return NULL_MESSAGE;
		}
		
		if( msg instanceof String) {
			return (String)msg;
		}
		return msg.toString();
	}

	public String getLoggerName() {
		return loggerName;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getSimpleClassName() {
		return simpleClassName;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public String getThreadName() {
		return threadName;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{").append(timestamp).append(" - ").append(loggerName).append("[").append(lineNumber).append("]:").append(message).append("}");
		
		return sb.toString();
	}

	@Override
	public int compareTo(RCPLogEntry o) {
		return Long.compare(timestamp, o.timestamp);
	}
	
	public Throwable getThrowable() {
		return throwable;
	}
	
	public boolean hasThrowable() {
		return throwable != null;
	}
	
	public String getThrowableString() {
		return throwableString;
	}
}
