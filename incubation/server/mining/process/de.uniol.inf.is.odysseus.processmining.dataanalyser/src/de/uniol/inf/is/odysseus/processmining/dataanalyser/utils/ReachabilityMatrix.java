package de.uniol.inf.is.odysseus.processmining.dataanalyser.utils;

import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ReachabilityMatrix {
	
	Map <Integer,String> numberToNodeMapping;
	Map <String,Integer> NodeToNumerMapping;
	boolean[][] reachabilityMatrix;
	Map<String,Set<String>> reachabilityMap;
	Map<String,Set<String>> directlyFollow;
	
	int elementcounter;
	public ReachabilityMatrix(DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph){
		elementcounter = 0;
		createMapping(graph.vertexSet());
		reachabilityMatrix = new boolean[elementcounter][elementcounter];
		initializeMatrix(graph);
		directlyFollow = getHashMapDFR();
		calculateReachabilityMatrix();
		reachabilityMap = getHashMap();
		
	}

	private void initializeMatrix(DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph) {
		for(int i = 0; i<elementcounter;i++){
			for(int j =0;j<elementcounter;j++){
				reachabilityMatrix[i][j] = false;				
			}
		}
		for(DefaultWeightedEdge edge : graph.edgeSet()){
			String src = graph.getEdgeSource(edge);
			String trgt = graph.getEdgeTarget(edge);
			// From source To target
			reachabilityMatrix[NodeToNumerMapping.get(src)][NodeToNumerMapping.get(trgt)] = true;
		}
		
		
	}

	private void createMapping(Set<String> nodes) {
		numberToNodeMapping = Maps.newHashMap();
		NodeToNumerMapping = Maps.newHashMap();
		
		for(String node : nodes){
			numberToNodeMapping.put(elementcounter, node);
			NodeToNumerMapping.put(node,elementcounter);
			elementcounter++;
		}
		
	}

	private void calculateReachabilityMatrix(){
		for(int y = 0; y< elementcounter; y++){
			for(int x= 0;x<elementcounter;x++){
				if(reachabilityMatrix[x][y]){
				for(int j = 0;j< elementcounter;j++){
					if(reachabilityMatrix[y][j]){
						reachabilityMatrix[x][j] = true;
					}
				}}
			}
		}
	}
	
	private Map<String,Set<String>> getHashMap(){
		Map<String,Set<String>> reachabilityMap = Maps.newHashMap();
		
		for(int i = 0; i<elementcounter;i++){
			Set<String> reachableNodes = Sets.newHashSet();
			for(int j =0;j<elementcounter;j++){
				if(reachabilityMatrix[i][j] == true){
					reachableNodes.add(numberToNodeMapping.get(j));
				}
				reachabilityMap.put(numberToNodeMapping.get(i),reachableNodes);
			}
		}
		return reachabilityMap;
	}
	
	private Map<String,Set<String>> getHashMapDFR(){
		Map<String,Set<String>> reachabilityMap = Maps.newHashMap();
		
		for(int i = 0; i<elementcounter;i++){
			Set<String> reachableNodes = Sets.newHashSet();
			for(int j =0;j<elementcounter;j++){
				if(reachabilityMatrix[i][j] == true){
					reachableNodes.add(numberToNodeMapping.get(j));
				}
				reachabilityMap.put(numberToNodeMapping.get(i),reachableNodes);
			}
		}
		return reachabilityMap;
	}
	public Map<String,Set<String>> getMap(){
		return this.reachabilityMap;
	}
	public Map<String,Set<String>> getMapDFR(){
		return this.directlyFollow;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((NodeToNumerMapping == null) ? 0 : NodeToNumerMapping
						.hashCode());
		result = prime * result
				+ ((directlyFollow == null) ? 0 : directlyFollow.hashCode());
		result = prime * result + elementcounter;
		result = prime
				* result
				+ ((numberToNodeMapping == null) ? 0 : numberToNodeMapping
						.hashCode());
		result = prime * result
				+ ((reachabilityMap == null) ? 0 : reachabilityMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReachabilityMatrix other = (ReachabilityMatrix) obj;
		if (NodeToNumerMapping == null) {
			if (other.NodeToNumerMapping != null)
				return false;
		} else if (!NodeToNumerMapping.equals(other.NodeToNumerMapping))
			return false;
		if (directlyFollow == null) {
			if (other.directlyFollow != null)
				return false;
		} else if (!directlyFollow.equals(other.directlyFollow))
			return false;
		if (elementcounter != other.elementcounter)
			return false;
		if (numberToNodeMapping == null) {
			if (other.numberToNodeMapping != null)
				return false;
		} else if (!numberToNodeMapping.equals(other.numberToNodeMapping))
			return false;
		if (reachabilityMap == null) {
			if (other.reachabilityMap != null)
				return false;
		} else if (!reachabilityMap.equals(other.reachabilityMap))
			return false;
		return true;
	}
	
}
