package de.uniol.inf.is.odysseus.planmanagement.executor.console;

import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

@SuppressWarnings("unchecked")
public class MySink extends AbstractSink {

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		System.out.println("Port:" + port + ", Object:" + object.toString());
	}

	
}
