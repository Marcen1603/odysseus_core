package de.uniol.inf.is.odysseus.peer.logging;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JXTAAppender extends AppenderSkeleton {

	private static final Logger LOG = LoggerFactory.getLogger(JXTAAppender.class);
	
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
	}

}
