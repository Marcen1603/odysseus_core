package de.uniol.inf.is.odysseus.p2p_new.communication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

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

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionaryAdapter;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaServerConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi.JxtaBiDiClientConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi.JxtaBiDiServerConnection;

public class PeerCommunicator extends P2PDictionaryAdapter implements IPeerCommunicator, IJxtaConnectionListener, IJxtaServerConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerCommunicator.class);

	private IJxtaServerConnection serverConnection;

	private final Map<IJxtaConnection, PeerID> waitingClientConnections = Maps.newHashMap();
	private final Collection<IJxtaConnection> waitingServerConnections = Lists.newArrayList();

	private final Map<IJxtaConnection, PeerID> activeConnections = Maps.newHashMap();
	private final Map<PeerID, IJxtaConnection> activeConnectionsPeerID = Maps.newHashMap();

	private IP2PDictionary p2pDictionary;

	// called by OSGi-DS
	public void activate() {
		LOG.debug("Activated");

		createServerConnectionAsync();
	}

	private void createServerConnectionAsync() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					LOG.debug("Waiting for p2p network manager");
					waitForP2PNetworkManager();
					LOG.debug("Network manager is active now");

					serverConnection = new JxtaBiDiServerConnection(createPipeAdvertisement(P2PNetworkManager.getInstance().getLocalPeerID(), P2PNetworkManager.getInstance().getLocalPeerName()));
					serverConnection.addListener(PeerCommunicator.this);
					serverConnection.start();
					LOG.debug("Waiting for client peer connections");

				} catch (IOException e) {
					LOG.debug("Could not start server connection", e);
				}
			}

			private void waitForP2PNetworkManager() {
				while (!P2PNetworkManager.isActivated() || !P2PNetworkManager.getInstance().isStarted()) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
					}
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

	public void bindP2PDictionary(IP2PDictionary dict) {
		p2pDictionary = dict;
		LOG.debug("Bound P2PDictionary {}", dict);

		p2pDictionary.addListener(this);
		for (PeerID remotePeerID : p2pDictionary.getRemotePeerIDs()) {
			remotePeerAdded(p2pDictionary, remotePeerID, p2pDictionary.getRemotePeerName(remotePeerID).get());
		}
	}

	public void unbindP2PDictionary(IP2PDictionary dict) {
		if (dict == p2pDictionary) {
			p2pDictionary.removeListener(this);

			LOG.debug("Unbound P2PDictionary {}", dict);
			p2pDictionary = null;
		}
	}

	@Override
	public boolean isConnected(PeerID destinationPeer) {
		return activeConnectionsPeerID.containsKey(destinationPeer);
	}

	@Override
	public void send(PeerID destinationPeer, byte[] message) throws PeerCommunicationException {
		Preconditions.checkNotNull(destinationPeer, "Destination peer to send message must not be null!");
		Preconditions.checkNotNull(message, "Message to send must not be null!");
		
		IJxtaConnection connection = activeConnectionsPeerID.get(destinationPeer);
		try {
			connection.send(message);
		} catch (IOException e) {
			throw new PeerCommunicationException("Could not send message", e);
		}
	}

	@Override
	public void onReceiveData(IJxtaConnection sender, byte[] data) {
		if (waitingServerConnections.contains(sender)) {
			readAndProcessNameInfos(sender, data);
			waitingServerConnections.remove(sender);
			return;
		}
		
		PeerID peerID = activeConnections.get(sender);
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
		String peerID = new String(idBytes);

		activeConnections.put(sender, toID(peerID));
		activeConnectionsPeerID.put(toID(peerID), sender);
		
		LOG.debug("Got connection info from peername {} with id {}", peerName, peerID);
	}

	public static int getInt(byte[] bytes, int offset) {
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

	@Override
	public void onConnect(IJxtaConnection sender) {
		PeerID connectedServerPeerID = waitingClientConnections.get(sender);
		String peerName = P2PDictionary.getInstance().getRemotePeerName(connectedServerPeerID).get();

		waitingClientConnections.remove(sender);

		LOG.debug("Established connection to remote server peer {}", peerName);

		try {
			sendNameInfos(sender);
		} catch (IOException e) {
			LOG.error("Could not name infos to server peer {}", peerName, e);
		}
	}

	private static void sendNameInfos(IJxtaConnection sender) throws IOException {
		String localName = P2PNetworkManager.getInstance().getLocalPeerName();
		String localID = P2PNetworkManager.getInstance().getLocalPeerID().toString();
		LOG.debug("Sending ownName={} and ownID={}", localName, localID);

		byte[] message = new byte[4 + localName.length() + 4 + localID.length()];
		insertInt(message, 0, localName.length());
		System.arraycopy(localName.getBytes(), 0, message, 4, localName.length());
		insertInt(message, 4 + localName.length(), localID.length());
		System.arraycopy(localID.getBytes(), 0, message, 4 + localName.length() + 4, localID.length());

		sender.send(message);
	}

	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}

	@Override
	public void onDisconnect(IJxtaConnection sender) {
		LOG.debug("Lost connection to remote server");
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
		
		if( activeConnections.containsKey(sender)) {
			PeerID peerID = activeConnections.get(sender);
			activeConnections.remove(sender);
			activeConnectionsPeerID.remove(peerID);
		}
	}

	@Override
	public void remotePeerAdded(IP2PDictionary sender, PeerID id, String name) {
		LOG.debug("New peer discovered: name = {}", name);

		IJxtaConnection clientConnection = new JxtaBiDiClientConnection(createPipeAdvertisement(id, name));
		clientConnection.addListener(this);
		tryConnectAsync(clientConnection, id, name);

	}

	private void tryConnectAsync(final IJxtaConnection connection, final PeerID peerID, final String peerName) {
		LOG.debug("Trying to connect to peer {}", peerName);

		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					waitingClientConnections.put(connection, peerID);
					connection.connect();
				} catch (final IOException ex) {
					LOG.error("Could not connect to peer " + peerName, ex);
					waitingClientConnections.remove(connection);
				}
			}
		});
		t.setName("Connect to peer " + peerName + " thread");
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		LOG.debug("Discovered peer lost: name = {}", name);
		
		Optional<IJxtaConnection> optWaitingConnection = getWaitingClientConnection(id);
		if( optWaitingConnection.isPresent()) {
			IJxtaConnection waitingConnection = optWaitingConnection.get();
			waitingConnection.disconnect();
			
			LOG.debug("Stopped waiting connection to peer {}", name);
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

	private static PipeAdvertisement createPipeAdvertisement(PeerID peerID, String peerName) {
		PipeID pipeID = IDFactory.newPipeID(P2PNetworkManager.getInstance().getLocalPeerGroupID(), peerName.getBytes());

		final PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setName(peerName);
		advertisement.setPipeID(pipeID);
		advertisement.setType(PipeService.UnicastType);
		return advertisement;
	}
}
