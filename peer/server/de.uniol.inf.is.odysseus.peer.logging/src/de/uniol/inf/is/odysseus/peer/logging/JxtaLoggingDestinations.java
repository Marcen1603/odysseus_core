package de.uniol.inf.is.odysseus.peer.logging;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.peer.jxta.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.peer.logging.adv.LoggingAdvertisement;

public class JxtaLoggingDestinations {

	private JxtaLoggingDestinations() {

	}

	public static Collection<PeerID> getDestinations() {
		return determineDestinations();
	}

	private static Collection<PeerID> determineDestinations() {
		Map<PeerID, LoggingAdvertisement> destinationMap = Maps.newHashMap();
		
		IJxtaServicesProvider jxtaServicesProvider = JXTALoggingPlugIn.getJxtaServicesProvider();
		if( jxtaServicesProvider != null ) {
			Collection<LoggingAdvertisement> advs = jxtaServicesProvider.getLocalAdvertisements(LoggingAdvertisement.class);
			for( LoggingAdvertisement adv : advs ) {
				if( !destinationMap.containsKey(adv.getPeerID()) ) {
					destinationMap.put(adv.getPeerID(), adv);
				} else {
					tryFlush(jxtaServicesProvider, adv);
				}
			}
			
			Collection<PeerID> knownPeers = JXTALoggingPlugIn.getPeerDictionary().getRemotePeerIDs();
			
			for( PeerID destination : destinationMap.keySet().toArray(new PeerID[0])) {
				if( !knownPeers.contains(destination)) {
					LoggingAdvertisement advertisement = destinationMap.remove(destination);
					
					tryFlush(jxtaServicesProvider, advertisement);
				}
			}
		}
		
		return destinationMap.keySet();
	}

	private static void tryFlush(IJxtaServicesProvider jxtaServicesProvider, LoggingAdvertisement advertisement) {
		try {
			jxtaServicesProvider.flushAdvertisement(advertisement);
		} catch (IOException e) {
		}
	}
}
