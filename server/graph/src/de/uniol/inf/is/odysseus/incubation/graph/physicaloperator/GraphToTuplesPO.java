package de.uniol.inf.is.odysseus.incubation.graph.physicaloperator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphEdge;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphNode;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;

public class GraphToTuplesPO<M extends IMetaAttribute> extends AbstractPipe<Tuple<M>, Tuple<M>> {
	
	public GraphToTuplesPO() {
		super();
	}
	
	public GraphToTuplesPO(GraphToTuplesPO<M> graphToKeyValuePO) {
		super(graphToKeyValuePO);
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Tuple<M> object, int port) {
		Graph graph = object.getAttribute(0);
		IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(graph.getName());
		
		Map<String, GraphNode> graphNodes = structure.getGraphNodes();

		Tuple<M> output = new Tuple<M>(1, false);
		output.setMetadata((M) object.getMetadata().clone());
		
		Map<Pair<String, String>, GraphEdge> map = structure.getRelations();
		for (Map.Entry<Pair<String, String>, GraphEdge> entry: map.entrySet()) {
			output.setAttribute(0, entry.getValue().toString());
			
			graphNodes.remove(entry.getKey().getE1());
			graphNodes.remove(entry.getKey().getE2());
			
			transfer(output);
		}
		
		for (GraphNode node : graphNodes.values()){
			output.setAttribute(0, node.toString());
			transfer(output);
		}
	}	
}
