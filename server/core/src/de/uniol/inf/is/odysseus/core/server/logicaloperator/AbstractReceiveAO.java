package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
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
	
	public AbstractReceiveAO(Resource resource, String wrapper, String transport,
			String protocol, String datahandler, OptionMap options) {
		super(resource, wrapper, transport, protocol,datahandler,options);
	}
	
}
