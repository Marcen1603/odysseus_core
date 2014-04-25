package de.uniol.inf.is.odysseus.peer.logging;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
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
				destinationMap.put(adv.getPeerID(), adv);
			}
			
			Collection<PeerID> knownPeers = JXTALoggingPlugIn.getP2PDictionary().getRemotePeerIDs();
			
			for( PeerID destination : destinationMap.keySet().toArray(new PeerID[0])) {
				if( !knownPeers.contains(destination)) {
					destinationMap.remove(destination);
					
					try {
						jxtaServicesProvider.flushAdvertisement(destinationMap.get(destination));
					} catch (IOException e) {
					}
				}
			}
		}
		
		return destinationMap.keySet();
	}
}
