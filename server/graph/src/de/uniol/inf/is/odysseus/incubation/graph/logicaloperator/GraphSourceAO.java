package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.incubation.graph.physicaloperator.access.transport.GraphTransportHandler;

/**
 * Logical GraphSource operator.
 * 
 * @author Kristian Bruns
 */
@LogicalOperator(maxInputPorts=0, minInputPorts=0, name="GRAPHSOURCE", doc="Operator to use GraphStructure in another Query than Graphbuilder.", category={LogicalOperatorCategory.SOURCE})
public class GraphSourceAO extends AbstractAccessAO {
	
	private static final long serialVersionUID = -3359040496771454107L;

	public GraphSourceAO() {		
		setTransportHandler(GraphTransportHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PUSH);
	}
	
	public GraphSourceAO(GraphSourceAO other) {
		super(other);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new GraphSourceAO(this);
	}
	
	@Parameter(type=StringParameter.class, name="structureName", optional=false, isList=false, doc="Name of Graph to use")
	public void setStructureName(String structureName) {
		addOption(GraphTransportHandler.STRUCTURENAME, structureName);
	}
	
	@GetParameter(name="structureName")
	public String getStructureName() {
		String structureName = getOption(GraphTransportHandler.STRUCTURENAME);
		if (structureName != null) {
			return structureName;
		} else {
			return "";
		}
	}

}
