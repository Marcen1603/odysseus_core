package de.uniol.inf.is.odysseus.physicaloperator;


public class NoHeartbeatGenerationStrategy<T> implements
		IHeartbeatGenerationStrategy<T> {

	@Override
	public void generateHeartbeat(T object, ISource<?> source) {
		// DO NOTHING
	}

	@Override
	public NoHeartbeatGenerationStrategy<T> clone() {
		return new NoHeartbeatGenerationStrategy<T>();
	}
}
