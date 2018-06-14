package de.uniol.inf.is.odysseus.net.connect;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public abstract class AbstractOdysseusNodeConnection implements IOdysseusNodeConnection {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractOdysseusNodeConnection.class);

	private Collection<IOdysseusNodeConnectionListener> listeners = Lists.newArrayList();
	private Collection<IOdysseusNodeConnectionListener> listenersBuffer = Lists.newArrayList();
	private boolean modifiedBuffer = false;

	@Override
	public final void addListener(IOdysseusNodeConnectionListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized (listenersBuffer) {
			listenersBuffer.add(listener);
			modifiedBuffer = true;
		}
	}

	@Override
	public final void removeListener(IOdysseusNodeConnectionListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized (listenersBuffer) {
			listenersBuffer.remove(listener);
			modifiedBuffer = false;
		}
	}

	protected final void fireMessageReceivedEvent(byte[] data) {
		synchronized (listeners) {
			updateListenersList();
			for (IOdysseusNodeConnectionListener listener : listeners) {
				try {
					listener.messageReceived(this, data);
				} catch (Throwable t) {
					LOG.error("Exception in odysseus node connection listener", t);
				}
			}
		}
	}

	protected final void fireDisconnectedEvent() {
		synchronized (listeners) {
			updateListenersList();

			for (IOdysseusNodeConnectionListener listener : listeners) {
				try {
					listener.disconnected(this);
				} catch (Throwable t) {
					LOG.error("Exception in odysseus node connection listener", t);
				}
			}
		}
	}

	private void updateListenersList() {
		synchronized (listenersBuffer) {
			if (modifiedBuffer) {
				Collection<IOdysseusNodeConnectionListener> temp = listeners;
				listeners = listenersBuffer;
				listenersBuffer = temp;
				modifiedBuffer = false;
			}
		}
	}
}
