package de.uniol.inf.is.odysseus.peer.broadcast.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.peer.broadcast.PeerReachabilityInfo;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.util.RepeatingJobThread;

public class BroadcastAnswerReceiver extends RepeatingJobThread {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastAnswerReceiver.class);

	private final DatagramSocket socket;
	private final IP2PNetworkManager networkManager;
	private final Map<PeerID, PeerReachabilityInfo> reachabilityMap = Maps.newHashMap();

	public BroadcastAnswerReceiver(DatagramSocket socket, IP2PNetworkManager manager) {
		super(0, "Broadcast answer receive thread");

		Preconditions.checkNotNull(socket, "Socket must not be null!");
		Preconditions.checkNotNull(manager, "Networkmanager must not be null!");

		this.socket = socket;
		this.networkManager = manager;
	}

	@Override
	public void beforeJob() {
		LOG.debug("Beginning udp broadcast answer receiver");
	}

	@Override
	public void doJob() {
		try {
			byte[] buffer = new byte[1024];
			DatagramPacket p = new DatagramPacket(buffer, buffer.length);
			socket.receive(p);

			InetAddress address = p.getAddress();

			try {
				String string = new String(p.getData());

				LOG.debug("Got broadcast answer: {}", string);

				JSONObject obj = new JSONObject(string);

				if (obj.has("PeerID")) {
					PeerID peerID = (PeerID) toID(obj.getString("PeerID"));
					if( peerID != null ) {
						String peerName = obj.getString("PeerName");
						String peerGroup = obj.getString("GroupName");
						PeerGroupID peerGroupID = (PeerGroupID) toID(obj.getString("GroupID"));
						String addressStr = address.getHostAddress();
						int port = obj.getInt("Port");
	
						LOG.debug("Received udp answer: " + addressStr + " = " + peerName + " { " + peerGroup + " }");
						LOG.debug("\tpeerID = " + peerID.toString());
	
						if( !peerID.equals(networkManager.getLocalPeerID())) {
							PeerReachabilityInfo info = new PeerReachabilityInfo(peerID, peerName, address, port, peerGroup, peerGroupID);
							synchronized (reachabilityMap) {
								reachabilityMap.put(peerID, info);
								LOG.debug("Added PeerReachabilityInfo");
							}
						}
					}
				}

			} catch (JSONException e) {
				LOG.debug("Could not JSON-parse answer from " + address.getHostAddress().toString() + ": " + e.getMessage());
			}
			
		} catch (SocketException e) {
			if (!e.getMessage().equalsIgnoreCase("socket closed")) {
				LOG.error("Could not process broadcast answer", e);
			} else {
				stopRunning();
			}
		} catch (IOException e) {
			LOG.error("Could not process broadcast answer", e);
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

	@Override
	public void afterJob() {
		LOG.debug("Stopping udp broadcast answer receiver");
	}

	public void clearReachabilityMap() {
		synchronized (reachabilityMap) {
			reachabilityMap.clear();
		}
	}

	public Map<PeerID, PeerReachabilityInfo> getReachabilityMap() {
		Map<PeerID, PeerReachabilityInfo> copy = Maps.newHashMap();
		synchronized (reachabilityMap) {
			copy.putAll(reachabilityMap);
		}

		return copy;
	}
}