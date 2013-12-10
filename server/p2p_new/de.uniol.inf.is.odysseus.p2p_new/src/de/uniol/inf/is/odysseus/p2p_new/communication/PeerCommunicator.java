package de.uniol.inf.is.odysseus.p2p_new.communication;

import java.io.IOException;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionaryAdapter;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi.JxtaBiDiServerConnection;

public class PeerCommunicator extends P2PDictionaryAdapter implements IPeerCommunicator, IJxtaConnectionListener, IJxtaServerConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerCommunicator.class);

	private IJxtaServerConnection serverConnection;

	// called by OSGi-DS
	public void activate() {
		LOG.debug("Activated");
		
		createServerConnectionAsync();
	}

	private void createServerConnectionAsync() {
		Thread t = new Thread( new Runnable() {
			@Override
			public void run() {
				try {
					serverConnection = new JxtaBiDiServerConnection(createPipeAdvertisement(P2PNetworkManager.getInstance().getLocalPeerID(), P2PNetworkManager.getInstance().getLocalPeerName()));
					serverConnection.addListener(PeerCommunicator.this);
					serverConnection.start();
				} catch (IOException e) {
					LOG.debug("Could not start server connection", e);
				}
			}
		});
		t.setDaemon(true);
		t.setName("Peer Communication server thread");
		t.start();
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Deactivated");
	}

	@Override
	public boolean isConnected(PeerID destinationPeer) {
		return false;
	}

	@Override
	public void send(PeerID destinationPeer, byte[] message) throws PeerCommunicationException {
	}

	@Override
	public void onReceiveData(IJxtaConnection sender, byte[] data) {
		// got data from connection
	}

	@Override
	public void onConnect(IJxtaConnection sender) {
		LOG.debug("Established connection to remote server");
	}

	@Override
	public void onDisconnect(IJxtaConnection sender) {
		LOG.debug("Lost connection to remote server");
	}

	@Override
	public void connectionAdded(IJxtaServerConnection sender, IJxtaConnection addedConnection) {
		LOG.debug("Established new connection to remote client");
	}

	@Override
	public void connectionRemoved(IJxtaServerConnection sender, IJxtaConnection removedConnection) {
		LOG.debug("Lost connection to remote client");
	}

	@Override
	public void remotePeerAdded(IP2PDictionary sender, PeerID id, String name) {
		LOG.debug("New peer discovered: name = {}", name);
	}

	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		LOG.debug("Discovered peer lost: name = {}", name);
	}
	
	private static PipeAdvertisement createPipeAdvertisement(PeerID peerID, String peerName) {
		PipeID pipeID = IDFactory.newPipeID(P2PNetworkManager.getInstance().getLocalPeerGroupID(), peerID.toString().getBytes());
		
		final PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setName(peerName);
		advertisement.setPipeID(pipeID);
		advertisement.setType(PipeService.UnicastType);
		return advertisement;
	}
}
