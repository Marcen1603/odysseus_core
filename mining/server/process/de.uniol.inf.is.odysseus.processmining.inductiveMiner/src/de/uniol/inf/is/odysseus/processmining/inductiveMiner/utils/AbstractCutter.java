package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;

public abstract class AbstractCutter {

	abstract public Cut getCut(Partition partition);

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
			if (!subPartitionNodes.isEmpty()) {
				partitionList.add(PartitionUtils.createNewPartitionFrom(
						subPartitionNodes, partition));
			}
		}
		return partitionList;
	}
}
