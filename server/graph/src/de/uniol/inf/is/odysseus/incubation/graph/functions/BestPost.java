package de.uniol.inf.is.odysseus.incubation.graph.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphEdge;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphNode;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;

public class BestPost<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -4960965026820211241L;
	
	Map<Pair<String, String>, Integer> posts = new HashMap<Pair<String, String>, Integer>();
	@SuppressWarnings("unchecked")
	Tuple<IMetaAttribute>[] result = new Tuple[0];
	
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
		for (Map.Entry<String, GraphNode> entry : graphNodes.entrySet()) {
			if (entry.getKey().contains("post")) {
				int comments = 0;
				Map<GraphEdge, GraphNode> inEdges = entry.getValue().getIncomingEdges();
				for (Map.Entry<GraphEdge, GraphNode> edge : inEdges.entrySet()) {
					if (edge.getKey().getLabel().equals("is_comment_of") && edge.getValue().getId().contains("comment")) {
						comments ++;
					}
				}
				posts.put(new Pair<String, String>(entry.getKey(), graph.getName()), comments);
			}
		}
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		List<String> outdatedStructures = new ArrayList<String>();
		
		for (T element : outdatedElements) {
			Graph graph = element.getAttribute(0);
			outdatedStructures.add(graph.getName());
		}
		
		// Konnte key nicht direkt entfernen, deswegen erst in Liste zwischengespeichert.
		List<Pair<String, String>> postsToRemove = new ArrayList<Pair<String, String>>();
		for(Map.Entry<Pair<String, String>, Integer> post : posts.entrySet()) {
			if (outdatedStructures.contains(post.getKey().getE2())) {
				postsToRemove.add(post.getKey());
			}
		}
		
		for (Pair<String, String> toRemove : postsToRemove) {
			posts.remove(toRemove);
		}
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		Tuple<IMetaAttribute>[] result = getMaxValuesOfMap(posts, 3);
		
		return result;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new BestPost<M, T>(this);
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return true;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {		
		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters, attributeResolver);
		final String[] outputNames = AggregationFunctionParseOptionsHelper.getOutputAttributeNames(parameters, attributeResolver);
		
		if (attributes == null) {
			return new BestPost<M, T>(attributeResolver.getSchema().get(0).size(), outputNames);
		}
		return new BestPost<M, T>(attributes, outputNames);
	}
	
	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> res = new ArrayList<>(3);
		res.add(new SDFAttribute(null, "post1", SDFDatatype.TUPLE, null, null, null));
		res.add(new SDFAttribute(null, "post2", SDFDatatype.TUPLE, null, null, null));
		res.add(new SDFAttribute(null, "post3", SDFDatatype.TUPLE, null, null, null));
		
		return res;
	}
	
	
	@SuppressWarnings("unchecked")
	public Tuple<IMetaAttribute>[] getMaxValuesOfMap(Map<Pair<String, String>, Integer> map, int numberOfValues) {
		Map<Pair<String, String>, Integer> mapCopy = map;
		List<String> usedPosts = new ArrayList<String>();

		Tuple<IMetaAttribute>[] bestPosts;
		
		if (map.size() < numberOfValues) {
			bestPosts = new Tuple[map.size()];
		} else {
			bestPosts = new Tuple[numberOfValues];
		}
		
		int index = 0;
		
		//mapCopy dient dazu, die Elemente zu entfernen, sonst bekommt man eine Fehlermeldung
		while(index < numberOfValues && !mapCopy.isEmpty()) {
			int maxValueInMap = Collections.max(mapCopy.values());
			
			for (Map.Entry<Pair<String, String>, Integer> entry : map.entrySet()) {
				if (entry.getValue() == maxValueInMap && !usedPosts.contains(entry.getKey().getE1())) {
					Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(new Object[]{entry.getKey().getE1(), entry.getValue()}, false);
					bestPosts[index] = tuple;
					usedPosts.add(entry.getKey().getE1());
					mapCopy.remove(new Pair<String, String>(entry.getKey().getE1(), entry.getKey().getE2()));
					break;
				}
			}
			
			index ++;
		}
		
		return bestPosts;
	}

}
