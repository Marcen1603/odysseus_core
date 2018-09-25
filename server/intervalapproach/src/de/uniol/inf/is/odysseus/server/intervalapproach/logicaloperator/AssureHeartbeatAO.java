package de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.HeartbeatAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;


@LogicalOperator(minInputPorts = 0, maxInputPorts = 1, name = "AssureHeartbeat", category = { LogicalOperatorCategory.PROCESSING }, doc = "Deprecated. Use Heartbeat instead!", deprecation=true)
@Deprecated
public class AssureHeartbeatAO extends HeartbeatAO {

	private static final long serialVersionUID = 1947259804373536156L;


	public AssureHeartbeatAO() {
	}

	public AssureHeartbeatAO(AssureHeartbeatAO other) {
		super(other);
	}


	@Override
	public AbstractLogicalOperator clone() {
		return new AssureHeartbeatAO(this);
	}

}
