package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.strategy;

import de.uniol.inf.is.odysseus.p2p.thinpeer.strategy.IIdGenerator;

public class StandardIdGenerator implements IIdGenerator {

	static long counter = 0;
	private String seed;
	
	public StandardIdGenerator(String seed) {
		this.seed = seed;
	}
	
	@Override
	public String generateId() {
		return seed+"_"+(counter++);
	}

}
