package de.uniol.inf.is.odysseus.peer.logging;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class JXTAAppender extends AppenderSkeleton {

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent log) {
	}

}
