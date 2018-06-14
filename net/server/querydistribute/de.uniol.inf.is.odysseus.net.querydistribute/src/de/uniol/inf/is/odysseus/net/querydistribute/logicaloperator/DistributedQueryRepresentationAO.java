package de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator;

import java.util.UUID;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "DISTRIBUTEDQUERYREPRESENTATIONAO", doc = "Represents a fully distributed query", minInputPorts = 0, maxInputPorts = 0, category = { LogicalOperatorCategory.TEST }, hidden = true)
public class DistributedQueryRepresentationAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;

	private UUID sharedQueryID;

	public DistributedQueryRepresentationAO(UUID sharedQueryID) {
		this.sharedQueryID = sharedQueryID;
	}
	
	public DistributedQueryRepresentationAO() {
	}

	public DistributedQueryRepresentationAO(DistributedQueryRepresentationAO copy) {
		this.sharedQueryID = copy.sharedQueryID;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DistributedQueryRepresentationAO(this);
	}

	@Parameter(name = "SHAREDQUERYIDSTRING", doc = "Shared query id which is fully distributed", type = StringParameter.class, optional = false)
	public void setSharedQueryIDString(String idString) {
		sharedQueryID = UUID.fromString(idString);
	}

	public String getSharedQueryIDString() {
		return sharedQueryID.toString();
	}

	public UUID getSharedQueryID() {
		return sharedQueryID;
	}
}
