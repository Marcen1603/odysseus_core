package de.uniol.inf.is.odysseus.p2p.jxta.utils;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

public class AdvertisementTools {
	
	public static PipeAdvertisement getServerPipeAdvertisement(PeerGroup netPeerGroup) {
		ID pipeId = IDFactory.newPipeID(netPeerGroup.getPeerGroupID());
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(pipeId);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("serverPipe");
		return advertisement;
	}
}
