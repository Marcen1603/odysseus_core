package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;


public abstract class TimestampToPayloadPO<K extends ITimeInterval, T extends IMetaAttributeContainer<K>> extends AbstractPipe<T, T> {

	public TimestampToPayloadPO(){}
	
	public TimestampToPayloadPO(TimestampToPayloadPO<K, T> timestampToPayloadPO) {
		super(timestampToPayloadPO);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
