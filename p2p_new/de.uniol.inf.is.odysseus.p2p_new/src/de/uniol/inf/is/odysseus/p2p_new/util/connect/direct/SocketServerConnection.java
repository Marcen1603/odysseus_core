package de.uniol.inf.is.odysseus.p2p_new.util.connect.direct;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class SocketServerConnection implements IJxtaServerConnection, IJxtaConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(SocketServerConnection.class);
	
	private final List<IJxtaServerConnectionListener> listeners = Lists.newArrayList();
	private final List<IJxtaConnection> connections = Lists.newArrayList();
	private final PipeAdvertisement pipeAdvertisement;
	
	private boolean started = false;
	private RepeatingJobThread accepterThread;
	private ServerSocket serverSocket;
	
	public SocketServerConnection(PipeAdvertisement pipeAdvertisement ) {
		Preconditions.checkNotNull(pipeAdvertisement, "Pipe Advertisement must not be null!");
		
		this.pipeAdvertisement = pipeAdvertisement;
	}
	
	@Override
	public void addListener(IJxtaServerConnectionListener listener) {
		Preconditions.checkNotNull(listener, "Listener to add must not be null!");
		
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
		Preconditions.checkState(isStarted() == false, "Server socket already started");
		
		started = true;
		
		serverSocket = new ServerSocket(0);
		serverSocket.setSoTimeout(0);
		accepterThread = new RepeatingJobThread() {
			@Override
			public void doJob() {
				try {
					Socket clientSocket = serverSocket.accept();
					
					IJxtaConnection connection = new SocketConnection(clientSocket);
					connection.addListener(SocketServerConnection.this);
					
					connection.connect();
					fireConnectionAddEvent(connection); // TODO: hete insert
					
				} catch (IOException e) {
					LOG.error("Could not accept connections", e);
				}
			};
		};
		accepterThread.start();
	}
	
	public final int getPort() {
		return serverSocket.getLocalPort();
	}

	@Override
	public void stop() {
		Preconditions.checkState(isStarted() == true, "Server socket is already stopped");
		
		if( accepterThread != null ) {
			accepterThread.stopRunning();
			accepterThread.interrupt();
			accepterThread = null;
		}
		
		for( IJxtaConnection connection : connections ) {
			connection.disconnect();
		}
		connections.clear();
		
		started = false;
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public ImmutableList<IJxtaConnection> getConnections() {
		return ImmutableList.copyOf(connections);
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
		connections.remove(sender);
		fireConnectionRemoveEvent(sender);
	}

	@Override
	public void onConnect(IJxtaConnection sender) {
		// do nothing
	}
	
	protected final void fireConnectionAddEvent(IJxtaConnection connection) {
		synchronized( listeners ) {
			for( IJxtaServerConnectionListener listener : listeners ) {
				try {
					listener.connectionAdded(this, connection);
				} catch( Throwable t ) {
					LOG.error("Exception during invoking server socket connection listener", t);
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
					LOG.error("Exception during invoking server socket connection listener", t);
				}
			}
		}
	}
}
