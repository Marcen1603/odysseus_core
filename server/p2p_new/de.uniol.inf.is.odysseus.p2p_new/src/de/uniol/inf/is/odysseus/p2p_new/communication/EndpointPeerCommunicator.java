package de.uniol.inf.is.odysseus.p2p_new.communication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.EndpointListener;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.Messenger;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;

public class EndpointPeerCommunicator implements IPeerCommunicator, IP2PDictionaryListener, EndpointListener {

	private static final String COMMUNICATION_SERVICE_ID = "odysseusP2PComm";

	private static final Logger LOG = LoggerFactory.getLogger(EndpointPeerCommunicator.class);

	private static IP2PDictionary p2pDictionary;

	private final Map<PeerID, Messenger> messengerMap = Maps.newHashMap();

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
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Activated");
	}

	@Override
	public ImmutableCollection<PeerID> getConnectedPeers() {
		return ImmutableList.copyOf(messengerMap.keySet());
	}

	@Override
	public boolean isConnected(PeerID destinationPeer) {
		return messengerMap.containsKey(destinationPeer);
	}

	@Override
	public void send(PeerID destinationPeer, byte[] message) throws PeerCommunicationException {
		Messenger messenger = messengerMap.get(destinationPeer);
		if( messenger == null ) {
			EndpointAddress addr = new EndpointAddress(destinationPeer, null, null);
			messenger = JxtaServicesProvider.getInstance().getEndpointService().getMessenger(addr);
			if( messenger == null ) {
				LOG.error("Wanted to send message to unknown peer {}", destinationPeer);
				return;
			}
			
			messengerMap.put(destinationPeer, messenger);
		}
		
		if( messenger.isClosed() ) {
			LOG.error("Tried to send message with closed messenger (e.g. lost peer): {}", destinationPeer);
			messengerMap.remove(destinationPeer);
			return;
		}
		
		Message msg = new Message();
		msg.addMessageElement(new ByteArrayMessageElement("bytes", null, message, null));

		try {
			messenger.sendMessage(msg, COMMUNICATION_SERVICE_ID, null);
		} catch (IOException e) {
			throw new PeerCommunicationException("Could not send message", e);
		}
	}

	@Override
	public void addListener(IPeerCommunicatorListener listener) {
		PeerCommunicatorListenerRegistry.getInstance().add(listener);
	}

	@Override
	public void removeListener(IPeerCommunicatorListener listener) {
		PeerCommunicatorListenerRegistry.getInstance().remove(listener);
	}

	@Override
	public void sourceAdded(IP2PDictionary sender, SourceAdvertisement advertisement) {
	}

	@Override
	public void sourceRemoved(IP2PDictionary sender, SourceAdvertisement advertisement) {
	}

	@Override
	public void sourceImported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
	}

	@Override
	public void sourceExported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
	}

	@Override
	public void remotePeerAdded(IP2PDictionary sender, PeerID id, String name) {
		LOG.debug("Got new peer {}, id = {}", name, id);

		EndpointAddress addr = new EndpointAddress(id, null, null);
		Messenger peerMessenger = JxtaServicesProvider.getInstance().getEndpointService().getMessenger(addr);
		messengerMap.put(id, peerMessenger);

		JxtaServicesProvider.getInstance().getEndpointService().addIncomingMessageListener(this, COMMUNICATION_SERVICE_ID, null);
	}

	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		LOG.debug("Peer {} is lost", name);

		messengerMap.remove(id);
	}

	@Override
	public void processIncomingMessage(Message message, EndpointAddress srcAddr, EndpointAddress dstAddr) {
		PeerID pid = (PeerID) toID("urn:jxta:" + srcAddr.getProtocolAddress());

		ByteArrayMessageElement messageElement = (ByteArrayMessageElement)message.getMessageElement("bytes");
		Collection<IPeerCommunicatorListener> listeners = PeerCommunicatorListenerRegistry.getInstance().getAll();
		for (IPeerCommunicatorListener listener : listeners) {
			try {
				listener.receivedMessage(this, pid, messageElement.getBytes());
			} catch (Throwable t) {
				LOG.error("Exception in peer communicator listener", t);
			}
		}
	}

	private static ID toID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}
}
