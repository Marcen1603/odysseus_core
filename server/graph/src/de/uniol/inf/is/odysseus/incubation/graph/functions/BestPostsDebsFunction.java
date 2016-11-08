package de.uniol.inf.is.odysseus.incubation.graph.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphEdge;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphNode;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Map-Function calculating the best posts based on the rules given by DEBS Grand Challenge '16.
 * 
 * @author Kristian Bruns
 */
public class BestPostsDebsFunction extends AbstractFunction<Tuple<IMetaAttribute>> {
	private static final long serialVersionUID = -8341087533326456443L;
	
	private Map<String, PointInTime> postTs = new HashMap<String, PointInTime>();
	private Map<String, PointInTime> postCommentTs = new HashMap<String, PointInTime>();
	private Map<String, Long> postScores = new HashMap<String, Long>();
	private Map<String, List<String>> postComments = new HashMap<String, List<String>>();
	private Map<String, String> postUser = new HashMap<String, String>();
	private long numPosts;
	
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFGraphDatatype.GRAPH}, {SDFDatatype.LONG}
	};
	
	public BestPostsDebsFunction() {
		super("bestPostsDebs", 2, BestPostsDebsFunction.ACC_TYPES, SDFDatatype.TUPLE);
	}

	@Override
	public Tuple<IMetaAttribute> getValue() {
		// Get graph and number of posts to calculate.
		Graph graph = getInputValue(0);
		this.numPosts = getInputValue(1);
		IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(graph.getName()).cloneDataStructure();
		
		for (GraphNode node: structure.getGraphNodes().values()) {
			if (node.getLabel().equals("post")) {
				// Erstellungszeitpunkt setzen falls noch nicht vorhanden.
				if (!postTs.containsKey(node.getId())) {
					postTs.put(node.getId(), new PointInTime(Long.valueOf(graph.getName().split("_")[1])));
				}
				
				// Save post Score
				postScores.put(node.getId(), (Long) node.getProps().get("total_score"));
				
				// Get related user of post.
				String userId = this.getRelatedUser(node.getId(), structure.getGraphNodes());
				if (userId != null) {
					postUser.put(node.getId(), userId);
				}
			} else if (node.getLabel().equals("comment")) {
				// Get related post of comment.
				String postId = this.getRelatedPost(node.getId(), structure.getGraphNodes());
				if (postId != null) {
					// Set creation time of post comment to creation time of this comment.
					postCommentTs.put(postId, new PointInTime(Long.valueOf(graph.getName().split("_")[1])));
					List<String> comments;
					if (postComments.containsKey(postId)) {
						comments = postComments.get(postId);
						if (!comments.contains(node.getId())) {
							comments.add(node.getId());
							postComments.put(postId, comments);
						}
					} else {
						comments = new ArrayList<String>();
						comments.add(node.getId());
						postComments.put(postId, comments);
					}
				}
			}
		}
		
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>((int) this.numPosts, false);
		
		// Bestimmung der drei besten Beiträge.
		for (int i=0; i<this.numPosts; i++) {
			if (!postScores.isEmpty()) {
				String bestPost = Collections.max(
					postScores.entrySet(),
					new Comparator<Map.Entry<String, Long>>() {
						@Override
						public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
							// Vergleich welcher Post besser ist.
							int res = o1.getValue().compareTo(o2.getValue());
							// Wenn Werte gleich sind, überprüfen, ob Posts unterschiedlichen Zeitstempel haben.
							if (res == 0) {
								if (postTs.containsKey(o1.getKey()) && postTs.containsKey(o2.getKey())) {
									if (postTs.get(o1.getKey()).before(postTs.get(o2.getKey()))) {
										res --;
									} else if (postTs.get(o1.getKey()).after(postTs.get(o2.getKey()))) {
										res ++;
									}
								}
							}
							
							// Wenn Ergebnis immernoch 0 ist, auch für Kommentare überprüfen.
							if (res == 0) {
								if (postCommentTs.containsKey(o1.getKey()) && postCommentTs.containsKey(o2.getKey())) {
									if (postCommentTs.get(o1.getKey()).before(postCommentTs.get(o2.getKey()))) {
										res--;
									} else if (postCommentTs.get(o1.getKey()).after(postCommentTs.get(o2.getKey()))) {
										res ++;
									}
								} else if (postCommentTs.containsKey(o1.getKey()) && !postCommentTs.containsKey(o2.getKey())) {
									res ++;
								} else if (!postCommentTs.containsKey(o1.getKey()) && postCommentTs.containsKey(o2.getKey())) {
									res --;
								}
							}
							
							return res;
						}
						
					}
				).getKey();
				
				if (postScores.get(bestPost) > 0) {
					Tuple<IMetaAttribute> postTuple = new Tuple<IMetaAttribute>(4, false);
					postTuple.setAttribute(0, bestPost);
					postTuple.setAttribute(1, postUser.get(bestPost));
					postTuple.setAttribute(2, postScores.get(bestPost));
					
					if (postComments.containsKey(bestPost)) {
						postTuple.setAttribute(3, postComments.get(bestPost).size());
					} else {
						postTuple.setAttribute(3, 0l);
					}
					
					tuple.setAttribute(i, postTuple);
				}
				postScores.remove(bestPost);
			}
		}
		
		GraphDataStructureProvider.getInstance().setGraphVersionRead(graph.getName(), "bestPostsDebs");
		
		return tuple;
	}
	
	/**
	 * Calculate related post of a given comment or user.
	 * 
	 * @param nodeId Id of comment or user.
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
				if (edge.getLabel().equals("is_comment_of") || edge.getLabel().equals("is_reply_of") || edge.getLabel().equals("has_written")) {
					node = edge.getEndingNotes().get(0);
				}
			}
		}
		
		return node.getId();
	}
	
	/**
	 * Calculate related user of a given post.
	 * 
	 * @param nodeId Id of post.
	 * @param nodes All nodes from graph.
	 * 
	 * @return Id of user.
	 */
	public String getRelatedUser(String nodeId, Map<String, GraphNode> nodes) {
		GraphNode node = nodes.get(nodeId);
		
		if (node.getLabel().equals("user")) {
			return node.getId();
		} else if (node.getIncomingEdges().isEmpty()) {
			return null;
		} 
		
		while (!node.getLabel().equals("user")) {
			for (GraphEdge edge : node.getIncomingEdges().keySet()) {
				if (edge.getLabel().equals("has_written")) {
					node = edge.getStartingNodes().get(0);
				}
			}
		}
		
		return node.getId();
	}
	
}
