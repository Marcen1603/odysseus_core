package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "ACCESS")
public class AccessAO extends AbstractAccessAO {

	private static final long serialVersionUID = 3913899451565703944L;
	
	public AccessAO() {
		super();
	}

	public AccessAO(AccessAO other){
		super(other);
	}
	
	public AccessAO(Resource resource, String wrapper, String transport,
			String protocol, String datahandler, Map<String, String> options) {
		super(resource, wrapper, transport, protocol,datahandler,options);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new AccessAO(this);
	}
	
}
