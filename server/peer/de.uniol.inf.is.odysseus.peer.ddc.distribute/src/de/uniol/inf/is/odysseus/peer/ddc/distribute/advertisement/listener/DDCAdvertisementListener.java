package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.listener;

import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DDCAdvertisement;
import net.jxta.document.Advertisement;

public class DDCAdvertisementListener implements IDDCAdvertisementListener{

	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if (advertisement instanceof DDCAdvertisement){
			//DDCAdvertisement ddcAdvertisement = (DDCAdvertisement) advertisement;
			
		}
	}

	@Override
	public void updateAdvertisements() {
		// TODO Auto-generated method stub
		
	}

}
