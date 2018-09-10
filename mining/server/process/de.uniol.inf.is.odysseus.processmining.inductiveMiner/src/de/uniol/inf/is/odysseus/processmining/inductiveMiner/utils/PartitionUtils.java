package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PartitionUtils {

	public static Partition createNewPartitionFrom(
			Set<String> newPartitionNodes, Partition parentPartition) {
		HashMap<String, Integer> startNodes = Maps.newHashMap();
		HashMap<String, Integer> endNodes = Maps.newHashMap();
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> newSubgraph = createPartitionedGraph(
				newPartitionNodes, parentPartition.getGraph());

		// Build complement node set
		Set<String> complementNodes = Sets.newHashSet(parentPartition
				.getGraph().vertexSet());
		complementNodes.removeAll(newPartitionNodes);

		// Add all nodes
		for (String node : newPartitionNodes) {
			newSubgraph.addVertex(node);
		}

		// Add all existing edges between the changeEffectedPartitionNodes
		for (String node : newPartitionNodes) {
			if (parentPartition.getGraph().containsVertex(node)) {
				for (DefaultWeightedEdge edge : parentPartition.getGraph()
						.edgesOf(node)) {
					String targetNode = parentPartition.getGraph()
							.getEdgeTarget(edge);
					String srcNode = parentPartition.getGraph().getEdgeSource(
							edge);
					if (complementNodes.contains(targetNode)) {
						// Outgoing edge
						endNodes.put(node, (int) parentPartition.getGraph()
								.getEdgeWeight(edge));
					} else if (complementNodes.contains(srcNode)) {
						startNodes.put(node, (int) parentPartition.getGraph()
								.getEdgeWeight(edge));
					} else if (parentPartition.getEndNodes().containsKey(node)) {
						endNodes.put(node,
								parentPartition.getEndNodes().get(node));
					} else if (parentPartition.getStartNodes()
							.containsKey(node)) {
						startNodes.put(node, parentPartition.getStartNodes()
								.get(node));
					}
				}
			} 
		}

		for (String node : newPartitionNodes) {
			if (parentPartition.getStartNodes().containsKey(node)) {
				startNodes.put(node, parentPartition.getStartNodes().get(node));
			}
			if (parentPartition.getEndNodes().containsKey(node)) {
				endNodes.put(node, parentPartition.getEndNodes().get(node));
			}

		}

		if (newSubgraph.vertexSet().size() != 0) {
			return new Partition(newSubgraph, startNodes, endNodes,
					parentPartition.getShortLoops());
		}
		return null;

	}

	/**
	 * Creates a new DirectedWeightedGraph with the given nodes and add the
	 * internal associated edges from graph
	 * 
	 * @param nodes
	 * @param graph
	 * @return
	 */
	public static DirectedWeightedPseudograph<String, DefaultWeightedEdge> createPartitionedGraph(
			Collection<String> nodes,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph) {

		DirectedWeightedPseudograph<String, DefaultWeightedEdge> graphPartition = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		Graphs.addAllVertices(graphPartition, nodes);
		for (String node : graphPartition.vertexSet()) {
			if (graph.containsVertex(node)) {
				for (DefaultWeightedEdge edge : graph.edgesOf(node)) {
					String src = graph.getEdgeSource(edge);
					String target = graph.getEdgeTarget(edge);
					double weight = graph.getEdgeWeight(edge);
					if (isPartitionInternalEdge(graphPartition, node, src,
							target)) {
						graphPartition.addEdge(node, target);
						DefaultWeightedEdge we = graphPartition.getEdge(src,
								target);
						graphPartition.setEdgeWeight(we, weight);
					}
				}
			}
		}
		return graphPartition;
	}

	private static boolean isPartitionInternalEdge(
			DirectedPseudograph<String, DefaultWeightedEdge> partitionGraph,
			String actualNode, String src, String target) {
		return partitionGraph.containsVertex(target) && src.equals(actualNode);
	}
}
