package de.uniol.inf.is.odysseus.peer.logging;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class LoggingDestinations {

	private static final Logger LOG = LoggerFactory.getLogger(LoggingDestinations.class);
	private static final LoggingDestinations INSTANCE = new LoggingDestinations();

	private final Map<PeerID, LoggingAdvertisement> advertisementMap = Maps.newHashMap();
	private final List<PeerID> knownPeers = Lists.newArrayList();

	private List<PeerID> destinations;
	private boolean changed = true;
	
	private LoggingDestinations() {

	}

	public static LoggingDestinations getInstance() {
		return INSTANCE;
	}

	public void addLoggingAdvertisement(LoggingAdvertisement advertisement) {
		Preconditions.checkNotNull(advertisement, "LogginAdvertisement must not be null!");

		synchronized (advertisementMap) {
			advertisementMap.put(advertisement.getPeerID(), advertisement);
		}
		changed = true;
		LOG.debug("Added LoggingAdvertisement from pid {}", advertisement.getPeerID());
	}

	public void removeLoggingAdvertisement(LoggingAdvertisement advertisement) {
		Preconditions.checkNotNull(advertisement, "LogginAdvertisement must not be null!");

		synchronized (advertisementMap) {
			advertisementMap.remove(advertisement.getPeerID());
		}
		changed = true;
		LOG.debug("Removed LoggingAdvertisement from pid {}", advertisement.getPeerID());
	}

	public void addKnownPeer(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "peerID must not be null!");

		synchronized (knownPeers) {
			knownPeers.add(peerID);
		}
		changed = true;
		LOG.debug("Added known peer {}", peerID);
	}

	public void removeKnownPeer(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "peerID must not be null!");

		synchronized( knownPeers ) {
			knownPeers.remove(peerID);
		}
		changed = true;
		LOG.debug("Removed known peer {}", peerID);
	}

	public Collection<PeerID> getDestinations() {
		if( changed ) {
			destinations = determineDestinations();
			changed = false;
		}
		return destinations;
	}

	private List<PeerID> determineDestinations() {
		List<PeerID> destinations = Lists.newArrayList();

		List<PeerID> knownPeersCopy = null;
		synchronized( knownPeers ) {
			knownPeersCopy = Lists.newArrayList(knownPeers);
		}
		
		synchronized( advertisementMap ) {
			for (PeerID knownPeer : knownPeersCopy) {
				if (advertisementMap.containsKey(knownPeer)) {
					destinations.add(knownPeer);
				}
			}
		}

		return destinations;
	}
}
