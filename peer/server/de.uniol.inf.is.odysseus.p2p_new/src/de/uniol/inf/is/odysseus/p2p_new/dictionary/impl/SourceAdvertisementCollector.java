package de.uniol.inf.is.odysseus.p2p_new.dictionary.impl;

import java.util.Collection;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.MultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.service.P2PNetworkManagerService;

public class SourceAdvertisementCollector extends AdvertisementCollector<SourceAdvertisement, MultipleSourceAdvertisement>{

	@Override
	public MultipleSourceAdvertisement merge(Collection<SourceAdvertisement> advertisements) {
		MultipleSourceAdvertisement multipleSrcAdv = (MultipleSourceAdvertisement) AdvertisementFactory.newAdvertisement(MultipleSourceAdvertisement.getAdvertisementType());
		multipleSrcAdv.setID(IDFactory.newPipeID(P2PNetworkManagerService.getInstance().getLocalPeerGroupID()));
		multipleSrcAdv.setPeerID(P2PNetworkManagerService.getInstance().getLocalPeerID());
		multipleSrcAdv.setSourceAdvertisements(advertisements);
		return multipleSrcAdv;
	}

}
