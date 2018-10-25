package de.uniol.inf.is.odysseus.rcp.logging.container;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.spi.LoggingEvent;

import java.util.Objects;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;

public class RCPLogContainer {

	private static final int MAX_ENTRIES = 5000;

	private final Collection<IRCPLogContainerListener> listeners = Lists.newArrayList();
	private final List<RCPLogEntry> logEntries = new CopyOnWriteArrayList<>();

	public void add( LoggingEvent event ) {
		Objects.requireNonNull(event, "Event to log in rcp container must not be null!");

		RCPLogEntry newEntry = new RCPLogEntry(event);
		RCPLogEntry oldEntry = null;
		synchronized( logEntries ) {
			logEntries.add(newEntry);

			if( logEntries.size() == MAX_ENTRIES + 1) {
				oldEntry = logEntries.remove(0); // removes oldest one
			}
		}

		fireAddLogEvent(newEntry);

		if( oldEntry != null ) {
			fireRemoveLogEvent(oldEntry);
		}
	}

	public List<RCPLogEntry> getLogEntriesInstance() {
		return logEntries;
	}

	public void addListener( IRCPLogContainerListener listener ) {
		Objects.requireNonNull(listener, "RCPContainer listener must not be null!");

		synchronized( listeners ) {
			listeners.add(listener);
		}
	}

	public void removeListener( IRCPLogContainerListener listener ) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}

	protected final void fireAddLogEvent(RCPLogEntry entry) {
		synchronized( listeners ) {
			for( IRCPLogContainerListener listener : listeners ) {
				try {
					listener.logAdded(this, entry);
				} catch( Throwable t ) {
					// needs to be sysout to avoid circular invocations!
					t.printStackTrace();
				}
			}
		}
	}

	protected final void fireRemoveLogEvent(RCPLogEntry entry) {
		synchronized( listeners ) {
			for( IRCPLogContainerListener listener : listeners ) {
				try {
					listener.logRemoved(this, entry);
				} catch( Throwable t ) {
					// needs to be sysout to avoid circular invocations!
					t.printStackTrace();
				}
			}
		}
	}

	public void clear() {
		synchronized( logEntries ) {
			logEntries.clear();
		}
	}
}
