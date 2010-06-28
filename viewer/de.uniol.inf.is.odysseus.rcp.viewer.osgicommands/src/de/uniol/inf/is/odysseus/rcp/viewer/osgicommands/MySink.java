package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

public class MySink extends AbstractSink<Object> {

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		System.out.println("Port:" + port + ", Object:" + object.toString());
	}

	@Override
	public MySink clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		System.out.println("Port:" + port + ", PUNCTUATION: " + timestamp);
	}

	
}
