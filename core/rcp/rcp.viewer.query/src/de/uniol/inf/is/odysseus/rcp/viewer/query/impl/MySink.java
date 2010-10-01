package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;

public class MySink extends AbstractSink<Object> {

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		System.out.println("Port:" + port + ", Object:" + object.toString());
	}

	@Override
	public MySink clone()  {
		return new MySink();
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
//		System.out.println("Port:" + port + ", PUNCTUATION: " + timestamp);
	}

	
}
