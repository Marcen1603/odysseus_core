package de.uniol.inf.is.odysseus.intervalapproach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

public class DefaultHeartbeatGeneration<K extends ITimeInterval, T extends IMetaAttributeContainer<K>> implements IHeartbeatGenerationStrategy<T>{

	Logger logger = LoggerFactory.getLogger(DefaultHeartbeatGeneration.class);
	
	@Override
	public void generateHeartbeat(T object, ISource source) {
		source.sendPunctuation(object.getMetadata().getStart());
		logger.debug("Send Heartbeat"+object.getMetadata().getStart());
	}

	@Override
	public DefaultHeartbeatGeneration clone() {
		return new DefaultHeartbeatGeneration();
	}

		
}