package de.uniol.inf.is.odysseus.incubation.graph.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphEdge;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphNode;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;

public class GraphIntersection <M extends ITimeInterval, T extends Tuple<M>> extends AbstractNonIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -4772454825738888335L;

	public GraphIntersection() {
		super();
	}
	
	public GraphIntersection(final String outputAttributeName) {
		super(null, false, new String[] {outputAttributeName});
	}
	
	public GraphIntersection(final GraphIntersection<M, T> other) {
		super(other);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	public Object[] evaluate(Collection elements, Tuple trigger, PointInTime pointInTime) {
		Tuple tuple = new Tuple(1, false);
		
		IGraphDataStructure<IMetaAttribute> intersection = null;
		
		for (Object element : elements) {
			tuple = (T) element;
			Graph graph = ((Graph) tuple.getAttribute(0));
			
			IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(graph.getName());
			Map<String, GraphNode> graphNodes = structure.getGraphNodes();
			
			if (intersection == null) {
				intersection = structure.cloneDataStructure();
			} else {		
				Map<Pair<String, String>, GraphEdge> relations = structure.getRelations();
				Map<Pair<String, String>, GraphEdge> relIntersect = intersection.getRelations();
				Map<String, GraphNode> nodes = new HashMap<String, GraphNode>(structure.getGraphNodes());
				Map<String, GraphNode> nodesIntersect = new HashMap<String, GraphNode>(intersection.getGraphNodes());
				
				intersection.clearDataStructure();
				
				for (Map.Entry<Pair<String, String>, GraphEdge> relation : relations.entrySet()) {
					if (relIntersect.containsKey(relation.getKey()) && (relIntersect.get(relation.getKey()).equals(relation.getValue()))) {
						GraphNode startingNode = graphNodes.get(relation.getKey().getE1());
						startingNode.clearEdges();
						
						GraphNode endingNode = structure.getGraphNodes().get(relation.getKey().getE2());
						endingNode.clearEdges();
						
						intersection.addRelation(startingNode, endingNode, relation.getValue());
						nodes.remove(startingNode);
						nodes.remove(endingNode);
						nodesIntersect.remove(startingNode);
						nodesIntersect.remove(endingNode);
					}
				}
				
				for (Map.Entry<String, GraphNode> entry : nodes.entrySet()) {
					if (nodesIntersect.containsKey(entry.getKey())) {
						intersection.addGraphNode(entry.getValue());
					}
				}
			}
		}
		
		String name = GraphDataStructureProvider.getInstance().addGraphDataStructure(intersection, pointInTime);
		Graph graph = new Graph(name);
		
		return new Graph[]{graph};
	}

	@Override
	public boolean needsOrderedElements() {
		return false;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> res = new ArrayList<>(1);
		res.add(new SDFAttribute(null, "graph", SDFGraphDatatype.GRAPH, null, null, null));
		return res;
//		return Collections.singleton(new SDFAttribute(null, outputAttributeNames[0], SDFGraphDatatype.GRAPH, null, null, null));
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return true;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		String outputName = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters, AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES);
		if (outputName == null) {
			outputName = "graph";
		}
		return new GraphIntersection<>(outputName);
	}

}
