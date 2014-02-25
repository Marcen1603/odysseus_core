package de.uniol.inf.is.odysseus.p2p_new.communication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.adv.AdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionaryAdapter;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi.JxtaBiDiClientConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi.JxtaBiDiServerConnection;

public class PeerCommunicator extends P2PDictionaryAdapter implements IPeerCommunicator, IJxtaConnectionListener, IJxtaServerConnectionListener, IAdvertisementListener {

	private static final int WAIT_INTERVAL_MILLIS = 200;
	private static final Logger LOG = LoggerFactory.getLogger(PeerCommunicator.class);

	private final Map<IJxtaConnection, PeerID> waitingClientConnections = Maps.newHashMap();
	private final Collection<IJxtaConnection> waitingServerConnections = Lists.newArrayList();

	private final Map<IJxtaConnection, PeerID> activeConnectionsAsServer = Maps.newHashMap();
	private final Map<IJxtaConnection, PeerID> activeConnectionsAsClient = Maps.newHashMap();
	
	private final Map<PeerID, IJxtaConnection> activeConnectionsAsServer_PeerID = Maps.newHashMap();
	private final Map<PeerID, IJxtaConnection> activeConnectionsAsClient_PeerID = Maps.newHashMap();
	
	private IP2PDictionary p2pDictionary;
	private PipeID serverPipeID;
	private IJxtaServerConnection serverConnection;

	// called by OSGi-DS
	public void bindP2PDictionary(IP2PDictionary dict) {
		p2pDictionary = dict;
		LOG.debug("Bound P2PDictionary {}", dict);

		p2pDictionary.addListener(this);
		for (PeerID remotePeerID : p2pDictionary.getRemotePeerIDs()) {
			remotePeerAdded(p2pDictionary, remotePeerID, p2pDictionary.getRemotePeerName(remotePeerID).get());
		}
	}
	
	// called by OSGi-DS
	public void unbindP2PDictionary(IP2PDictionary dict) {
		if (dict == p2pDictionary) {
			p2pDictionary.removeListener(this);

			LOG.debug("Unbound P2PDictionary {}", dict);
			p2pDictionary = null;
		}
	}
	
	// called by OSGi-DS
	public void activate() {
		LOG.debug("Activated");

		createServerConnectionAsync();
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Deactivated");
		
		for( IJxtaConnection con : activeConnectionsAsClient.keySet() ) {
			con.disconnect();
		}
		
		for( IJxtaConnection con : activeConnectionsAsServer.keySet() ) {
			con.disconnect();
		}
		
		activeConnectionsAsClient.clear();
		activeConnectionsAsClient_PeerID.clear();
		activeConnectionsAsServer.clear();
		activeConnectionsAsServer_PeerID.clear();

		serverConnection.removeListener(this);
	}

	private void createServerConnectionAsync() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					LOG.debug("Waiting for p2p network manager");
					waitForP2PNetworkManager();
					LOG.debug("Network manager is active now");

					serverPipeID = IDFactory.newPipeID(P2PNetworkManager.getInstance().getLocalPeerGroupID());
					serverConnection = new JxtaBiDiServerConnection(createPipeAdvertisement(serverPipeID));
					serverConnection.addListener(PeerCommunicator.this);
					serverConnection.start();
					LOG.debug("Waiting for client peer connections");
					
					AdvertisementManager.getInstance().addAdvertisementListener(PeerCommunicator.this);
					publishConnectionAdvertisement(serverPipeID, P2PNetworkManager.getInstance().getLocalPeerName());

				} catch (IOException e) {
					LOG.debug("Could not start server connection", e);
				}
			}

		});
		t.setDaemon(true);
		t.setName("Peer Communication server thread");
		t.start();
	}
	
	private static void waitForP2PNetworkManager() {
		while (!P2PNetworkManager.isActivated() || !P2PNetworkManager.getInstance().isStarted()) {
			waitSomeTime();
		}
	}

	private static void waitSomeTime() {
		try {
			Thread.sleep(WAIT_INTERVAL_MILLIS);
		} catch (InterruptedException e) {
		}
	}

	private static PipeAdvertisement createPipeAdvertisement(PipeID pipeID) {
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(pipeID);
		advertisement.setType(PipeService.UnicastType);
		return advertisement;
	}

	private void publishConnectionAdvertisement(PipeID pipeID, String peerName) {
		CommunicationAdvertisement adv = (CommunicationAdvertisement)AdvertisementFactory.newAdvertisement(CommunicationAdvertisement.getAdvertisementType());
		
		adv.setPeerID(P2PNetworkManager.getInstance().getLocalPeerID());
		adv.setPipeID(pipeID);
		adv.setID(IDFactory.newPipeID(P2PNetworkManager.getInstance().getLocalPeerGroupID()));
		adv.setPeerName(peerName);

		waitForJxtaServices();
		
		try {
			JxtaServicesProvider.getInstance().getDiscoveryService().publish(adv, DiscoveryService.DEFAULT_LIFETIME, DiscoveryService.DEFAULT_EXPIRATION);
			JxtaServicesProvider.getInstance().getDiscoveryService().remotePublish(adv, DiscoveryService.DEFAULT_EXPIRATION);
			LOG.debug("Published communication advertisement");
			
		} catch (IOException e) {
			LOG.error("Could not publish communication advertisement", e);
		}
	}
	
	private static void waitForJxtaServices() {
		LOG.debug("Waiting for jxta services become active...");
		while( !JxtaServicesProvider.isActivated() ) {
			waitSomeTime();
		}
		LOG.debug("Got jxta services! Begin repeated publishing of communication advertisement");
	}
		
	@Override
	public void connectionAdded(IJxtaServerConnection sender, IJxtaConnection addedConnection) {
		LOG.debug("Established new connection to remote client");

		waitingServerConnections.add(addedConnection);
		addedConnection.addListener(this);
	}

	@Override
	public void connectionRemoved(IJxtaServerConnection sender, IJxtaConnection removedConnection) {
		LOG.debug("Lost connection to remote client");
		
		if( waitingServerConnections.contains(removedConnection)) {
			LOG.debug("Interrupt waiting server connection");
			waitingServerConnections.remove(removedConnection);
		}
		
		if( activeConnectionsAsServer.containsKey(removedConnection)) {
			PeerID peerID = activeConnectionsAsServer.get(removedConnection);
			activeConnectionsAsServer.remove(removedConnection);
			activeConnectionsAsServer_PeerID.remove(peerID);
			
			LOG.debug("Removed active connection as server");
		}
	}

	@Override
	public boolean isConnected(PeerID destinationPeer) {
		return activeConnectionsAsServer_PeerID.containsKey(destinationPeer);
	}

	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv) {
		if( adv instanceof CommunicationAdvertisement ) {
			CommunicationAdvertisement commAdv = (CommunicationAdvertisement)adv;
			LOG.debug("New communication advertisement received from peer {}...", commAdv.getPeerName());
			if( isNotOwnPeer(commAdv) && hasNoConnection(commAdv)) {
				LOG.debug("...and is a new interesting one");
				IJxtaConnection clientConnection = new JxtaBiDiClientConnection(createPipeAdvertisement(commAdv.getPipeID()));
				clientConnection.addListener(this);
				tryConnectAsync(clientConnection, commAdv.getPeerID(), commAdv.getPeerName());
			} else {
				LOG.debug("...but is our own peer or we have already a connection to that peer");
			}
		}
	}

	private boolean hasNoConnection(CommunicationAdvertisement commAdv) {
		return !activeConnectionsAsClient_PeerID.containsKey(commAdv.getPeerID()) &&
				!getWaitingClientConnection(commAdv.getPeerID()).isPresent();
	}

	private static boolean isNotOwnPeer(CommunicationAdvertisement commAdv) {
		return !commAdv.getPeerID().equals(P2PNetworkManager.getInstance().getLocalPeerID()) 
				&& !commAdv.getPeerName().equals(P2PNetworkManager.getInstance().getLocalPeerName());
	}

	private void tryConnectAsync(final IJxtaConnection connection, final PeerID peerID, final String peerName) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					LOG.debug("Beginning connection to server peer {}", peerName);
					waitingClientConnections.put(connection, peerID);
					connection.connect();
				} catch (final IOException ex) {
					waitingClientConnections.remove(connection);
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public void onConnect(IJxtaConnection sender) {
		PeerID connectedServerPeerID = waitingClientConnections.get(sender);

		waitingClientConnections.remove(sender);

		LOG.debug("Established connection to remote server peer");
		
		activeConnectionsAsClient.put(sender, connectedServerPeerID);
		activeConnectionsAsClient_PeerID.put(connectedServerPeerID, sender);

		try {
			sendNameInfos(sender);
		} catch (IOException e) {
			LOG.error("Could not name infos to server peer", e);
		}
	}
	
	private static void sendNameInfos(IJxtaConnection destination) throws IOException {
		String localName = P2PNetworkManager.getInstance().getLocalPeerName();
		String localID = P2PNetworkManager.getInstance().getLocalPeerID().toString();
		LOG.debug("Sending ownName={} and ownID={} to server peer", localName, localID);

		byte[] message = new byte[4 + localName.length() + 4 + localID.length()];
		insertInt(message, 0, localName.length());
		System.arraycopy(localName.getBytes(), 0, message, 4, localName.length());
		insertInt(message, 4 + localName.length(), localID.length());
		System.arraycopy(localID.getBytes(), 0, message, 4 + localName.length() + 4, localID.length());

		destination.send(message);
	}	
	
	@Override
	public void onDisconnect(IJxtaConnection sender) {
		if( activeConnectionsAsClient.containsKey(sender)) {
			PeerID peerID = activeConnectionsAsClient.get(sender);
			LOG.debug("Lost connection to remote server peer");
			
			activeConnectionsAsClient.remove(sender);
			activeConnectionsAsClient_PeerID.remove(peerID);
		}
	}
	
	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
	}

	@Override
	public void send(PeerID destinationPeer, byte[] message) throws PeerCommunicationException {
		Preconditions.checkNotNull(destinationPeer, "Destination peer to send message must not be null!");
		Preconditions.checkNotNull(message, "Message to send must not be null!");
		
		IJxtaConnection connection = activeConnectionsAsServer_PeerID.get(destinationPeer);
		try {
			connection.send(message);
		} catch (IOException e) {
			throw new PeerCommunicationException("Could not send message", e);
		}
	}

	@Override
	public void onReceiveData(IJxtaConnection sender, byte[] data) {
		// receive data as server
		if (waitingServerConnections.contains(sender)) {
			
			readAndProcessNameInfos(sender, data);
			waitingServerConnections.remove(sender);
			return;
		}
		
		// receiver data as client
		PeerID peerID = activeConnectionsAsClient.get(sender);
		if( peerID == null ) {
			throw new RuntimeException("Got message from unknown connection/peer");
		}
		
		Collection<IPeerCommunicatorListener> listeners = PeerCommunicatorListenerRegistry.getInstance().getAll();
		for( IPeerCommunicatorListener listener : listeners ) {
			try {
				listener.receivedMessage(this, peerID, data);
			} catch( Throwable t ) {
				LOG.error("Exception in peer communicator listener", t);
			}
		}
	}

	private void readAndProcessNameInfos(IJxtaConnection sender, byte[] data) {
		int nameLength = getInt(data, 0);
		byte[] nameBytes = new byte[nameLength];
		System.arraycopy(data, 4, nameBytes, 0, nameLength);

		int idLength = getInt(data, 4 + nameLength);
		byte[] idBytes = new byte[idLength];
		System.arraycopy(data, 4 + nameLength + 4, idBytes, 0, idLength);

		String peerName = new String(nameBytes);
		String peerIDStr = new String(idBytes);

		PeerID peerID = toID(peerIDStr);
		activeConnectionsAsServer.put(sender, peerID);
		activeConnectionsAsServer_PeerID.put(peerID, sender);
		
		LOG.debug("Got connection info from client peer name={} with id={}", peerName, peerID);
	}

	private static int getInt(byte[] bytes, int offset) {
		int ret = 0;
		for (int i = 0; i < 4 && i + offset < bytes.length; i++) {
			ret <<= 8;
			ret |= bytes[i + offset] & 0xFF;
		}
		return ret;
	}

	private static PeerID toID(String text) {
		try {
			final URI id = new URI(text);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}

	@Override
	public void remotePeerAdded(IP2PDictionary sender, PeerID id, String name) {
		// do nothing
	}


	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		LOG.debug("Discovered (server-)peer lost: name = {}", name);
		
		Optional<IJxtaConnection> optWaitingConnection = getWaitingClientConnection(id);
		if( optWaitingConnection.isPresent()) {
			IJxtaConnection waitingConnection = optWaitingConnection.get();
			waitingConnection.disconnect();
			
			LOG.debug("Stopped waiting connection to peer {}", name);
		}
		
		if( activeConnectionsAsServer_PeerID.containsKey(id)) {
			IJxtaConnection removed = activeConnectionsAsServer_PeerID.remove(id);
			removed.disconnect();
			
			activeConnectionsAsServer.remove(removed);
			LOG.debug("Removed active connection to client peer {}", name);
		}
		
		if( activeConnectionsAsClient_PeerID.containsKey(id)) {
			IJxtaConnection removed = activeConnectionsAsClient_PeerID.remove(id);
			removed.disconnect();
			
			activeConnectionsAsClient.remove(removed);
			LOG.debug("Removed active connection to server peer {}", name);
		}
	}

	private Optional<IJxtaConnection> getWaitingClientConnection(PeerID id) {
		for( IJxtaConnection connection : waitingClientConnections.keySet() ) {
			if( waitingClientConnections.get(connection).equals(id)) {
				return Optional.of(connection);
			}
		}
		return Optional.absent();
	}
	
	@Override
	public void addListener(IPeerCommunicatorListener listener) {
		PeerCommunicatorListenerRegistry.getInstance().add(listener);
	}
	
	@Override
	public void removeListener(IPeerCommunicatorListener listener) {
		PeerCommunicatorListenerRegistry.getInstance().remove(listener);
	}
}
