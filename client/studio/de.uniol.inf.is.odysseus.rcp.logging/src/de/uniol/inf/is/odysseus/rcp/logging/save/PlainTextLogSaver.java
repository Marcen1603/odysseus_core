package de.uniol.inf.is.odysseus.rcp.logging.save;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.uniol.inf.is.odysseus.rcp.logging.ILogSaver;
import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;

public class PlainTextLogSaver implements ILogSaver {

	public static final String SAVER_NAME = "Plain";
	public static final String FILE_EXTENSION = "txt";

	@Override
	public final void save(String filename, List<RCPLogEntry> entriesToSave) throws IOException {
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			for( RCPLogEntry entry : entriesToSave ) {
				writer.write(convertToLine(entry));
				writer.write("\n");
			}
			
		} finally {
			if( writer != null ) {
				writer.close();
			}
		}
	}

	protected String convertToLine(RCPLogEntry entry) {
		StringBuilder sb = new StringBuilder();
		sb.append(entry.getTimestamp()).append(" ").append(entry.getLevel().toString()).append(" ").append(entry.getLoggerName()).append(": ").append(entry.getMessage());
		if( entry.hasThrowable() ) {
			sb.append(determineThrowableText(entry.getThrowable()));
		}
		return sb.toString();
	}
	
	protected final String determineThrowableText( Throwable t ) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n").append(t.getClass().getName()).append(": ").append(t.getMessage());
		sb.append(getStackTraceText(t));
		return sb.toString();
	}

	@Override
	public String getName() {
		return SAVER_NAME;
	}

	@Override
	public String getFilenameExtension() {
		return FILE_EXTENSION;
	}

	protected String getStackTraceText(Throwable ex) {
		StackTraceElement[] stack = ex.getStackTrace();

		StringBuilder sb = new StringBuilder();

		for (StackTraceElement s : stack) {
			sb.append("\tat ");
			sb.append(s);
			sb.append("\n");
		}

		// cause
		Throwable cause = ex.getCause();
		if (cause != null) {
			printStackTraceAsCause(cause, sb, stack);
		}

		return sb.toString();
	}

	private void printStackTraceAsCause(Throwable ex, StringBuilder sb, StackTraceElement[] causedTrace) {
		StackTraceElement[] trace = ex.getStackTrace();
		int m = trace.length - 1, n = causedTrace.length - 1;
		while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
			m--;
			n--;
		}

		sb.append("Caused by: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
		sb.append("\n");
		for (int i = 0; i <= m; i++)
			sb.append("\tat " + trace[i]).append("\n");
		
		int framesInCommon = trace.length - 1 - m;
		if (framesInCommon != 0)
			sb.append("\t... " + framesInCommon + " more").append("\n");

		// Recurse if we have a cause
		Throwable ourCause = ex.getCause();
		if (ourCause != null) {
			printStackTraceAsCause(ourCause, sb, trace);
		}
	}
}
