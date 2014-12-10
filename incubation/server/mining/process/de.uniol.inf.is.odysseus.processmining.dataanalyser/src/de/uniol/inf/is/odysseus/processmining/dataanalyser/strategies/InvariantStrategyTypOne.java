package de.uniol.inf.is.odysseus.processmining.dataanalyser.strategies;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.processmining.common.AbstractLCTuple;
import de.uniol.inf.is.odysseus.processmining.common.DFRTuple;
import de.uniol.inf.is.odysseus.processmining.common.InductiveMinerTransferTuple;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.InvariantAnalysisResult;

public class InvariantStrategyTypOne implements IInvariantStrategy {
	InductiveMinerTransferTuple mostRecentModel = null;
	InductiveMinerTransferTuple currentModel = null;

	public InvariantStrategyTypOne(){
		
	}
	public InvariantStrategyTypOne(InductiveMinerTransferTuple mostRecentModel,
			InductiveMinerTransferTuple currentModel) {
		this.mostRecentModel = mostRecentModel;
		this.currentModel = currentModel;
	}

	public InvariantAnalysisResult calculateStrategy() {
		if(mostRecentModel != null && currentModel != null){
		InvariantAnalysisResult result = new InvariantAnalysisResult();
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> mostRecentDFG = getGraph(mostRecentModel
				.getDirectlyFollowRelations());
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentDFG = getGraph(currentModel
				.getDirectlyFollowRelations());
		
		result.setAlmostEqualStartNodes(areNodesAlmostEqual(
				mostRecentModel.getStartActivites(),
				currentModel.getStartActivites()));
		result.setAlmostEqualEndNodes(areNodesAlmostEqual(
				mostRecentModel.getEndActivities(),
				currentModel.getEndActivities()));
		result.setNodeCountDistinction(calculateNodeCountDistinction(mostRecentDFG, currentDFG));
		result.setEdgeCountDistinction(calculateEdgeCountDistinction(mostRecentDFG, currentDFG));
		result.setDegreeDistinction(calculateDegreeDistinct(mostRecentDFG, currentDFG));

		return result;
		}
		return null;
	}

	
	/**
	 * Creates a directly follow graph base on the given directly follow relations
	 * @param dfrel
	 * @return
	 */
	private DirectedWeightedPseudograph<String, DefaultWeightedEdge> getGraph(
			HashMap<Object, AbstractLCTuple> dfrel) {

		DirectedWeightedPseudograph<String, DefaultWeightedEdge> dfg = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		for (Map.Entry entry : dfrel.entrySet()) {
			DFRTuple t = (DFRTuple) entry.getValue();
			String startNode = t.getActivity();
			String endNode = t.getFollowActivity();
			// Graph
			dfg.addVertex(startNode);
			dfg.addVertex(endNode);
			DefaultWeightedEdge e = dfg.addEdge(t.getActivity(),
					t.getFollowActivity());
			dfg.setEdgeWeight(e, t.getFrequency());
		}
		return dfg;
	}

	
	private double calculateDegreeDistinct(DirectedWeightedPseudograph<String, DefaultWeightedEdge> mostRecentDFG,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentDFG){
		
		Set<String> mostRecentNodes = Sets.newHashSet(mostRecentDFG.vertexSet());
		Set<String> currentNodes = Sets.newHashSet(currentDFG.vertexSet());
		
		mostRecentNodes.retainAll(currentNodes);
		int differentDegrees = 0;
		int edgeCounter =0;
		for(String node : currentNodes){
			if(mostRecentDFG.degreeOf(node) != currentDFG.degreeOf(node)){
				differentDegrees += 1;
			}
			edgeCounter += currentDFG.degreeOf(node);
		}
		
		return differentDegrees / edgeCounter ;
	}
	
	/**
	 * Calculates the percentaged distinct between the edges of the given graphs
	 * @param mostRecentDFG
	 * @param currentDFG
	 * @return
	 */
	private double calculateEdgeCountDistinction(DirectedWeightedPseudograph<String, DefaultWeightedEdge> mostRecentDFG,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentDFG){
		return currentDFG.edgeSet().size() / mostRecentDFG.edgeSet().size();
	}
	
	/**
	 * Calculates the percentaged distinct between the node of the given graphs
	 * @param mostRecentDFG
	 * @param currentDFG
	 * @return
	 */
	private double calculateNodeCountDistinction(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> mostRecentDFG,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentDFG) {
		return currentDFG.vertexSet().size() / mostRecentDFG.vertexSet().size();
	}

	/**
	 * Checks if the nodes of the two parameters are almost equal
	 * 
	 * @param mostRecentNodes
	 * @param currentNodes
	 * @return
	 */
	private boolean areNodesAlmostEqual(
			HashMap<Object, AbstractLCTuple> mostRecentNodes,
			HashMap<Object, AbstractLCTuple> currentNodes) {
		if (!mostRecentModel.getEndActivities().equals(
				currentModel.getEndActivities())) {
			mostRecentNodes = mostRecentModel.getEndActivities();
			currentNodes = currentModel.getEndActivities();
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
	 * @param map
	 * @return
	 */
	private String getMostFrequentItemOf(HashMap<Object, AbstractLCTuple> map) {
		String mostFreqItem = null;
		int freq = 0;
		for (Object key : map.keySet()) {
			if (freq < map.get(key).getFrequency()) {
				mostFreqItem = (String) key;
				freq = map.get(key).getFrequency();
			}
		}
		return mostFreqItem;
	}
	public InductiveMinerTransferTuple getMostRecentModel() {
		return mostRecentModel;
	}
	public void setMostRecentModel(InductiveMinerTransferTuple mostRecentModel) {
		this.mostRecentModel = mostRecentModel;
	}
	public InductiveMinerTransferTuple getCurrentModel() {
		return currentModel;
	}
	public void setCurrentModel(InductiveMinerTransferTuple currentModel) {
		this.currentModel = currentModel;
	}

}
