package de.uniol.inf.is.odysseus.physicaloperator.base;


public class NoHeartbeatGenerationStrategy<T> implements
		IHeartbeatGenerationStrategy<T> {

	@Override
	public void generateHeartbeat(T object, ISource source) {
		// DO NOTHING
	}

	@Override
	public NoHeartbeatGenerationStrategy clone() {
		return new NoHeartbeatGenerationStrategy();
	}
}
