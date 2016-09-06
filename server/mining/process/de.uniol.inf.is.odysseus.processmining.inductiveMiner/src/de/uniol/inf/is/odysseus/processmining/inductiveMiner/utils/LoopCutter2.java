package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.OperatorType;

public class LoopCutter2 extends AbstractCutter {

	@Override
	public Cut getCut(Partition partition) {
		// Graph References
		LinkedList<Partition> partitions = Lists.newLinkedList();

		DirectedWeightedPseudograph<String, DefaultWeightedEdge> dwGraph = partition
				.getGraph();

		Set<String> startNodeSet = Sets.newHashSet(partition.getStartNodes()
				.keySet());
		Set<String> endNodeSet = Sets.newHashSet(partition.getEndNodes()
				.keySet());
		Multimap<PathType, LoopPath> loopPathsMap = createLoopPaths(dwGraph, startNodeSet, endNodeSet);
		Set<String> bodyPartPaths = Sets.newHashSet();
		List<Set<String>> loopPartPaths = Lists.newArrayList();
		
		
		for(LoopPath part : loopPathsMap.get(PathType.StartToEnd)){
			bodyPartPaths.addAll(part.getPath());
		}
		
		for(LoopPath part : loopPathsMap.get(PathType.EndToStart)){
			loopPartPaths.add(part.getPath());
		}
		
		for(LoopPath part : loopPathsMap.get(PathType.StartEqualEnd)){
			loopPartPaths.add(part.getPath());
		}
		bodyPartPaths.addAll(startNodeSet);
		bodyPartPaths.addAll(endNodeSet);
		
		
		if(loopPathsMap.get(PathType.NotValid).size() == 0){
			// Cut is Valid
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> loopbodyGraph =PartitionUtils.createPartitionedGraph(bodyPartPaths, dwGraph);
		Set<String> bodyShortLoops = Sets.newHashSet();
		for (DefaultWeightedEdge edge : loopbodyGraph.edgeSet()) {
			String src = loopbodyGraph.getEdgeSource(edge);
			String tgt = loopbodyGraph.getEdgeTarget(edge);
			if (partition.getShortLoops().contains(src + tgt)) {
				bodyShortLoops.add(src + tgt);
			}
		}
		
		Partition bodypartition = new Partition(loopbodyGraph, partition.getStartNodes(), partition.getEndNodes(), bodyShortLoops) ;
		//Add Head / Body
		partitions.add(bodypartition);
		
		//Add the rest redo part
		partitions.addAll(getNewPartitionList(loopPartPaths, partition));
		return new Cut(partitions,partition, OperatorType.LOOP);	
			
		} else {
			// Cut is not valid to the assumptions -> flower modell
			
			partitions.add(partition);
			return new Cut(partitions,partition, OperatorType.FLOWER);	
		}
		
	}

	
	/**
	 * Classifies and creates the paths between start and end nodes.
	 * @param g
	 * @param startNodes
	 * @param endNodes
	 * @return
	 */
	private Multimap<PathType, LoopPath> createLoopPaths(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> g,
			Set<String> startNodes, Set<String> endNodes) {
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> dwGraphCutted = getDeepCloneOfGraph(g);
		// Get Path by removing start and end nodes
		dwGraphCutted.removeAllVertices(startNodes);
		dwGraphCutted.removeAllVertices(endNodes);
		
		Multimap<PathType, LoopPath> pathMap = HashMultimap.create();
		
		if(dwGraphCutted.vertexSet().isEmpty()){
			pathMap.put(PathType.NotValid,  new LoopPath());
			return pathMap;
		}
		ConnectivityInspector<String, DefaultWeightedEdge> ci = new ConnectivityInspector<String, DefaultWeightedEdge>(
				dwGraphCutted);
		
		for (Set<String> component : ci.connectedSets()) {
			boolean startsWithStartNode = false;
			boolean startsWithEndNode = false;
			boolean endsWithStartNode = false;
			boolean endsWithEndNode = false;
			LoopPath loopPath = new LoopPath();
			loopPath.setPath(component);
			
			for (String node : component) {
				
				// Add start and endnodes to the component	
				for (DefaultWeightedEdge incomingEdge : g.incomingEdgesOf(node)) {
					// Component starts with a startnode
					for (String startNode : startNodes) {
						if ((g.getEdgeSource(incomingEdge)).equals(startNode)) {
							loopPath.putStartNode(startNode, PathPosition.START);
							startsWithStartNode = true;
						}
					}
					// Component starts with an endnode
					for (String endNode : endNodes) {
						if (g.getEdgeSource(incomingEdge).equals(endNode)) {
							loopPath.putEndNode(endNode, PathPosition.START);
							startsWithEndNode = true;
						}
					}
				}
				
				for (DefaultWeightedEdge outgoingEdge : g.outgoingEdgesOf(node)) {
					// Component ends with an endnode
					for (String endNode : endNodes) {
						if ((g.getEdgeTarget(outgoingEdge)).equals(endNode)) {
							loopPath.putEndNode(endNode, PathPosition.END);
							endsWithEndNode = true;
						}
						
					}
					// Component ends with a startnode
					for (String startNode : startNodes) {
						if ((g.getEdgeTarget(outgoingEdge)).equals(startNode)) {
							loopPath.putStartNode(startNode, PathPosition.END);
							endsWithStartNode = true;
						}
					}
				}
			}
			// classify component by its start and endnodes
			if(startsWithStartNode && !startsWithEndNode && endsWithEndNode && !endsWithStartNode){
				pathMap.put(PathType.StartToEnd, loopPath);
			} else if(startsWithEndNode && endsWithStartNode && !startsWithStartNode && !endsWithEndNode){
				pathMap.put(PathType.EndToStart, loopPath);
			} else if (startsWithStartNode && startsWithEndNode && endsWithEndNode && endsWithStartNode && (loopPath.startnodes.equals(loopPath.endNodes))){
				pathMap.put(PathType.StartEqualEnd,loopPath);
			} else {
				pathMap.put(PathType.NotValid, loopPath);
			}
				
		}
		return pathMap;
	}

	private DirectedWeightedPseudograph<String, DefaultWeightedEdge> getDeepCloneOfGraph(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> g) {
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> copy = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		Graphs.addAllVertices(copy, g.vertexSet());
		Graphs.addAllEdges(copy, g, g.edgeSet());
		return copy;
	}

	private class LoopPath {
		private Set<String> path;
		private Map<String, PathPosition> startnodes;
		private Map<String, PathPosition> endNodes;

		LoopPath() {
			path = Sets.newHashSet();
			startnodes = Maps.newHashMap();
			endNodes = Maps.newHashMap();
		}

		public Set<String> getPath() {
			return path;
		}
		public void setPath(Set<String> path) {
			this.path = path;
		}

		public void putStartNode(String node, PathPosition position) {
			this.startnodes.put(node, position);
		}
		public void putEndNode(String node, PathPosition position) {
			this.endNodes.put(node, position);
		}

		@SuppressWarnings("unused")
		public Map<String, PathPosition> getStartnodes() {
			return startnodes;
		}
		@SuppressWarnings("unused")
		public void setStartnodes(Map<String, PathPosition> startnodes) {
			this.startnodes = startnodes;
		}
		@SuppressWarnings("unused")
		public Map<String, PathPosition> getEndNodes() {
			return endNodes;
		}
		@SuppressWarnings("unused")
		public void setEndNodes(Map<String, PathPosition> endNodes) {
			this.endNodes = endNodes;
		}

	}

	enum PathType{
		StartEqualEnd,
		StartToEnd,
		EndToStart,
		NotValid
	}
	enum PathPosition {
		START, // Kante des knoten geht in den Weg über
		END // Kante des Knoten
	}
}
