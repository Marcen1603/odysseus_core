package de.uniol.inf.is.odysseus.incubation.graph.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphEdge;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphNode;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Map-Function calculates and sets scores of entities based on the rules given by DEBS Grand Challenge '16.
 * 
 * @author Kristian Bruns
 */
public class ScoreUpdaterFunction extends AbstractFunction<Graph> {

	private static final long serialVersionUID = 2900875434766423329L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {{SDFGraphDatatype.GRAPH}, {SDFDatatype.LONG}};
	
	private String lastGraphStructure;
	private Map<String, Long> addedEntities = new HashMap<String, Long>();
	private List<String> commentsAddedToTotalScore = new ArrayList<String>();
	
	public ScoreUpdaterFunction() {
		super("scoreUpdater", 2, ScoreUpdaterFunction.ACC_TYPES, SDFGraphDatatype.GRAPH);
	}

	@Override
	public Graph getValue() {
		// Get graph and timeInterval.
		Graph graph = getInputValue(0);
		Long timeInterval = getInputValue(1);
		IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(graph.getName()).cloneDataStructure();
		
		Long creationTime = Long.valueOf(graph.getName().split("_")[1]);
		
		// Gucken ob ein neuer Post oder Kommentar hinzugefügt wurde.
		for (GraphNode node : structure.getGraphNodes().values()) {
			if ((node.getLabel().equals("post") || node.getLabel().equals("comment")) && !addedEntities.containsKey(node.getId())) {
				addedEntities.put(node.getId(), creationTime);
			}
			
			// Der muss hier stehen, da meist User -> Comment zuerst eingefügt wird und er ansonsten nicht mehr überprüfen würde.
			if (node.getLabel().equals("comment")) {
				String postId = this.getRelatedPost(node.getId(), structure.getGraphNodes());
				if (postId != null && !this.commentsAddedToTotalScore.contains(node.getId())) {
					Map<String, Object> props = structure.getGraphNode(postId).getProps();
					Long postScore = (Long) props.get("total_score");
					if (postScore > 0) {
						props.put("total_score", (Long) props.get("total_score") + (Long) structure.getGraphNode(node.getId()).getProps().get("single_score"));
						structure.getGraphNode(postId).setProps(props);
					}
					
					this.commentsAddedToTotalScore.add(node.getId());
				}
			}
		}
		
		// Single scores Updaten.
		if (lastGraphStructure != null) {
			for (Map.Entry<String, Long> entry : addedEntities.entrySet()) {
				Long tsOld = Long.valueOf((lastGraphStructure.split("_")[1])) - entry.getValue();
				Long tsNew = creationTime - entry.getValue();
						
				// Die Formel für die Berechnung wieivel vom Score abgezogen werden muss: 
				// ((int) (tsNew-creationTime)/fenstergroesse) - ((int) (tsOld-creationTime)/fenstergroesse)
				int oldRes = (int) (tsOld/timeInterval);
				int newRes = (int) (tsNew/timeInterval);
				int diff = newRes - oldRes;
				
				if (diff > 0) {
					Map<String, Object> props = structure.getGraphNode(entry.getKey()).getProps();
					props.put("single_score", (Long) props.get("single_score") - diff);
					structure.getGraphNode(entry.getKey()).setProps(props);
					
					// Total Score des Beitrags auch verringern.
					String postId = this.getRelatedPost(entry.getKey(), structure.getGraphNodes());
					if (postId != null) {
						Map<String, Object> postProps = structure.getGraphNode(postId).getProps();
						postProps.put("total_score", (Long)((Long) postProps.get("total_score") - diff));
						structure.getGraphNode(postId).setProps(postProps);
					}
				}
			}
		}
		
		lastGraphStructure = graph.getName();
		
		GraphDataStructureProvider.getInstance().setGraphVersionRead(lastGraphStructure, "scoreUpdater");
		
		return graph;
	}
	
	/**
	 * Calculate post related to a comment.
	 * 
	 * @param nodeId Id of comment.
	 * @param nodes All nodes from graph.
	 * 
	 * @return Id of related post.
	 */
	public String getRelatedPost(String nodeId, Map<String, GraphNode> nodes) {
		GraphNode node = nodes.get(nodeId);
		
		if (node.getLabel().equals("post")) {
			return node.getId();
		} else if (node.getOutgoingEdges().isEmpty()) {
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
