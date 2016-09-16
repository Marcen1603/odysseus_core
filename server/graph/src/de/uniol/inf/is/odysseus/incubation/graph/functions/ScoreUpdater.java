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
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphEdge;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphNode;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;

public class ScoreUpdater<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -4079751252782432026L;

//	private Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
	private Map<String, Long> postScores = new HashMap<String, Long>();
	private Map<String, List<String>> postComments = new HashMap<String, List<String>>();
	private String graphName;
	private Graph graph;
	private String outdated;
	
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
		Graph graph = newElement.getAttribute(0);
		
		String regExp = "[\\_]{1}[0-9]*";
		
		this.graphName = graph.getName().replaceFirst(regExp, "");
		
		// Muss hier geklont werden oder kann ich das auch so verändern????? Erstmal ohne klonen probieren!
		IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(graph.getName()).cloneDataStructure();
		
		Map<String, GraphNode> graphNodes = structure.getGraphNodes();
		
		// Bestimme total_scores und comments bzw. user zu posts.
		for (GraphNode node : graphNodes.values()) {
			if (node.getLabel().equals("post")) {
				Long score = 0l;
				if (postScores.containsKey(node.getId())) {
					score += postScores.get(node.getId()).longValue();
				}
				score += (Long) node.getProps().get("single_score");
				postScores.put(node.getId(), score);
			} else if (node.getLabel().equals("comment")) {
				String postId = this.getRelatedPost(node.getId(), graphNodes);
				if (postId != null) {
					Long score = 0l;
					if (postScores.containsKey(postId)) {
						score += postScores.get(postId);
					}
					score += (Long) node.getProps().get("single_score");
					postScores.put(postId, score);
					
					// Füge Kommentar zu richtigem Post hinzu.
					if (postComments.containsKey(postId)) {
						List<String> comments = postComments.get(postId);
						comments.add(node.getId());
						postComments.put(postId, comments);
					} else {
						List<String> comments = new ArrayList<String>();
						comments.add(node.getId());
						postComments.put(postId, comments);
					}
				}
			}
		}
		
		// Total Score für Posts setzen
		for (Map.Entry<String, Long> entry : postScores.entrySet()) {
			GraphNode node = structure.getGraphNode(entry.getKey());
			Map<String, Object> props = node.getProps();
			props.put("total_score", entry.getValue());
			node.setProps(props);
		}
		
//		Tuple<IMetaAttribute> res = new Tuple<IMetaAttribute>(2, false);
//		res.setAttribute(0, newElement.getAttribute(0));
//		res.setAttribute(1, "false");
//		res.setMetadata(newElement.getMetadata().clone());
		
//		tuple = res.clone();
		
		String graphName = GraphDataStructureProvider.getInstance().addGraphDataStructure(structure, newElement.getMetadata().getStart());
		Graph newGraph = new Graph(graphName);
		
		this.graph = newGraph;
		this.outdated = "false";
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure("test").cloneDataStructure();
		
		PointInTime removeTime = pointInTime.minus(new PointInTime(60000));
		
		List<String> postsToRemove = new ArrayList<String>();
		List<String> commentsToRemove = new ArrayList<String>();
		
		for (GraphNode node : structure.getGraphNodes().values()) {
				// Ist Knoten ein Post oder ein Kommentar, wird Score verringert.
				if (node.getLabel().equals("post")) {
					Map<String, Object> props = node.getProps();
					// Verringere single_score. Der single_score kann nicht unter 0 fallen.
					Long singleScore = ((Long) props.get("single_score")) - 1;
					if (singleScore < 0) {
						singleScore = 0l;
					}
					
					// Verringere total_score. Wenn total_score unter 0 fällt, wird post aus Graphen entfernt.
					// Ansonsten setze properties.
					Long totalScore = ((Long) props.get("total_score")) - 1;
					if (totalScore <= 0) {
						postsToRemove.add(node.getId());
					} else {
						props.put("total_score", totalScore);
						props.put("single_score", singleScore);
						node.setProps(props);
						postScores.put(node.getId(), totalScore);
					}
				} else if (node.getLabel().equals("comment")) {
					String postId = this.getRelatedPost(node.getId(), structure.getGraphNodes());
					Map<String, Object> postProps = structure.getGraphNode(postId).getProps();
					Map<String, Object> props = node.getProps();
					Long singleScore = ((Long) props.get("single_score")) - 1;
					// Ist single_score = 0 wird Kommentar aus System entfernt.
					if (singleScore <= 0) {
						commentsToRemove.add(node.getId());
					} else {
						props.put("single_score", singleScore);
					}
					
					Long totalScore = ((Long) postProps.get("total_score")) - 1;
					// Ist total_score = 0, wird Beitrag entfernt.
					if (totalScore <= 0) {
						postsToRemove.add(postId);
					} else {
						postProps.put("total_score", totalScore);
						postScores.put(postId, totalScore);
					}
			}
		}
		
		// Entferne alle Posts und alle Kommentare die zu dem Post gehören.
		for (String postId : postsToRemove) {
			structure.removeGraphNode(postId);
			for (String relComment : postComments.get(postId)) {
				structure.removeGraphNode(relComment);
			}
		}
		
		// Entferne alle Kommentare
		for (String commentId : commentsToRemove) {
			structure.removeGraphNode(commentId);
		}
		
		String name = GraphDataStructureProvider.getInstance().addGraphDataStructure(structure, pointInTime);
		
		this.graph = new Graph(name);
		this.outdated = "true";
		
//		Tuple<IMetaAttribute> res = new Tuple<IMetaAttribute>(2, false);
//		res.setAttribute(0, newGraph);
//		res.setAttribute(1, "true");
//		res.setMetadata(new TimeInterval(pointInTime, PointInTime.INFINITY));
//		
//		tuple = res;		
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		return new Object[]{this.graph, this.outdated};
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		List<SDFAttribute> res = new ArrayList<SDFAttribute>();
		SDFAttribute attr = new SDFAttribute("graph", "graph", SDFGraphDatatype.GRAPH);
		SDFAttribute attr2 = new SDFAttribute("outdated", "outdated", SDFDatatype.STRING);
		res.add(attr);
		res.add(attr2);
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
	
	public String getRelatedPost(String nodeId, Map<String, GraphNode> nodes) {
		GraphNode node = nodes.get(nodeId);
		
		if (node.getOutgoingEdges().isEmpty()) {
			return null;
		}
		
		while (!node.getLabel().equals("post")) {
			for (GraphEdge edge : node.getOutgoingEdges().keySet()) {
				if (edge.getLabel().equals("is_comment_of") || edge.getLabel().equals("is_reply_of")) {
					node = edge.getEndingNotes().get(0);
				}
			}
		}
		
		return node.getId();
	}

}
