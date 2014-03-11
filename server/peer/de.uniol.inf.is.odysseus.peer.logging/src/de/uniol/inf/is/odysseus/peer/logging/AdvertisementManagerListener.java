package de.uniol.inf.is.odysseus.peer.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;

public class AdvertisementManagerListener implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementManagerListener.class);
	
	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv) {
		if( adv instanceof LoggingAdvertisement ) {
			LoggingAdvertisement logAdv = (LoggingAdvertisement)adv;
			LOG.debug("Got LoggingAdvertisement...");
			if( !logAdv.getPeerID().equals( JXTALoggingPlugIn.getP2PNetworkManager().getLocalPeerID())) {
				LOG.debug("... and its from peerID {}", logAdv.getPeerID());
				LoggingDestinations.getInstance().add(logAdv.getPeerID());
			} else {
				LOG.debug("... and its ours");
			}
		}
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		if( adv instanceof LoggingAdvertisement ) {
			LoggingAdvertisement logAdv = (LoggingAdvertisement)adv;
			LOG.debug("Removed LoggingAdvertisement");
			if( !logAdv.getPeerID().equals( JXTALoggingPlugIn.getP2PNetworkManager().getLocalPeerID())) {
				LoggingDestinations.getInstance().remove(logAdv.getPeerID());
			}
		}
	}

}
