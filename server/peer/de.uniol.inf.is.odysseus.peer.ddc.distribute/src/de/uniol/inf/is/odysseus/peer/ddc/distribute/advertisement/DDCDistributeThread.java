package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement;

import de.uniol.inf.is.odysseus.peer.ddc.DDC;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DDCAdvertisementGenerator;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DDCAdvertisementSender;

public class DDCDistributeThread extends Thread {
	
	private DDC ddc;
	
	public DDCDistributeThread(DDC ddc){
		this.ddc = ddc;
	}

	@Override
	public void run() {
		DDCAdvertisement advertisement = DDCAdvertisementGenerator
				.getInstance().generate(ddc);
		DDCAdvertisementSender.getInstance().publishDDCAdvertisement(
				advertisement);
	}
}
