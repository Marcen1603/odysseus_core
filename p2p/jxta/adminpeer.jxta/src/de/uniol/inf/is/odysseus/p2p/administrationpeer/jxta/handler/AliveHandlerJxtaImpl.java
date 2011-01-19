package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.handler;

import java.io.IOException;

import de.uniol.inf.is.odysseus.p2p.administrationpeer.handler.IAliveHandler;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;

/**
 * TODO: Über einen neuen Rückkanal Alive bestätigen
 * 
 * @author Mart Köhler
 *
 */
public class AliveHandlerJxtaImpl implements IAliveHandler {

	private final int WAIT_TIME = 10000;
	private AdministrationPeerJxtaImpl administrationPeerJxtaImpl;

	public AliveHandlerJxtaImpl(AdministrationPeerJxtaImpl administrationPeerJxtaImpl) {
		this.administrationPeerJxtaImpl = administrationPeerJxtaImpl;
	}
	
	@Override
	public void run() {

		ExtendedPeerAdvertisement adv = new ExtendedPeerAdvertisement();
		adv.setPeerName(administrationPeerJxtaImpl
				.getNetPeerGroup().getPeerName());
		adv.setPipe(administrationPeerJxtaImpl
				.getServerPipeAdvertisement().toString());
		adv.setType("AdministrationPeer");
		adv.setPeerId(administrationPeerJxtaImpl
				.getNetPeerGroup().getPeerID().toString());
		while (true) {
			try {
				administrationPeerJxtaImpl.getDiscoveryService()
						.publish(adv, 13000, 13000);
				administrationPeerJxtaImpl.getDiscoveryService()
						.remotePublish(adv, 13000);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
