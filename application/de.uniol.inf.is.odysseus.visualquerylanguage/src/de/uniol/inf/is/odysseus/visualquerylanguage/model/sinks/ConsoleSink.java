package de.uniol.inf.is.odysseus.visualquerylanguage.model.sinks;

import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

@SuppressWarnings("unchecked")
public class ConsoleSink extends AbstractSink{

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		System.out.println("Port:" + port + ", Object:" + object.toString());
	}
	
	@Override
	public ConsoleSink clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
