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

public class BestPostsDebs<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = 6587136994118302253L;
	
	private Map<String, PointInTime> tsPost = new HashMap<String, PointInTime>();
	private Map<String, PointInTime> tsComment = new HashMap<String, PointInTime>();
	private Map<String, String> postStructures = new HashMap<String, String>();
	private String structureName;
	
	public BestPostsDebs() {
		super();
	}
	
	public BestPostsDebs(final BestPostsDebs<M,T> other) {
		super(other);
	}
	
	public BestPostsDebs(final int[] attributes, final String[] outputNames) {
		super(attributes, outputNames);
	}
	
	public BestPostsDebs(final int inputAttributesLength, final String[] outputNames) {
		super(null, outputNames);
	}

	@Override
	public void addNew(T newElement) {
		Graph graph = newElement.getAttribute(0);
		Graph postGraph = newElement.getAttribute(1);

		String node1 = graph.getNode1();
		String node2 = graph.getNode2();
		
		if ((node1 != null && node1.contains("post")) || (node2 != null && node2.contains("post")) && !tsPost.containsKey(postGraph.getNode1())) {
			tsPost.put(postGraph.getNode1(), newElement.getMetadata().getStart());
		}
		
		if ((node1 != null && node1.contains("comment")) || (node2 != null && node2.contains("comment")) && !tsComment.containsKey(postGraph.getNode1())) {
			tsComment.put(postGraph.getNode1(), newElement.getMetadata().getStart());
		}
		
		this.structureName = graph.getName();
		this.postStructures.put(postGraph.getNode1(), postGraph.getName());
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
	}

	
	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {				
		IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(structureName);
		String[] bestPosts = new String[3];
		Long[] bestScores = new Long[]{0l, 0l, 0l};
		
		for (GraphNode node : structure.getGraphNodes().values()) {
			if (node.getLabel().equals("post")) {
				Long totalScore = (Long) node.getProps().get("total_score");				
				if (
					bestPosts[0] == null ||
					totalScore > bestScores[0] ||
					(totalScore == bestScores[0] && !tsPost.containsKey(bestPosts[0])) ||
					(totalScore == bestScores[0] && tsPost.containsKey(node.getId()) && tsPost.get(node.getId()).after(tsPost.get(bestPosts[0]))) ||
					(totalScore == bestScores[0] && tsPost.containsKey(node.getId()) && tsPost.get(node.getId()).equals(tsPost.get(bestPosts[0])) && !tsComment.containsKey(bestPosts[0])) ||
					(totalScore == bestScores[0] && tsPost.containsKey(node.getId()) && tsPost.get(node.getId()).equals(tsPost.get(bestPosts[0])) && tsComment.containsKey(node.getId()) && tsComment.get(node.getId()).after(tsComment.get(bestPosts[0])))
				) {
					bestPosts[2] = bestPosts[1];
					bestScores[2] = bestScores[1];
					bestPosts[1] = bestPosts[0];
					bestScores[1] = bestScores[0];
					bestPosts[0] = node.getId();
					bestScores[0] = totalScore;
				} else if (
						bestPosts[1] == null ||
						totalScore > bestScores[1] ||
						(totalScore == bestScores[1] && !tsPost.containsKey(bestPosts[1])) ||
						(totalScore == bestScores[1] && tsPost.containsKey(node.getId()) && tsPost.get(node.getId()).after(tsPost.get(bestPosts[1]))) ||
						(totalScore == bestScores[1] && tsPost.containsKey(node.getId()) && tsPost.get(node.getId()).equals(tsPost.get(bestPosts[1])) && !tsComment.containsKey(bestPosts[1])) ||
						(totalScore == bestScores[1] && tsPost.containsKey(node.getId()) && tsPost.get(node.getId()).equals(tsPost.get(bestPosts[1])) && tsComment.containsKey(node.getId()) && tsComment.get(node.getId()).after(tsComment.get(bestPosts[1])))
				) {
					bestPosts[2] = bestPosts[1];
					bestScores[2] = bestScores[1];
					bestPosts[1] = node.getId();
					bestScores[1] = totalScore;
				} else if (
						bestPosts[2] == null ||
						totalScore > bestScores[2] ||
						(totalScore == bestScores[2] && !tsPost.containsKey(bestPosts[2])) ||
						(totalScore == bestScores[2] && tsPost.containsKey(node.getId()) && tsPost.get(node.getId()).after(tsPost.get(bestPosts[2]))) ||
						(totalScore == bestScores[2] && tsPost.containsKey(node.getId()) && tsPost.get(node.getId()).equals(tsPost.get(bestPosts[2])) && !tsComment.containsKey(bestPosts[2])) ||
						(totalScore == bestScores[2] && tsPost.containsKey(node.getId()) && tsPost.get(node.getId()).equals(tsPost.get(bestPosts[2])) && tsComment.containsKey(node.getId()) && tsComment.get(node.getId()).after(tsComment.get(bestPosts[2])))
				) {
					bestPosts[2] = node.getId();
					bestScores[2] = totalScore;
				}
			}
		}
		
		Object[] res = new Object[3];
		for (int i=0; i<bestPosts.length; i++) {
			IGraphDataStructure<IMetaAttribute> postStructure = GraphDataStructureProvider.getInstance().getGraphDataStructure(postStructures.get(bestPosts[i]));
			
			String author = "";
			Long numComments = 0l;
			if (postStructure != null) {
				for (GraphNode node : postStructure.getGraphNodes().values()) {
					if (node.getLabel().equals("user")) {
						author = node.getId();
						break;
					} else if (node.getLabel().equals("comment")) {
						numComments ++;
					}
				}
			}
			
			Tuple<IMetaAttribute> postTuple = new Tuple<IMetaAttribute>(4, false);
			postTuple.setAttribute(0, bestPosts[i]);
			postTuple.setAttribute(1, author);
			postTuple.setAttribute(2, bestScores[i]);
			postTuple.setAttribute(3, numComments);
			
			res[i] = postTuple;
		}
		
		return res;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> res = new ArrayList<>(3);
		res.add(new SDFAttribute(null, "post1", SDFDatatype.TUPLE, null, null, null));
		res.add(new SDFAttribute(null, "post2", SDFDatatype.TUPLE, null, null, null));
		res.add(new SDFAttribute(null, "post3", SDFDatatype.TUPLE, null, null, null));
		
		return res;
	}
	
	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new BestPostsDebs<M, T>(this);
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return true;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return new BestPostsDebs<M, T>();
	}

}
