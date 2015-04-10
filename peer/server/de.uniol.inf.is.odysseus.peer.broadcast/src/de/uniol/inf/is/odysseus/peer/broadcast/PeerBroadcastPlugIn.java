package de.uniol.inf.is.odysseus.peer.broadcast;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.peer.broadcast.impl.BroadcastRequestListener;
import de.uniol.inf.is.odysseus.peer.broadcast.impl.BroadcastRequestSender;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class PeerBroadcastPlugIn implements BundleActivator {
	
	private static final int BROADCAST_INTERVAL = 20000;

	private static IP2PNetworkManager networkManager;

	private BroadcastRequestListener listener;
	private BroadcastRequestSender broadcastSender;

	// called by OSGi
	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		networkManager = serv;
		
		startBroadcast();
	}
	
	// called by OSGi
	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (serv == networkManager) {
			
			stopBroadcast();
			
			serv = null;
		}
	}

	private void startBroadcast() {
		listener = new BroadcastRequestListener(networkManager);
		Thread t = new Thread(listener);
		t.setName("Peer Broadcast listener");
		t.setDaemon(true);
		t.start();
		
		broadcastSender = new BroadcastRequestSender(BROADCAST_INTERVAL, networkManager);
		broadcastSender.start();
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	private void stopBroadcast() {
		broadcastSender.stopRunning();
		listener.stopListener();
	}
}
