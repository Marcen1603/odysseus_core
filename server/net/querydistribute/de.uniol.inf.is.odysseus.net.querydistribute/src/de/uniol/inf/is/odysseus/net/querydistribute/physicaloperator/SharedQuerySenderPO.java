package de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator.SharedQuerySenderAO;

@SuppressWarnings("rawtypes")
public class SharedQuerySenderPO<T extends IStreamObject> extends AbstractSource<T> {

	public SharedQuerySenderPO( SharedQuerySenderAO operatorAO ) {
		
	}
	
	public SharedQuerySenderPO( SharedQuerySenderPO<T> other) {
		
	}
	
	@Override
	protected AbstractSource clone() throws CloneNotSupportedException {
		return new SharedQuerySenderPO<T>(this);
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		
	}
	
	@Override
	protected void process_close() {
	}
}
