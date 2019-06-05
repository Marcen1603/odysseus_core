package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.util.Constants;

abstract public class AbstractReceiveAO extends AbstractAccessAO {

	private static final long serialVersionUID = 3913899451565703944L;
	
	public AbstractReceiveAO() {
		super();
		setWrapper(Constants.GENERIC_PUSH);
	}

	public AbstractReceiveAO(AbstractReceiveAO other){
		super(other);
	}

	
}
