package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.handler;

import de.uniol.inf.is.odysseus.p2p.administrationpeer.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.AdministrationPeerJxtaImpl;

public class BiddingHandlerJxtaImpl implements IBiddingHandler {

	// Wieviele Schleifendurchlaeufe soll gewartet werden, ob noch genug
	// Angebote eingehen.
	int MAXRETRIES = 3;

	int WAIT_TIME = 10000;

	public BiddingHandlerJxtaImpl() {
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (AdministrationPeerJxtaImpl.getInstance().getQueries()) {
				AdministrationPeerJxtaImpl.getInstance()
						.getBiddingHandlerStrategy().handleBidding();
			}

		}

	}

}
