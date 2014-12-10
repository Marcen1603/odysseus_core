package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.OperatorType;

public class LoopCutter extends AbstractCutter {

	public LoopCutter() {
	}

	@Override
	public Cut getCut(Partition partition) {
		// Graph References
		LinkedList<Partition> partitions = Lists.newLinkedList();

		DirectedWeightedPseudograph<String, DefaultWeightedEdge> dwGraph = partition
				.getGraph();
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> dwGraphCutted = getDeepCloneOfGraph(dwGraph);
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> loopHead = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		HashMap<String, Integer> startNodes = partition.getStartNodes();
		HashMap<String, Integer> endNodes = partition.getEndNodes();

		Set<String> startNodePartition = Sets.newHashSet(startNodes.keySet());
		Set<String> endNodePartition = Sets.newHashSet(endNodes.keySet());

		// Get Path by removing start and end nodes
		dwGraphCutted.removeAllVertices(startNodePartition);
		dwGraphCutted.removeAllVertices(endNodePartition);
		if(!endNodePartition.isEmpty()){
		ConnectivityInspector<String, DefaultWeightedEdge> ci = new ConnectivityInspector<String, DefaultWeightedEdge>(
				dwGraphCutted);

		List<Set<String>> paths = ci.connectedSets();
		List<List<String>> sortedPaths = Lists.newArrayList();
		LinkedList<String> loopHeadNodeList = Lists.newLinkedList();
		List<Set<String>> loopTailNodeList = Lists.newLinkedList();

		// Sort the single node of the paths between start and endactivities
		for (Set<String> pathSet : paths) {
			sortedPaths.add(sortSet(pathSet, dwGraphCutted));
		}
		
		if(!startNodes.equals(endNodes)){
		// Build head and tail partitions
		for (List<String> sortedPath : sortedPaths) {
			if (!sortedPath.isEmpty()) {
				int first = 0;
				int last = sortedPath.size()-1;
				for (String startNode : startNodePartition) {
					if (dwGraph.containsEdge(startNode, sortedPath.get(first))
							&& !(endNodePartition.contains(sortedPath.get(first)))) {
						loopHeadNodeList.addAll(sortedPath);
					}
				}
				for (String endNode : endNodePartition) {
					if (dwGraph.containsEdge(endNode, sortedPath.get(last))) {
						for (String startNode : startNodePartition) {
							if (dwGraph.containsEdge(sortedPath.get(last),
									startNode)) {
								loopTailNodeList.add(Sets
										.newHashSet(sortedPath));
							}
						}
					}
				}
			}
		}
		loopHeadNodeList.addAll(startNodePartition);
		loopHeadNodeList.addAll(endNodePartition);
		} else {
			// Start und Endknoten sind gleich
			startNodePartition = Sets.newHashSet(startNodes.keySet());
			endNodePartition = Sets.newHashSet();
			for(Set<String> path : paths){
				endNodePartition.addAll(path);
			}
			loopHeadNodeList.addAll(startNodePartition);
			loopTailNodeList.add(endNodePartition);
			
		}


		// Create Head
		loopHead = createPartitionedGraph(loopHeadNodeList, dwGraph);
		Set<String> headShortLoops = Sets.newHashSet();
		for (DefaultWeightedEdge edge : loopHead.edgeSet()) {
			String src = loopHead.getEdgeSource(edge);
			String tgt = loopHead.getEdgeTarget(edge);
			if (partition.getShortLoops().contains(src + tgt)) {
				headShortLoops.add(src + tgt);
			}
		}

		partitions.add(new Partition(loopHead, startNodes, endNodes,
				headShortLoops));

		// Create Tail
		// Determine new End and Start activities

		partitions.addAll(getNewPartitionList(loopTailNodeList, partition));
		return new Cut(partitions, OperatorType.LOOP);
		}
		partitions.add(partition);
			return new Cut(partitions,OperatorType.FLOWER);
	}

	private DirectedWeightedPseudograph<String, DefaultWeightedEdge> getDeepCloneOfGraph(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> g) {
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> copy = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		Graphs.addAllVertices(copy, g.vertexSet());
		Graphs.addAllEdges(copy, g, g.edgeSet());
		return copy;
	}
	
	protected LinkedList<String> sortSet(Set<String> set,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> g) {
		LinkedList<String> sortedNodes = Lists.newLinkedList();
		if (set.size() > 1) {
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> graphOfSet = createPartitionedGraph(
					set, g);
			CycleDetector<String, DefaultWeightedEdge> cycleDetection = new CycleDetector<String, DefaultWeightedEdge>(graphOfSet);
			if (!cycleDetection.detectCycles()) {
				// No Cycle
				
				TopologicalOrderIterator<String, DefaultWeightedEdge> topsort = new TopologicalOrderIterator<String, DefaultWeightedEdge>(
						graphOfSet);
				while (topsort.hasNext()) {
					sortedNodes.add(topsort.next());
				}
			} else {
				StrongConnectivityInspector<String, DefaultWeightedEdge> sci = new StrongConnectivityInspector<String, DefaultWeightedEdge>(graphOfSet);
				for( Set<String> connectedComponent : sci.stronglyConnectedSets()){

					sortedNodes.addAll(connectedComponent);
				}
			}
			return sortedNodes;
		}
		sortedNodes.addAll(set);
		return sortedNodes;
	}
	
}
