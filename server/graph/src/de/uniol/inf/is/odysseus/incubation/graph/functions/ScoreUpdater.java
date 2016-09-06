package de.uniol.inf.is.odysseus.incubation.graph.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphNode;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;

public class ScoreUpdater<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -4079751252782432026L;
	
	Graph graphElement;
	Graph postGraphElement;
	String outdated;
	String actualDataStructure;
	Map<String, String> actualPostDataStructures = new HashMap<String, String>();
	
	public ScoreUpdater() {
		super();
	}
	
	public ScoreUpdater(final ScoreUpdater<M, T> other) {
		super(other);
	}
	
	public ScoreUpdater(final int[] attributes, final String[] outputNames) {
		super(attributes, outputNames);
	}
	
	public ScoreUpdater(final int inputAttributesLength, final String[] outputNames) {
		super(null, outputNames);
	}

	@Override
	public void addNew(T newElement) {
		// Rufe beide Graphen (Gesamtnetzwerk, Postdatenstruktur) ab.
		Graph graph = newElement.getAttribute(0);
		Graph postGraph = newElement.getAttribute(1);
		
		IGraphDataStructure<IMetaAttribute> postStructure = GraphDataStructureProvider.getInstance().getGraphDataStructure(postGraph.getName());
		
		// Aktualisieren der neuesten Datenstrukturen
		this.actualDataStructure = graph.getName();
		this.actualPostDataStructures.put(postGraph.getNode1(), postGraph.getName());
		
		// Bestimme total_score
		Long totalScore = 0l;
		for (GraphNode node : postStructure.getGraphNodes().values()) {
			if (!(node.getLabel().equals("user"))) {
				totalScore += (Long) node.getProps().get("single_score");
			}
		}
		
		//Setze total_score für Post
		GraphNode postNode = postStructure.getGraphNode(postGraph.getNode1());
		Map<String, Object> props = postNode.getProps();
		props.put("total_score", totalScore);
		postNode.setProps(props);
		postNode = GraphDataStructureProvider.getInstance().getGraphDataStructure(graph.getName()).getGraphNode(postGraph.getNode1());
		postNode.setProps(props);
		
		graphElement = graph;
		postGraphElement = postGraph;
		outdated = "false";
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		for (T removeElement : outdatedElements) {
			// Bestimme beide Graphen.
			Graph graph = removeElement.getAttribute(0);
			Graph postGraph = removeElement.getAttribute(1);
			
			IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(this.actualDataStructure);
			IGraphDataStructure<IMetaAttribute> postStructure = GraphDataStructureProvider.getInstance().getGraphDataStructure(actualPostDataStructures.get(postGraph.getNode1()));
			
			// Betroffene Knoten.
			String node1 = graph.getNode1();
			String node2 = graph.getNode2();
			
			// Setze single_score des Knotens und total_score des Posts herunter unf füge diese in die jeweiligen Properties ein.
			// Ist Single_Score 0, wird Element entfernt. Bei Posts nur, wenn total_score 0 ist.
			if (node1 != null && (node1.contains("post") || node1.contains("comment"))) {
				Map<String, Object> props = structure.getGraphNode(node1).getProps();
				Map<String, Object> postProps = structure.getGraphNode(postGraph.getNode1()).getProps();
				
				Long singleScore = ((Long) props.get("single_score")) -1;
				props.put("single_score", singleScore);
				Long totalScore = ((Long) postProps.get("total_score"));
				postProps.put("total_score", totalScore);
				
				if (totalScore == 0) {
					structure.removeGraphNode(postGraph.getNode1());
					structure.removeGraphNode(node1);
					GraphDataStructureProvider.getInstance().removeGraphDataStructure(postGraph.getNode1());
				} else if (singleScore == 0 && !(node1.contains("post"))) {
					structure.removeGraphNode(node1);
					postStructure.removeGraphNode(node1);
					
					structure.getGraphNode(postGraph.getNode1()).setProps(postProps);
					postStructure.getGraphNode(postGraph.getNode1()).setProps(postProps);
				} else {
					structure.getGraphNode(node1).setProps(props);
					structure.getGraphNode(postGraph.getNode1()).setProps(postProps);
					
					postStructure.getGraphNode(node1).setProps(props);
					postStructure.getGraphNode(postGraph.getNode1()).setProps(postProps);
				}
			}
			
			// Gleiches für node2.
			if (node2 != null && (node2.contains("post") || node2.contains("comment"))) {
				Map<String, Object> props = GraphDataStructureProvider.getInstance().getGraphDataStructure(this.actualDataStructure).getGraphNode(node2).getProps();
				Map<String, Object> postProps = GraphDataStructureProvider.getInstance().getGraphDataStructure(this.actualDataStructure).getGraphNode(postGraph.getNode1()).getProps();
				
				Long singleScore = ((Long) props.get("single_score"));
				props.put("single_score", singleScore);
				Long totalScore = ((Long) postProps.get("total_score"));
				postProps.put("total_score", totalScore);
				
				if (totalScore == 0) {
					structure.removeGraphNode(postGraph.getNode1());
					structure.removeGraphNode(node2);
					GraphDataStructureProvider.getInstance().removeGraphDataStructure(postGraph.getNode1());
				} else if (singleScore == 0 && !(node2.contains("post"))) {
					structure.removeGraphNode(node2);
					postStructure.removeGraphNode(node2);
					
					structure.getGraphNode(postGraph.getNode1()).setProps(postProps);
					postStructure.getGraphNode(postGraph.getNode1()).setProps(postProps);
				} else {
					structure.getGraphNode(node2).setProps(props);
					structure.getGraphNode(postGraph.getNode1()).setProps(postProps);
					
					postStructure.getGraphNode(node2).setProps(props);
					postStructure.getGraphNode(postGraph.getNode1()).setProps(postProps);
				}
			}
			
			PointInTime diff = removeElement.getMetadata().getEnd().minus(removeElement.getMetadata().getStart());
			PointInTime tmp = removeElement.getMetadata().getEnd();
			removeElement.getMetadata().setEnd(tmp.plus(diff));
			removeElement.getMetadata().setStart(tmp);

			// aktuellste Datenstrukturen setzen
			Graph newGraph = new Graph(this.actualDataStructure, graph.getNode1(), graph.getNode2());
			Graph newPostGraph = new Graph(this.actualPostDataStructures.get(postGraph.getNode1()), postGraph.getNode1(), postGraph.getNode2());
			
			graphElement = newGraph;
			postGraphElement = newPostGraph;
			outdated = "true";
		}
		
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		return new Object[]{graphElement, postGraphElement, outdated};
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> res = new ArrayList<>(1);
		res.add(new SDFAttribute(null, "graph", SDFGraphDatatype.GRAPH, null, null, null));
		res.add(new SDFAttribute(null, "post_graph", SDFGraphDatatype.GRAPH, null, null, null));
		res.add(new SDFAttribute(null, "outdated", SDFDatatype.STRING, null, null, null));
		return res;
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return true;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return new ScoreUpdater<M, T>();
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new ScoreUpdater<M, T>(this);
	}

}
