package de.uniol.inf.is.odysseus.incubation.graph.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
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

public class BestPost<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -4960965026820211241L;
	
	private Map<String, List<String>> structurePosts = new HashMap<String, List<String>>();
	private Map<String, Long> postComments = new HashMap<String, Long>();
	private List<String> posts = new ArrayList<String>();
	private List<String> comments = new ArrayList<String>();
	
	@SuppressWarnings("unchecked")
	private Tuple<IMetaAttribute>[] result = new Tuple[0];
	
	private int numPosts = 0;
	
	@SuppressWarnings("unchecked")
	public BestPost() {
		super();
		result = new Tuple[0];
	}
	
	public BestPost(final BestPost<M, T> other) {
		super(other);
		
		result = other.result;
		Arrays.fill(result, new Tuple<IMetaAttribute>(1, false));
	}
	
	@SuppressWarnings("unchecked")
	public BestPost(final int[] attributes, final String[] outputNames) {
		super(attributes, outputNames);
		this.result = new Tuple[attributes.length];
		Arrays.fill(result, new Tuple<IMetaAttribute>(1, false));
		if (outputNames.length != attributes.length) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public BestPost(final int inputAttributesLength, final String[] outputNames) {
		super(null, outputNames);
		this.result = new Tuple[inputAttributesLength];
		Arrays.fill(result, new Tuple<IMetaAttribute>(1, false));
		if (outputNames.length != inputAttributesLength) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}
	
	@Override
	public void addNew(T newElement) {
		Graph graph = newElement.getAttribute(0);
		IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(graph.getName());
		
		Map<String, GraphNode> graphNodes = structure.getGraphNodes();
		for (GraphNode node : graphNodes.values()) {
			if (node.getLabel().equals("post") && !posts.contains(node.getId())) {
				if (structurePosts.containsKey(graph.getName())) {
					List<String> posts = structurePosts.get(graph.getName());
					posts.add(node.getId());
					structurePosts.put(graph.getName(), posts);
				} else {
					structurePosts.put(graph.getName(), Arrays.asList(node.getId()));
				}
				postComments.put(node.getId(), 0l);
				this.posts.add(node.getId());
			} else if (node.getLabel().equals("comment")) {
				String postId = this.getRelatedPost(node.getId(), structure.getGraphNodes());
				if (postId != null && !this.comments.contains(node.getId())) {
					if (postComments.containsKey(postId)) {
						postComments.put(postId, postComments.get(postId) + 1);
					} else {
						postComments.put(postId, 1l);
					}
					this.comments.add(node.getId());
				}
			}
		}
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		for (T element : outdatedElements) {
			Graph graph = element.getAttribute(0);
			
			if (structurePosts.containsKey(graph.getName())) {
				for (String postId : structurePosts.get(graph.getName())) {
					postComments.remove(postId);
				}
				
				structurePosts.remove(graph.getName());
			}
		}
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		Tuple<IMetaAttribute>[] result = getMaxValuesOfMap();
		return result;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		BestPost<M, T> newFunction = new BestPost<M, T>(this);
		newFunction.numPosts = this.numPosts;
		newFunction.structurePosts = this.structurePosts;
		newFunction.postComments = this.postComments;
		newFunction.result = this.result.clone();
		return newFunction;
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return true;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {		
		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters, attributeResolver);
		final String[] outputNames = AggregationFunctionParseOptionsHelper.getOutputAttributeNames(parameters, attributeResolver);
		
		this.numPosts = ((Long) parameters.get("numPosts")).intValue();
		
		if (attributes == null) {
			return new BestPost<M, T>(attributeResolver.getSchema().get(0).size(), outputNames);
		}
		return new BestPost<M, T>(attributes, outputNames);
	}
	
	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> res = new ArrayList<>(this.numPosts);
		
		for (int i = 1; i <= this.numPosts; i++) {
			res.add(new SDFAttribute(null, "post" + i, SDFDatatype.TUPLE, null, null, null));
		}
		
		return res;
	}
	
	
	@SuppressWarnings("unchecked")
	public Tuple<IMetaAttribute>[] getMaxValuesOfMap() {
		Tuple<IMetaAttribute>[] bestPosts;
	
		Map<String, Long> postCommentsCopy = new HashMap<String, Long>(this.postComments);
		
		if (postCommentsCopy.size() < this.numPosts) {
			bestPosts = new Tuple[postCommentsCopy.size()];
		} else {
			bestPosts = new Tuple[this.numPosts];
		}
		
		for (int i=0; i<bestPosts.length; i++) {
			if (!postCommentsCopy.isEmpty()) {
				String bestPost = Collections.max(
						postCommentsCopy.entrySet(),
						new Comparator<Map.Entry<String, Long>>() {
							@Override
							public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
								return o1.getValue().compareTo(o2.getValue());
							}
							
						}
				).getKey();
				
				Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
				tuple.setAttribute(0, bestPost);
				tuple.setAttribute(1, postCommentsCopy.get(bestPost));
				
				bestPosts[i] = tuple;
				
				postCommentsCopy.remove(bestPost);
			}
		}

		return bestPosts;
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
