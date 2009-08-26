package de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler;

import java.io.IOException;

import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.IAliveHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.listener.SocketServerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.ExtendedPeerAdvertisement;

public class AliveHandlerJxtaImpl implements IAliveHandler {
	private final int WAIT_TIME = 10000;

	@Override
	public void run() {

		ExtendedPeerAdvertisement adv = new ExtendedPeerAdvertisement();
		adv.setPeerName(OperatorPeerJxtaImpl.getInstance().getNetPeerGroup()
				.getPeerName());
		adv.setPipe(((SocketServerListenerJxtaImpl) OperatorPeerJxtaImpl
				.getInstance().getSocketServerListener())
				.getServerPipeAdvertisement().toString());
		adv.setType("OperatorPeer");
		adv.setPeerId(OperatorPeerJxtaImpl.getInstance().getNetPeerGroup()
				.getPeerID().toString());
		while (true) {
			try {
				OperatorPeerJxtaImpl.getInstance().getDiscoveryService()
						.publish(adv, 13000, 13000);
				OperatorPeerJxtaImpl.getInstance().getDiscoveryService()
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
