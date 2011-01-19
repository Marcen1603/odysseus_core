package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.strategy;

import java.util.List;
import java.util.Random;

import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.IHotPeerStrategy;

public class HotPeerStrategyRandom implements IHotPeerStrategy {

	@Override
	public Object getHotPeer(List<?> peerList) {
		Random rnd = new Random(System.currentTimeMillis());
		return rnd.nextInt(peerList.size());
	}


}
