package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * Logical operator for CountNodes.
 * 
 * @author Kristian Bruns
 */
@LogicalOperator(name="CountNodes", minInputPorts=1, maxInputPorts=1, doc="Count nodes in a graph", category={LogicalOperatorCategory.TRANSFORM})
public class CountNodesAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -6991939720638719339L;
	
	private String graphAttr = "";

	public CountNodesAO() {
		super();
	}
	
	public CountNodesAO(CountNodesAO countNodesAo) {
		super(countNodesAo);
		this.graphAttr = countNodesAo.graphAttr;
	}
	
	@Parameter(type=StringParameter.class, name="GRAPHATTRIBUTE", optional=false, isList=false, doc="name of attribute contains graphstructure")
	public void setGraphAttribute(String graphAttribute) {
		this.graphAttr = graphAttribute;
	}
	
	@GetParameter(name="GRAPHATTRIBUTE")
	public String getGraphAttribute() {
		return this.graphAttr;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new CountNodesAO(this);
	}

}
