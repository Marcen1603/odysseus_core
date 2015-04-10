package de.uniol.inf.is.odysseus.p2p_new.dictionary.impl;

import java.util.Collection;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveMultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.service.P2PNetworkManagerService;

public class RemoveSourceAdvertisementCollector extends AdvertisementCollector<RemoveSourceAdvertisement, RemoveMultipleSourceAdvertisement>{

	@Override
	public RemoveMultipleSourceAdvertisement merge(Collection<RemoveSourceAdvertisement> advertisements) {
		
		Collection<ID> sourceIDRemoved = determineIDs(advertisements);
		
		RemoveMultipleSourceAdvertisement adv = (RemoveMultipleSourceAdvertisement) AdvertisementFactory.newAdvertisement(RemoveMultipleSourceAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.getInstance().getLocalPeerGroupID()));
		adv.setSourceAdvertisementIDs(sourceIDRemoved);
		
		return adv;
	}

	private static Collection<ID> determineIDs(Collection<RemoveSourceAdvertisement> advertisements) {
		Collection<ID> ids = Lists.newArrayList();
		for( RemoveSourceAdvertisement advertisement : advertisements ) {
			ids.add(advertisement.getSourceAdvertisementID());
		}
		return ids;
	}

}
