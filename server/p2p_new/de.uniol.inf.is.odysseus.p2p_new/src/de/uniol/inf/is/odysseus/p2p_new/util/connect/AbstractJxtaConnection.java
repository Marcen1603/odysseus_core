package de.uniol.inf.is.odysseus.p2p_new.util.connect;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;

public abstract class AbstractJxtaConnection implements IJxtaConnection {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractJxtaConnection.class);
	private static final int MAX_SEND_WAITING_MILLIS = 15000;
	
	private final List<IJxtaConnectionListener> listeners = Lists.newArrayList();
	
	private boolean isConnected = false;
	private boolean isConnectFailed = false;

	@Override
	public void addListener(IJxtaConnectionListener listener) {
		Preconditions.checkNotNull(listener, "Listener to add must not be null!");
		
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(IJxtaConnectionListener listener) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	@Override
	public void connect() throws IOException {
		isConnected = true;

		fireConnectEvent();
	}

	@Override
	public final boolean isConnected() {
		return isConnected;
	}
	
	@Override
	public void disconnect() {
		isConnected = false;
		fireDisconnectEvent();
	}
	
	protected final void waitForConnect() throws IOException {
		if( isConnectFailed ) {
			throw new IOException("Could not send message since connecting failed before");
		}

		if( !isConnected ) {
			final long waitingTime = System.currentTimeMillis();
			while( !isConnected && !isConnectFailed) {
				try {
					if( System.currentTimeMillis() - waitingTime > MAX_SEND_WAITING_MILLIS ) {
						throw new IOException("Cannot send message. Waiting time is too long (connect failed?)");
					}
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}
			
		if( isConnectFailed ) {
			throw new IOException("Could not send message since connecting failed before");
		}		
	}
	
	protected final void setConnectFail() {
		isConnectFailed = true;
	}
	
	protected final void fireDisconnectEvent() {
		synchronized( listeners ) {
			for( IJxtaConnectionListener listener : listeners.toArray(new IJxtaConnectionListener[0]) ) {
				try {
					listener.onDisconnect(this);
				} catch( Throwable t ) {
					LOG.error("Exception during invoking jxta connection listener", t);
				}
			}
		}
	}
	
	protected final void fireConnectEvent() {
		synchronized( listeners ) {
			for( IJxtaConnectionListener listener : listeners.toArray(new IJxtaConnectionListener[0]) ) {
				try {
					listener.onConnect(this);
				} catch( Throwable t ) {
					LOG.error("Exception during invoking jxta connection listener", t);
				}
			}
		}
	}

	protected final void fireMessageReceiveEvent(byte[] data) {
		synchronized( listeners ) {
			for (final IJxtaConnectionListener listener : listeners.toArray(new IJxtaConnectionListener[0])) {
				try {
					listener.onReceiveData(this, data);
				} catch (final Throwable t) {
					LOG.error("Exception in JxtaConnection listener", t);
				}
			}
		}
	}
}
