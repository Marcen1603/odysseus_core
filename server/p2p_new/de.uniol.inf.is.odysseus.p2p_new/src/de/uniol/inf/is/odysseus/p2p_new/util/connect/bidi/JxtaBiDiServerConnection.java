package de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi;

import java.io.IOException;
import java.util.List;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;
import net.jxta.util.JxtaServerPipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.AbstractJxtaServerConnection;

public class JxtaBiDiServerConnection extends AbstractJxtaServerConnection implements IJxtaConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaBiDiServerConnection.class);
	
	private final List<IJxtaConnection> activeConnections = Lists.newArrayList();
	private final PipeAdvertisement pipeAdvertisement;
	
	private JxtaServerPipe serverPipe;
	private RepeatingJobThread accepterThread;
	
	public JxtaBiDiServerConnection( PipeAdvertisement pipeAdvertisement ) {
		Preconditions.checkNotNull(pipeAdvertisement, "PipeAdvertisement for JxtaBiDiServerConnection must not be null");
		
		this.pipeAdvertisement = pipeAdvertisement;
	}
	
	@Override
	public void start() throws IOException {
		Preconditions.checkState(accepterThread == null, "JxtaServerConnection is already running!");
		
		LOG.debug("Starting jxta bibi server connection");
		
		serverPipe = new JxtaServerPipe(P2PNetworkManager.getInstance().getLocalPeerGroup(), pipeAdvertisement);
		serverPipe.setPipeTimeout(0);

		accepterThread = new RepeatingJobThread() {
			@Override
			public void doJob() {
				try {
					LOG.debug("Waiting to accept pipe from client...");
					JxtaBiDiPipe pipeToClient = serverPipe.accept();
					
					LOG.debug("Accepted new pipe to client");
					
					JxtaBiDiConnection connection = new JxtaBiDiConnection(pipeToClient);
					connection.addListener(JxtaBiDiServerConnection.this);
					connection.connect();
					
					LOG.debug("Got new connection!");
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
		LOG.debug("Stopping jxta bidi server connection");
		
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
		LOG.debug("Lost one connection");
		
		activeConnections.remove(sender);
		fireConnectionRemoveEvent(sender);
	}
	
	@Override
	public void onConnect(IJxtaConnection sender) {
		// do nothing
	}
}
