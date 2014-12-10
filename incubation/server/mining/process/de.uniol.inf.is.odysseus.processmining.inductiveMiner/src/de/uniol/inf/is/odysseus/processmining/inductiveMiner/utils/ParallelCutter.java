package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.OperatorType;

public class ParallelCutter extends AbstractCutter {

	@Override
	public Cut getCut(Partition partition) {
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> dwGraph = partition
				.getGraph();
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> nGraph;
		removeKnownShortLoopsFrom(dwGraph, partition.getShortLoops());
		nGraph = getNegativeGraphOf(dwGraph);

		ConnectivityInspector<String, DefaultWeightedEdge> nGraphconnectivityInspector = new ConnectivityInspector<String, DefaultWeightedEdge>(
				nGraph);

		List<Set<String>> partitions = nGraphconnectivityInspector
				.connectedSets();

		return new Cut(getNewPartitionList(partitions,
				partition), OperatorType.PARALLEL);
	}

	private static List<DirectedWeightedPseudograph<String, DefaultWeightedEdge>> createPartitionlist(
			List<Set<String>> stringPartitions,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> dGraph) {

		// TODO Neue Knoten und kanten

		List<DirectedWeightedPseudograph<String, DefaultWeightedEdge>> partitions = Lists
				.newLinkedList();

		for (Set<String> strPartition : stringPartitions) {
			partitions.add(createPartitionGraph(strPartition, dGraph));
		}

		return partitions;
	}

	private static DirectedWeightedPseudograph<String, DefaultWeightedEdge> createPartitionGraph(
			Set<String> nodes,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph) {

		DirectedWeightedPseudograph<String, DefaultWeightedEdge> partition = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		Graphs.addAllVertices(partition, nodes);
		for (String node : partition.vertexSet()) {
			for (DefaultWeightedEdge edge : graph.edgesOf(node)) {
				String src = graph.getEdgeSource(edge);
				String target = graph.getEdgeTarget(edge);
				if (isPartitionInternalEdge(partition, node, src, target)) {
					// Erzeuge nur interne Kanten
					partition.addEdge(node, graph.getEdgeTarget(edge));
				}
			}
		}
		System.out.println("P: " + partition);

		return partition;
	}

	private static boolean isPartitionInternalEdge(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> partition,
			String actualNode, String src, String target) {
		return partition.containsVertex(target) && src.equals(actualNode);
	}

	/**
	 * Creates an inverted graph of dwGraph
	 * 
	 * @param dwGraph
	 * @return
	 */
	private static DirectedWeightedPseudograph<String, DefaultWeightedEdge> getNegativeGraphOf(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> dwGraph) {
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> nGraph = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		// Add all Nodes
		Graphs.addAllVertices(nGraph, dwGraph.vertexSet());

		// Add only edges which donot go in both directions
		for (String srcNode : dwGraph.vertexSet()) {
			for (String targetNode : dwGraph.vertexSet()) {
				if (!(srcNode.equals(targetNode))) {
					if (isNegativeEdge(dwGraph, srcNode, targetNode)) {
						nGraph.addEdge(srcNode, targetNode);
					}
				}
			}
		}
		return nGraph;
	}

	private static boolean isNegativeEdge(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> dGraph,
			String src, String target) {
		return !dGraph.containsEdge(src, target)
				|| !dGraph.containsEdge(target, src);
	}

	private static void removeKnownShortLoopsFrom(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> dwGraph,
			Set<String> shortLoops) {
		for (String loop : shortLoops) {
			String src = loop.substring(0, 1);
			String target = loop.substring(1, 2);
			System.out.println(src);
			System.out.println(target);
			if (dwGraph.containsEdge(src, target)) {
				dwGraph.removeEdge(src, target);
			}
			if (dwGraph.containsEdge(target, src)) {
				dwGraph.removeAllEdges(target, src);
			}

		}
	}

}
