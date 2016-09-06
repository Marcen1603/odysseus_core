package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name="SearchNode", doc="Searching for node in Graph Structure", category={LogicalOperatorCategory.TRANSFORM})
public class SearchNodeAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 4455941120263904312L;
	
	private String nodeLabel;
	private List<NamedExpression> nodeProperties;
	private String structureName;
	private IGraphDataStructure<IMetaAttribute> structure;
	
	public SearchNodeAO() {
		super();
	}
	
	public SearchNodeAO (SearchNodeAO searchNode) {
		super(searchNode);
		this.nodeLabel = searchNode.nodeLabel;
		this.nodeProperties = searchNode.nodeProperties;
		this.structureName = searchNode.structureName;
		this.structure = searchNode.structure;
	}
	
	@Override
	public void initialize() {
		this.structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(this.structureName);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SearchNodeAO(this);
	}
	
	@Parameter(type=StringParameter.class, name="STRUCTURENAME", optional=false, isList=false, doc="structure name")
	public void setStructureName(String structureName) {
		this.structureName = structureName;
	}
	
	@Parameter(type=StringParameter.class, name="NODELABEL", optional=false, isList=false, doc="node label")
	public void setNodeLabel (String nodeLabel) {
		this.nodeLabel = nodeLabel;
	}
	
	@Parameter(type=NamedExpressionParameter.class, name="NODEPROPERTIES", optional=false, isList=true, doc="node properties")
	public void setNodeProperties (List<NamedExpression> nodeProperties) {
		this.nodeProperties = nodeProperties;
	}
	
	@GetParameter(name="NODELABEL")
	public String getNodeLabel() {
		return this.nodeLabel;
	}
	
	@GetParameter(name="NODEPROPERTIES")
	public List<NamedExpression> getNodeProperties() {
		return this.nodeProperties;
	}
	
	@GetParameter(name="STRUCTURENAME")
	public String getStructureName() {
		return this.structureName;
	}
	
	public IGraphDataStructure<IMetaAttribute> getGraphDataStructure() {
		return this.structure;
	}

}
