package de.uniol.inf.is.odysseus.peer.logging;

import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.logging.adv.LoggingAdvertisement;

public class JxtaLoggingDestinations {

	private JxtaLoggingDestinations() {

	}

	public static Collection<PeerID> getDestinations() {
		return determineDestinations();
	}

	private static List<PeerID> determineDestinations() {
		List<PeerID> destinations = Lists.newArrayList();
		Collection<LoggingAdvertisement> advs = JXTALoggingPlugIn.getJxtaServicesProvider().getLocalAdvertisements(LoggingAdvertisement.class);
		for( LoggingAdvertisement adv : advs ) {
			destinations.add(adv.getPeerID());
		}
		
		Collection<PeerID> knownPeers = JXTALoggingPlugIn.getP2PDictionary().getRemotePeerIDs();
		
		for( PeerID destination : destinations.toArray(new PeerID[0])) {
			if( !knownPeers.contains(destination)) {
				destinations.remove(destination);
			}
		}
		
		return destinations;
	}
}
