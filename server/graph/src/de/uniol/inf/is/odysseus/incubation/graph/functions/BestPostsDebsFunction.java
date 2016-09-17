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

public class BestPostsDebsFunction extends AbstractFunction<Tuple<IMetaAttribute>> {
	private static final long serialVersionUID = -8341087533326456443L;
	
	private Map<String, PointInTime> postTs = new HashMap<String, PointInTime>();
	private Map<String, PointInTime> postCommentTs = new HashMap<String, PointInTime>();
	private Map<String, Long> postScores = new HashMap<String, Long>();
	private Map<String, List<String>> postComments = new HashMap<String, List<String>>();
	private Map<String, String> postUser = new HashMap<String, String>();
	
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFGraphDatatype.GRAPH}
	};
	
	public BestPostsDebsFunction() {
		super("bestPostsDebs", 1, BestPostsDebsFunction.ACC_TYPES, SDFDatatype.TUPLE);
	}

	@Override
	public Tuple<IMetaAttribute> getValue() {
		Graph graph = getInputValue(0);
		IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(graph.getName()).cloneDataStructure();
		
		for (GraphNode node: structure.getGraphNodes().values()) {
			if (node.getLabel().equals("post")) {
				// Erstellungszeitpunkt setzen falls noch nicht vorhanden.
				if (!postTs.containsKey(node.getId())) {
					postTs.put(node.getId(), new PointInTime(Long.valueOf(graph.getName().split("_")[1])));
				}
				
				postScores.put(node.getId(), (Long) node.getProps().get("total_score"));
			} else if (node.getLabel().equals("comment")) {
				String postId = this.getRelatedPost(node.getId(), structure.getGraphNodes());
				if (postId != null) {
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
			} else if (node.getLabel().equals("user")) {
				String postId = this.getRelatedPost(node.getId(), structure.getGraphNodes());
				if (postId != null) {
					postUser.put(postId, node.getId());
				}
			}
		}
		
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(3, false);
		
		// Bestimmung der drei besten Beiträge.
		for (int i=0; i<3; i++) {
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
				postScores.remove(bestPost);
			}
		}
		
		GraphDataStructureProvider.getInstance().setGraphVersionRead(graph.getName(), "bestPostsDebs");
		
		return tuple;
	}
	
	// Ermitteln des zugehörigen Beitrags.
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
	
}
