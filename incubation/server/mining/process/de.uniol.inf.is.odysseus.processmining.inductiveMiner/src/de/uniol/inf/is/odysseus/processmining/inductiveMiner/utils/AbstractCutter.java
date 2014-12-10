package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;

public abstract class AbstractCutter {

	abstract public Cut getCut(Partition partition);

	/**
	 * Creates a new DirectedWeightedGraph with the given nodes and add the
	 * internal associated edges from graph
	 * 
	 * @param nodes
	 * @param graph
	 * @return
	 */
	protected static DirectedWeightedPseudograph<String, DefaultWeightedEdge> createPartitionedGraph(
			Collection<String> nodes,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph) {

		DirectedWeightedPseudograph<String, DefaultWeightedEdge> graphPartition = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		Graphs.addAllVertices(graphPartition, nodes);
		for (String node : graphPartition.vertexSet()) {
			for (DefaultWeightedEdge edge : graph.edgesOf(node)) {
				String src = graph.getEdgeSource(edge);
				String target = graph.getEdgeTarget(edge);
				double weight = graph.getEdgeWeight(edge);
				if (isPartitionInternalEdge(graphPartition, node, src, target)) {
					// Create internal Edges
					// System.out.println("W: " + weight);
					graphPartition.addEdge(node, target);
					DefaultWeightedEdge we = graphPartition
							.getEdge(src, target);
					graphPartition.setEdgeWeight(we, weight);
				}
			}
		}
		// System.out.println("P: " + graphPartition);
		return graphPartition;
	}

	protected Set<String> getNewInternalshortLoops(
			Set<String> shortLoops,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> subPartitionGraph) {
		Set<String> newShortLoops = Sets.newHashSet();
		for (String loop : shortLoops) {
			String loopSrc = loop.substring(0, 1);
			String loopTraget = loop.substring(1, 2);
			if (subPartitionGraph.containsVertex(loopSrc)
					&& subPartitionGraph.containsVertex(loopTraget)) {
				newShortLoops.add(loop);
			}
		}
		return newShortLoops;
	}

	private static boolean isPartitionInternalEdge(
			DirectedPseudograph<String, DefaultWeightedEdge> partitionGraph,
			String actualNode, String src, String target) {
		return partitionGraph.containsVertex(target) && src.equals(actualNode);
	}

	/**
	 * Returns a DirectedWeightedPseudograph with the compressed, strongly
	 * connected Subpartitions of dwGraph as Nodes and the Edges between those
	 * compressed Nodes
	 * 
	 * @param subPartitionList
	 *            strongly connected component set
	 * @param dwGraph
	 *            the basic DirectedWeightedPseudograph of the Partition
	 * @return
	 */
	protected DirectedWeightedPseudograph<Set<String>, DefaultWeightedEdge> getCompressedGraph(
			Collection<Set<String>> subPartitionList,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> dwGraph) {

		DirectedWeightedPseudograph<Set<String>, DefaultWeightedEdge> compressedGraph = new DirectedWeightedPseudograph<Set<String>, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		Graphs.addAllVertices(compressedGraph, subPartitionList);

		for (DefaultWeightedEdge edge : dwGraph.edgeSet()) {
			// Variables
			String srcNode = dwGraph.getEdgeSource(edge);
			String targetNode = dwGraph.getEdgeTarget(edge);
			Set<String> compressedSrcNode = null;
			Set<String> compressedTargetNode = null;

			// Get Compressed Nodes to Compare
			for (Set<String> subpartition : subPartitionList) {
				if (subpartition.contains(srcNode)) {
					compressedSrcNode = subpartition;
				}
			}

			for (Set<String> subpartition : subPartitionList) {
				if (subpartition.contains(targetNode)) {
					compressedTargetNode = subpartition;
				}
			}

			// Compare References, if not equal the compressedNodes are
			// different
			if ((compressedSrcNode != null && compressedTargetNode != null)) {
				if (compressedSrcNode != compressedTargetNode) {
					compressedGraph.addEdge(compressedSrcNode,
							compressedTargetNode);
				}
			}

		}
		return compressedGraph;
	}


	protected LinkedList<Partition> getNewPartitionList(
			List<Set<String>> nodeSet, Partition partition) {
		LinkedList<Partition> partitionList = Lists.newLinkedList();
		
		for (Set<String> subPartitionNodes : nodeSet) {
//			System.out.println("PARTS:" +nodeSet);
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> tempGraphRef = createPartitionedGraph(
					subPartitionNodes, partition.getGraph());
			
			HashMap<String, Integer> tempStartNodes = Maps.newHashMap();
			HashMap<String, Integer> tempEndNodes = Maps.newHashMap();
			Set<String> tempShoortloops = Sets.newHashSet();
			// Reset Parentpartition
			Set<String> parentPartitionsNodes = Sets.newHashSet(partition.getGraph()
					.vertexSet());

			// Build complement node set
			parentPartitionsNodes.removeAll(subPartitionNodes);
			
			for (String node : subPartitionNodes) {
				// System.out.println("NODE: "+node);
				
				for (DefaultWeightedEdge edge : partition.getGraph().edgesOf(
						node)) {
					String targetNode = partition.getGraph().getEdgeTarget(
							edge);
					String srcNode = partition.getGraph()
							.getEdgeSource(edge);
					if (parentPartitionsNodes.contains(targetNode)) {
						// Outgoing edge
						tempEndNodes.put(node, (int) partition.getGraph()
								.getEdgeWeight(edge));
					} else if (parentPartitionsNodes.contains(srcNode)) {
						tempStartNodes.put(node, (int) partition.getGraph()
								.getEdgeWeight(edge));
					} else if (partition.getEndNodes().containsKey(node)) {
						tempEndNodes.put(node, partition.getEndNodes()
								.get(node));
					} else if (partition.getStartNodes().containsKey(node)){
						tempStartNodes.put(node, partition.getStartNodes().get(node));
					}
				}

			}
			
			for(String node : subPartitionNodes){
				if(partition.getStartNodes().containsKey(node)){
					tempStartNodes.put(node, partition.getStartNodes().get(node));
				}
				if(partition.getEndNodes().containsKey(node)){
					tempEndNodes.put(node, partition.getEndNodes().get(node));
				}
			}
			
			if(tempGraphRef.vertexSet().size() != 0 ){
			partitionList.add(new Partition(tempGraphRef, tempStartNodes,
					tempEndNodes, tempShoortloops));}
		}
//		if(partitionList.isEmpty()){
////			System.out.println("EMPTY");
//			partitionList.addFirst(partition);
//		}
		return partitionList;
	}
}
