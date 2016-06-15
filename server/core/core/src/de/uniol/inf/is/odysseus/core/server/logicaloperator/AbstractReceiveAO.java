package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.util.Constants;

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
			String protocol, String datahandler, Map<String, String> options) {
		super(resource, wrapper, transport, protocol,datahandler,options);
	}
	
}
