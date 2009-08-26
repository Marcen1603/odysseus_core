package de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.strategy;

import java.util.Random;

import de.uniol.inf.is.odysseus.p2p.thinpeer.strategy.IIdGenerator;

public class RandomIdGenerator implements IIdGenerator {

	@Override
	public String generateId() {
		Random r = new Random();
		return String.valueOf(r.nextInt(500000));
	}

}
