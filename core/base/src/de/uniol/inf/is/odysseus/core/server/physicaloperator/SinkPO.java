package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

public class SinkPO<R> extends AbstractSink<R> {

	@Override
	protected void process_next(R object, int port, boolean isReadOnly) {		
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {		
	}

	@Override
	public AbstractSink<R> clone() {
		return null;
	}


}
