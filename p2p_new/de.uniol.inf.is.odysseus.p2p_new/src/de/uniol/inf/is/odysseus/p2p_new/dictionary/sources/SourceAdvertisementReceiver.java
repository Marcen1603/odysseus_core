package de.uniol.inf.is.odysseus.p2p_new.dictionary.sources;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;

public class SourceAdvertisementReceiver implements IAdvertisementListener {

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
				
				P2PDictionary.getInstance().addSource(srcAdvertisement);
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
