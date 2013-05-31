package de.uniol.inf.is.odysseus.p2p_new.dictionary.sources;

import java.util.List;

import net.jxta.document.Advertisement;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.peer.PeerID;
import net.jxta.protocol.RouteAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;

public class SourceAdvertisementReceiver implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(SourceAdvertisementReceiver.class);
	
	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv) {
		if (adv instanceof SourceAdvertisement ) {
			final SourceAdvertisement srcAdvertisement = (SourceAdvertisement) adv;
			if( !P2PDictionary.getInstance().existsSource(srcAdvertisement)) {
				
				if( srcAdvertisement.getPeerID().equals(P2PDictionary.getInstance().getLocalPeerID()) && srcAdvertisement.isView() ) {
					// überbleibsel aus alter veröffentlichung (Advertisement-Echo)
					// --> ignorieren
					return;
				}
				
				
				try {
					P2PDictionary.getInstance().addSource(srcAdvertisement, true);
				} catch (InvalidP2PSource e) {
					LOG.error("Could not add source advertisement", e);
				}
			}
		} else if( adv instanceof RouteAdvertisement ) {
			RouteAdvertisement routeAdv = (RouteAdvertisement)adv;
			List<EndpointAddress> destEndpointAddresses = routeAdv.getDestEndpointAddresses();
			PeerID destPeerID = routeAdv.getDestPeerID();
			System.out.println();
			System.out.println("ROUTE: " + P2PDictionary.getInstance().getPeerRemoteName(destPeerID));
			for( EndpointAddress address : destEndpointAddresses ) {
				System.out.println("\t" + address.getProtocolAddress());
			}
		}
	}


	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		if (adv instanceof SourceAdvertisement) {
			final SourceAdvertisement srcAdvertisement = (SourceAdvertisement) adv;
			P2PDictionary.getInstance().removeSource(srcAdvertisement);
		}		
	}
}
