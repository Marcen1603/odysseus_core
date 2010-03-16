package de.uniol.inf.is.odysseus.broker.executor;

import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

public class ConsoleSink extends AbstractSink<Object>{

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		System.out.println("Port: "+port+"\t:"+object);		
	}

}
