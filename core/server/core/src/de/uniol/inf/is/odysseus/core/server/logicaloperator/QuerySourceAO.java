package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PhysicalOperatorParameter;

/**
 * This Operator can be used to append a subquery to an existing 
 * physical plan
 * 
 * @author Marco Grawunder
 *
 */
@LogicalOperator(maxInputPorts=0, minInputPorts=0, name="QuerySource", doc="Attach a named query as source", category={LogicalOperatorCategory.SOURCE}, hidden = false)
public class QuerySourceAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -6717408016417095953L;

	IPhysicalOperator operator;
	int port = 0;
	
	public QuerySourceAO() {
		super();
	}
	
	public QuerySourceAO(QuerySourceAO other) {
		super(other);
		this.operator = other.operator;
		this.port = other.port;
	}

	@Parameter(type = PhysicalOperatorParameter.class, name = "operator", optional=false, doc="The name of the query that should deliver data or a tuple with queryname and operatorname")
	public void setQueryName(IPhysicalOperator operator){
		this.operator=  operator;
	}	

	public IPhysicalOperator getOperator() {
		return operator;
	}

	public int getPort() {
		return port;
	}

	@Parameter(type = IntegerParameter.class, name = "port", optional=true, doc="The number of the output port of the operator in the query that should be connect to.")
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return operator.getOutputSchema(port);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new QuerySourceAO(this);
	}

}
