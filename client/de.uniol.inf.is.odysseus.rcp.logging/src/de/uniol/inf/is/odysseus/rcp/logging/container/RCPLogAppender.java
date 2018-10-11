package de.uniol.inf.is.odysseus.rcp.logging.container;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.google.common.base.Preconditions;

public class RCPLogAppender extends AppenderSkeleton {

	private final RCPLogContainer containerToFill;
	
	public RCPLogAppender( RCPLogContainer containerToFill ) {
		// Preconditions.checkNotNull(containerToFill, "RCPContainer to fill with rcp logs must not be null!");
		
		this.containerToFill = containerToFill;
	}
	
	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {
		try {
			containerToFill.add(event);
		} catch( Throwable t ) {
			// make sure that this does not break other code somewhere
			System.err.print("Could not log logging event: ");
			t.printStackTrace(System.err);
		}
	}
	
}
