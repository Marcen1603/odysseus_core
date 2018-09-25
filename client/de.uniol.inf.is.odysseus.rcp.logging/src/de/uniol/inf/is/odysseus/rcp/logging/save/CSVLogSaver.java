package de.uniol.inf.is.odysseus.rcp.logging.save;

import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;

public class CSVLogSaver extends PlainTextLogSaver {

	public static final String SAVER_NAME = "CSV";
	public static final String FILENAME_EXTENSION = "csv";

	@Override
	protected String convertToLine(RCPLogEntry entry) {
		StringBuilder sb = new StringBuilder();

		sb.append(entry.getTimestamp()).append(",");
		sb.append(entry.getLevel().toString()).append(",");
		sb.append(entry.getLoggerName()).append(",");
		sb.append(entry.getClassName()).append(",");
		sb.append(entry.getMethodName()).append(",");
		sb.append(entry.getLineNumber()).append(",");
		sb.append(entry.getThreadName()).append(",");
		sb.append(getMiniMessage(entry.getMessage()));
		if( entry.hasThrowable() ) {
			sb.append(",");
			sb.append(entry.getThrowable().getClass()).append(",");
			sb.append(getMiniMessage(entry.getThrowable().getMessage()));
		}
		
		return sb.toString();
	}
	
	private static String getMiniMessage(String message) {
		int pos = message.indexOf("\n");
		return pos == -1 ? message : message.substring(0, pos) + " [...]";
	}
	
	@Override
	public String getFilenameExtension() {
		return FILENAME_EXTENSION;
	}
	
	@Override
	public String getName() {
		return SAVER_NAME;
	}
}
