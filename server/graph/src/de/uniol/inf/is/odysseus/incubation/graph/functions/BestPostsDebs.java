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
	
	Map<String, Tuple<IMetaAttribute>> posts = new HashMap<String, Tuple<IMetaAttribute>>();
	Map<String, PointInTime> timestampPost = new HashMap<String, PointInTime>();
	Map<String, PointInTime> timestampComment = new HashMap<String, PointInTime>();
	Map<String, Long> postScores = new HashMap<String, Long>();
	
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
		
		if ((node1 != null && node1.contains("post")) || (node2 != null && node2.contains("post"))) {
			timestampPost.put(postGraph.getNode1(), newElement.getMetadata().getStart());
		}
		
		if ((node1 != null && node1.contains("comment")) || (node2 != null && node2.contains("comment"))) {
			timestampComment.put(postGraph.getNode1(), newElement.getMetadata().getStart());
		}
		
		IGraphDataStructure<IMetaAttribute> structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(postGraph.getName());
		GraphNode node = structure.getGraphNode(postGraph.getNode1());
		
		Long totalScore = (Long) node.getProps().get("total_score");
		String author = "";
		for (GraphNode edgeNode : node.getIncomingEdges().values()) {
			if (edgeNode.getLabel().equals("user")) {
				author  = edgeNode.getId();
			}
		}
			
		Long numComments = 0l;
		for (GraphNode graphNode : structure.getGraphNodes().values()) {
			if (graphNode.getLabel().equals("comment")) {
				numComments ++;
			}
		}
		
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(4, false);
		tuple.setAttribute(0, postGraph.getNode1());
		tuple.setAttribute(1, author);
		tuple.setAttribute(2, totalScore);
		tuple.setAttribute(3, numComments);
		
		posts.put(postGraph.getNode1(), tuple);
		postScores.put(postGraph.getNode1(), totalScore);
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		@SuppressWarnings("unchecked")
		Tuple<IMetaAttribute>[] result 
			= new Tuple[]{new Tuple<IMetaAttribute>(4, false), new Tuple<IMetaAttribute>(4, false), new Tuple<IMetaAttribute>(4, false)};
		
		String[] postNames = new String[3];
		Long[] maxValues = new Long[]{0l, 0l, 0l};
		for (Map.Entry<String, Long> entry : postScores.entrySet()) {
			if (
				entry.getValue() > maxValues[0].longValue() ||
				(entry.getValue() == maxValues[0].longValue() && timestampPost.get(entry.getKey()).after(timestampPost.get(postNames[0]))) ||
				(entry.getValue() == maxValues[0].longValue() && timestampPost.get(entry.getKey()).equals(timestampPost.get(postNames[0])) && (timestampComment.get(postNames[0]) == null && timestampComment.get(entry.getKey()).after(timestampComment.get(postNames[0]))))
			) {
				maxValues[2] = maxValues[1];
				postNames[2] = postNames[1];
				maxValues[1] = maxValues[0];
				postNames[1] = postNames[0];
				maxValues[0] = entry.getValue();
				postNames[0] = entry.getKey();
			} else if (
				entry.getValue() > maxValues[1].longValue() ||
				(entry.getValue() == maxValues[1].longValue() && timestampPost.get(entry.getKey()).after(timestampPost.get(postNames[1]))) ||
				(entry.getValue() == maxValues[1].longValue() && timestampPost.get(entry.getKey()).equals(timestampPost.get(postNames[1])) && (timestampComment.get(postNames[1]) == null && timestampComment.get(entry.getKey()).after(timestampComment.get(postNames[1]))))
			) {
				maxValues[2] = maxValues[1];
				postNames[2] = postNames[1];
				maxValues[1] = entry.getValue();
				postNames[1] = entry.getKey();
			} else if (
				entry.getValue() > maxValues[2].longValue() ||
				(entry.getValue() == maxValues[2].longValue() && timestampPost.get(entry.getKey()).after(timestampPost.get(postNames[2]))) ||
				(entry.getValue() == maxValues[2].longValue() && timestampPost.get(entry.getKey()).equals(timestampPost.get(postNames[2])) && (timestampComment.get(postNames[2]) == null && timestampComment.get(entry.getKey()).after(timestampComment.get(postNames[2]))))
			) {
				maxValues[2] = entry.getValue();
				postNames[2] = entry.getKey();
			}
		}
		
		result[0] = posts.get(postNames[0]);
		result[1] = posts.get(postNames[1]);
		result[2] = posts.get(postNames[2]);
		
		return result;
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
