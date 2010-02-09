package de.uniol.inf.is.odysseus.broker.physicaloperator;

import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

public class BrokerPO<T> extends AbstractPipe<T, T> {

	public BrokerPO(BrokerPO<T> po){
		
	}

	public BrokerPO(){
		
	}
	
	@Override
	protected void process_next(T object, int port) {
		System.out.println("Broker invoked...");
		transfer(object);		
		
	}

	@Override
	public OutputMode getOutputMode() {		// 
		return OutputMode.INPUT;
	}

}
