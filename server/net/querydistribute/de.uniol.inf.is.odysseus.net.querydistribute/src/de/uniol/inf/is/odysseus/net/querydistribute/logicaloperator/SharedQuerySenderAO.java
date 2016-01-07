package de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "SHAREDQUERYSENDER", doc = "Send data to other node of same query", minInputPorts = 1, maxInputPorts = 1, category={LogicalOperatorCategory.SINK}, hidden = true)
public class SharedQuerySenderAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;
	
	private String connectionID;
	private String odysseusNodeID;

	public SharedQuerySenderAO() {
		super();
	}
	
	public SharedQuerySenderAO( SharedQuerySenderAO other ) {
		super(other);
		
		this.connectionID = other.connectionID;
		this.odysseusNodeID = other.odysseusNodeID;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SharedQuerySenderAO(this);
	}

	@Parameter(name = "CONNECTIONID", doc = "Connection UUID to identify connections between nodes", type = StringParameter.class, optional = false)
	public void setConnectionID(String uuidString) {
		connectionID = uuidString;
	}

	public String getConnectionID() {
		return connectionID;
	}

	@Parameter(name = "ODYSSEUSNODEID", doc = "OdysseusNodeID to identify the node to connect to", type = StringParameter.class, optional = false)
	public void setOdysseusNodeID(String odysseusNodeIDString) {
		odysseusNodeID = odysseusNodeIDString;
	}

	public String getOdysseusNodeID() {
		return odysseusNodeID;
	}

	@Override
	public boolean isSourceOperator() {
		return false;
	}
}
