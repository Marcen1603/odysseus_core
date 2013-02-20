package de.uniol.inf.is.odysseus.p2p_new.adv;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementSelector;

class AdvertisementAllSelector implements IAdvertisementSelector {

	public static final AdvertisementAllSelector INSTANCE = new AdvertisementAllSelector();
	
	private AdvertisementAllSelector() {
		
	}
	
	@Override
	public boolean isSelected(Advertisement adverisement) {
		return true;
	}

}
