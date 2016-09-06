package de.uniol.inf.is.odysseus.processmining.dataanalyser.strategies;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.processmining.common.AbstractLCTuple;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.IInvariantResult;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.InvariantResultTypOne;

public class InvariantStrategyTypOne<T extends IMetaAttribute> extends InvariantStategyBase<T> {

	
	public InvariantStrategyTypOne() {
		super();
	}
	
	@Override
	public IInvariantResult calculateStrategy(Tuple<T> mostRecent, Tuple<T> current) {
		updateData(mostRecent, current);
		if (this.mostRecentModel != null && this.currentModel != null) {
			InvariantResultTypOne result = new InvariantResultTypOne();
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> mostRecentDFG = getGraph(transferHelper
					.getDirectlyFollowRelations(mostRecentModel));
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentDFG = getGraph(transferHelper
					.getDirectlyFollowRelations(currentModel));
			
			result.setAlmostEqualStartNodes(areNodesAlmostEqual(
					transferHelper.getActivities(mostRecentModel),
					transferHelper.getActivities(currentModel)));
			result.setAlmostEqualEndNodes(areNodesAlmostEqual(
					transferHelper.getEndActivities(mostRecentModel),
					transferHelper.getEndActivities(currentModel)));
			result.setNodeCountDistinction(calculateNodeCountDistinction(
					mostRecentDFG, currentDFG));
			result.setEdgeCountDistinction(calculateEdgeCountDistinction(
					mostRecentDFG, currentDFG));
			result.setDegreeDistinction(calculateDegreeDistinct(mostRecentDFG,
					currentDFG));
			result.setCurrentNodesCount(currentDFG.vertexSet().size());
			result.setCurrentEdgeCount(currentDFG.edgeSet().size());
			result.setMostRecentNodesCount(mostRecentDFG.vertexSet().size());
			result.setMostRecentEdgeCount(mostRecentDFG.edgeSet().size());
			return result;
		}
		return null;
	}



	private double calculateDegreeDistinct(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> mostRecentDFG,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentDFG) {

		Map<String, Integer> mostRecentDegreeMap = Maps.newHashMap();
		Map<String, Integer> currentDegreeMap = Maps.newHashMap();
		
		for(String key : mostRecentDFG.vertexSet()){
			mostRecentDegreeMap.put(key, 
					mostRecentDFG.incomingEdgesOf(key).size()+mostRecentDFG.outgoingEdgesOf(key).size());
		}
		
		for(String key : currentDFG.vertexSet()){
			currentDegreeMap.put(key, 
					currentDFG.incomingEdgesOf(key).size()+currentDFG.outgoingEdgesOf(key).size());
		}
		Set<String> commonNodes = Sets.newHashSet(mostRecentDFG.vertexSet());
		commonNodes.retainAll(currentDFG.vertexSet());
		double differentDegrees = 0;
		double edgeCounter = 0;
		for (String node : commonNodes) {
			if (currentDegreeMap.get(node) != currentDegreeMap.get(node)) {
				differentDegrees += 1;
			}
			edgeCounter +=currentDegreeMap.get(node);
		}
		
		return differentDegrees == 0 ? 1.0 : differentDegrees / edgeCounter ;
	}

	/**
	 * Calculates the percental distinct between the edges of the given graphs
	 * 
	 * @param mostRecentDFG
	 * @param currentDFG
	 * @return
	 */
	private double calculateEdgeCountDistinction(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> mostRecentDFG,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentDFG) {
		return ((double)currentDFG.edgeSet().size()) / ((double)mostRecentDFG.edgeSet().size());
	}

	/**
	 * Calculates the percental distinct between the node of the given graphs
	 * 
	 * @param mostRecentDFG
	 * @param currentDFG
	 * @return
	 */
	private double calculateNodeCountDistinction(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> mostRecentDFG,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentDFG) {
		return ((double)currentDFG.vertexSet().size()) / ((double)mostRecentDFG.vertexSet().size());
	}

	/**
	 * Checks if the nodes of the two parameters are almost equal
	 * 
	 * @param mostRecentNodes
	 * @param currentNodes
	 * @return
	 */
	private boolean areNodesAlmostEqual(
			HashMap<Object, AbstractLCTuple<T>> mostRecentNodes,
			HashMap<Object, AbstractLCTuple<T>> currentNodes) {
		if (!transferHelper.getEndActivities(mostRecentModel).equals(
				transferHelper.getEndActivities(currentModel))) {
			mostRecentNodes = transferHelper.getEndActivities(mostRecentModel);
			currentNodes = transferHelper.getEndActivities(currentModel);
			if (getMostFrequentItemOf(mostRecentNodes).equals(
					getMostFrequentItemOf(currentNodes))) {
				return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets returns the string of the key with the highest frequency
	 * 
	 * @param mostRecentNodes
	 * @return
	 */
	private String getMostFrequentItemOf(HashMap<Object, AbstractLCTuple<T>> mostRecentNodes) {
		String mostFreqItem = null;
		int freq = 0;
		for (Object key : mostRecentNodes.keySet()) {
			if (freq < mostRecentNodes.get(key).getFrequency()) {
				mostFreqItem = (String) key;
				freq = mostRecentNodes.get(key).getFrequency();
			}
		}
		return mostFreqItem;
	}
}
