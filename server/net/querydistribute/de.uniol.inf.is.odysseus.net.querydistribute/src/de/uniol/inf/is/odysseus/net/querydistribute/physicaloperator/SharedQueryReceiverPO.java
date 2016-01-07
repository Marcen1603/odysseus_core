package de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator.SharedQueryReceiverAO;

@SuppressWarnings("rawtypes")
public class SharedQueryReceiverPO<T extends IStreamObject> extends AbstractSource<T> {

	public SharedQueryReceiverPO( SharedQueryReceiverAO operatorAO ) {
		
	}
	
	public SharedQueryReceiverPO( SharedQueryReceiverPO<T> other) {
		
	}
	
	@Override
	protected AbstractSource clone() throws CloneNotSupportedException {
		return new SharedQueryReceiverPO<T>(this);
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		
	}
	
	@Override
	protected void process_close() {
	}
}
