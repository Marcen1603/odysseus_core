package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.IClone;


public interface IHeartbeatGenerationStrategy<R> extends IClone {
	public void generateHeartbeat(R object, ISource<?> source);
	@Override
	IHeartbeatGenerationStrategy<R> clone();
}
