package de.uniol.inf.is.odysseus.p2p_new.distributor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class QueryPartManager implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartManager.class);
	
	@Override
	public boolean isSelected(Advertisement advertisement) {
		if( advertisement instanceof QueryPartAdvertisement ) {
			QueryPartAdvertisement adv = (QueryPartAdvertisement)advertisement;
			return adv.getPeerID().equals(P2PNewPlugIn.getOwnPeerID());
		}
		return false;
	}

	@Override
	public void advertisementOccured(IAdvertisementManager sender, Advertisement advertisement) {
		QueryPartAdvertisement adv = (QueryPartAdvertisement)advertisement;
		LOG.debug("Got query part");
		LOG.debug("PQL-Statement is {}", adv.getPqlStatement());
	}
	
	// called by OSGi-DS
	public void bindAdvertisementManager(IAdvertisementManager manager) {
		manager.addAdvertisementListener(this);

		LOG.debug("Bound AdvertisementManager {}", manager);
	}

	// called by OSGi-DS
	public void unbindAdvertisementManager(IAdvertisementManager manager) {
		manager.removeAdvertisementListener(this);

		LOG.debug("Unbound AdvertisementManager {}", manager);
	}

}
