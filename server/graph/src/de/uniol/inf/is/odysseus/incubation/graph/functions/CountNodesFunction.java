package de.uniol.inf.is.odysseus.incubation.graph.functions;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Map-Function for counting nodes in a graph.
 * 
 * @author Kristian Bruns
 */
public class CountNodesFunction extends AbstractFunction<Long> {
	
	private static final long serialVersionUID = 7964522823576486110L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFGraphDatatype.GRAPH}
	};
	
	public CountNodesFunction() {
		super("countNodes", 1, CountNodesFunction.ACC_TYPES, SDFDatatype.LONG);
	}

	@Override
	public Long getValue() {	
		// Get graph.
		Graph graph = getInputValue(0);
		IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(graph.getName());
		
		// Mark this graph as read by this operator.
		GraphDataStructureProvider.getInstance().setGraphVersionRead(graph.getName(), "countNodesFunction");
		
		Integer value = structure.getGraphNodes().size();
		return value.longValue();
	}
}
