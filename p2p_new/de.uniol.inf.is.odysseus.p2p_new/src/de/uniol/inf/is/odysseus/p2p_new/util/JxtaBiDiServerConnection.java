package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;
import net.jxta.util.JxtaServerPipe;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class JxtaBiDiServerConnection implements IJxtaServerConnection, IJxtaConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaBiDiServerConnection.class);
	
	private final List<IJxtaServerConnectionListener> listeners = Lists.newArrayList();
	private final List<IJxtaConnection> activeConnections = Lists.newArrayList();
	private final PipeAdvertisement pipeAdvertisement;
	
	private JxtaServerPipe serverPipe;
	private RepeatingJobThread accepterThread;
	
	public JxtaBiDiServerConnection( PipeAdvertisement pipeAdvertisement ) {
		Preconditions.checkNotNull(pipeAdvertisement, "PipeAdvertisement for JxtaBiDiServerConnection must not be null");
		
		this.pipeAdvertisement = pipeAdvertisement;
	}
	
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

	@Override
	public void start() throws IOException {
		Preconditions.checkState(accepterThread == null, "JxtaServerConnection is already running!");
				
		serverPipe = new JxtaServerPipe(P2PNewPlugIn.getOwnPeerGroup(), getPipeAdvertisement());
		serverPipe.setPipeTimeout(0);

		accepterThread = new RepeatingJobThread() {
			@Override
			public void doJob() {
				try {
					JxtaBiDiPipe pipeToClient = serverPipe.accept();
					
					JxtaBiDiConnection connection = new JxtaBiDiConnection(pipeToClient);
					connection.addListener(JxtaBiDiServerConnection.this);
					
					activeConnections.add(connection);
					fireConnectionAddEvent(connection);
					
				} catch (IOException e) {
					LOG.error("Could not accept a client JxtaBiDiConnection", e);
				}
			}
		};
		accepterThread.start();
	}

	@Override
	public void stop() {
		if( accepterThread != null ) {
			accepterThread.stopRunning();
			accepterThread.interrupt();
			accepterThread = null;
		}
		
		for( IJxtaConnection connection : activeConnections.toArray(new IJxtaConnection[0])) {
			connection.disconnect();
		}
	}

	@Override
	public boolean isStarted() {
		return accepterThread != null;
	}

	@Override
	public ImmutableList<IJxtaConnection> getConnections() {
		return ImmutableList.copyOf(activeConnections);
	}

	@Override
	public PipeAdvertisement getPipeAdvertisement() {
		return pipeAdvertisement;
	}

	@Override
	public void onReceiveData(IJxtaConnection sender, byte[] data) {
		// do nothing
	}

	@Override
	public void onDisconnect(IJxtaConnection sender) {
		activeConnections.remove(sender);
		fireConnectionRemoveEvent(sender);
	}
	
	protected final void fireConnectionAddEvent(IJxtaConnection connection) {
		synchronized( listeners ) {
			for( IJxtaServerConnectionListener listener : listeners ) {
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
			for( IJxtaServerConnectionListener listener : listeners ) {
				try {
					listener.connectionRemoved(this, connection);
				} catch( Throwable t ) {
					LOG.error("Exception during invoking jxta server connection listener", t);
				}
			}
		}
	}
}
