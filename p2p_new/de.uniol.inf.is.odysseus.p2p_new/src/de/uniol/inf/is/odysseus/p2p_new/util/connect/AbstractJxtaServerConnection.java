package de.uniol.inf.is.odysseus.p2p_new.util.connect;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnectionListener;

public abstract class AbstractJxtaServerConnection implements IJxtaServerConnection {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractJxtaServerConnection.class);
	
	private final List<IJxtaServerConnectionListener> listeners = Lists.newArrayList();

	@Override
	public void addListener(IJxtaServerConnectionListener listener) {
		Preconditions.checkNotNull(listener, "Listener to add to jxta server connection must not be null!");
		
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(IJxtaServerConnectionListener listener) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	protected final void fireConnectionAddEvent(IJxtaConnection connection) {
		synchronized( listeners ) {
			for( IJxtaServerConnectionListener listener : listeners.toArray(new IJxtaServerConnectionListener[0]) ) {
				try {
					listener.connectionAdded(this, connection);
				} catch( Throwable t ) {
					LOG.error("Exception during invoking jxta server connection listener", t);
				}
			}
		}
	}
	
	protected final void fireConnectionRemoveEvent(IJxtaConnection connection) {
		synchronized( listeners ) {
			for( IJxtaServerConnectionListener listener : listeners.toArray(new IJxtaServerConnectionListener[0]) ) {
				try {
					listener.connectionRemoved(this, connection);
				} catch( Throwable t ) {
					LOG.error("Exception during invoking jxta server connection listener", t);
				}
			}
		}
	}
}
