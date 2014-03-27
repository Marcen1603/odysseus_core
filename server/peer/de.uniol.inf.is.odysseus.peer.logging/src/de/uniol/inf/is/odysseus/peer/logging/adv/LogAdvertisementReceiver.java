package de.uniol.inf.is.odysseus.peer.logging.adv;

import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.peer.logging.JXTALoggingPlugIn;
import de.uniol.inf.is.odysseus.peer.logging.JxtaLoggingDestinations;

public class LogAdvertisementReceiver implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(LogAdvertisementReceiver.class);
	
	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv) {
		if( adv instanceof LoggingAdvertisement ) {
			LoggingAdvertisement logAdv = (LoggingAdvertisement)adv;
			LOG.debug("Got LoggingAdvertisement...");
			if( !logAdv.getPeerID().equals( JXTALoggingPlugIn.getP2PNetworkManager().getLocalPeerID())) {
				LOG.debug("... and its from peerID {}", logAdv.getPeerID());
				JxtaLoggingDestinations.getInstance().addLoggingAdvertisement(logAdv);
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
				JxtaLoggingDestinations.getInstance().removeLoggingAdvertisement(logAdv);
			}
		}
	}

}
