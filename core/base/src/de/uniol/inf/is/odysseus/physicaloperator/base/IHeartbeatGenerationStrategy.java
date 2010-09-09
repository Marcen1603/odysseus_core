package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.IClone;


public interface IHeartbeatGenerationStrategy<R> extends IClone {
	public void generateHeartbeat(R object, ISource<?> source);
	IHeartbeatGenerationStrategy<R> clone();
}
