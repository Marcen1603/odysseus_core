package de.uniol.inf.is.odysseus.net.connect;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public abstract class AbstractOdysseusNodeConnection implements IOdysseusNodeConnection {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractOdysseusNodeConnection.class);
	
	private final Collection<IOdysseusNodeConnectionListener> listeners = Lists.newArrayList();
	
	@Override
	public final void addListener(IOdysseusNodeConnectionListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized( listeners ) {
			listeners.add(listener);
		}
	}

	@Override
	public final void removeListener(IOdysseusNodeConnectionListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	protected final void fireMessageReceivedEvent(byte[] data) {
		synchronized( listeners ) {
			for( IOdysseusNodeConnectionListener listener : listeners ) {
				try {
					listener.messageReceived(this, data);
				} catch( Throwable t ) {
					LOG.error("Exception in odysseus node connection listener", t);
				}
			}
		}
	}

}
