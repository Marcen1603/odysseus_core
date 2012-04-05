package de.uniol.inf.is.odysseus.intervalapproach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;

public class NElementHeartbeatGeneration <K extends ITimeInterval, T extends IMetaAttributeContainer<K>> implements IHeartbeatGenerationStrategy<T>{

	static private final Logger logger = LoggerFactory.getLogger(NElementHeartbeatGeneration.class);
	
	private int counter = 0;
	final private int eachElement;
	
	public NElementHeartbeatGeneration() {
		eachElement = 10;
	}

	public NElementHeartbeatGeneration(int eachElement) {
		this.eachElement = eachElement;
	}

	public NElementHeartbeatGeneration(
			NElementHeartbeatGeneration<K, T> nElementHeartbeatGeneration) {
		this.eachElement = nElementHeartbeatGeneration.eachElement;
		this.counter = nElementHeartbeatGeneration.counter;
	}

	@Override
	public void generateHeartbeat(T object, ISource<?> source) {
		if (counter % eachElement == 0){
			counter = 0;
			source.sendPunctuation(object.getMetadata().getStart());
			logger.debug("Sending punctuation ... ");
		}
		counter++;
	}
	

	@Override
	public NElementHeartbeatGeneration<K, T> clone() {
		return new NElementHeartbeatGeneration<K, T>(this);
	}

}
