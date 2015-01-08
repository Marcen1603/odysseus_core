package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.OperatorType;

public class SequenceCutter extends AbstractCutter {
	@Override
	public Cut getCut(Partition partition) {
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> dwGraph = partition
				.getGraph();

		StrongConnectivityInspector<String, DefaultWeightedEdge> strongConnectivityInspector = new StrongConnectivityInspector<String, DefaultWeightedEdge>(
				dwGraph);

		List<Set<String>> strongConnectedComponents = strongConnectivityInspector
				.stronglyConnectedSets();
		DirectedWeightedPseudograph<Set<String>, DefaultWeightedEdge> compressedGraph = getCompressedGraph(
				strongConnectedComponents, dwGraph);

		Multimap<Set<String>, Set<String>> reachabilityFromToMap = getTransitiveClosure(compressedGraph);

		Set<Set<String>> pairWiseUnreachable = getPairwiseUnreachable(
				reachabilityFromToMap, strongConnectedComponents);
		List<Set<String>> connectedXorSets = getXorComponents(
				pairWiseUnreachable, dwGraph);

		compressedGraph.removeAllVertices(pairWiseUnreachable);
		Set<Set<String>> mergedAndFlattend = getMergedFinalGraphWithCuts(
				connectedXorSets, compressedGraph, dwGraph);

		mergedAndFlattend.addAll(compressedGraph.vertexSet());
		DirectedWeightedPseudograph<Set<String>, DefaultWeightedEdge> finalCompressedPartitionedGraph = getCompressedGraph(
				mergedAndFlattend, dwGraph);
		StrongConnectivityInspector<Set<String>, DefaultWeightedEdge> finalPartitioning = new StrongConnectivityInspector<Set<String>, DefaultWeightedEdge>(
				finalCompressedPartitionedGraph);

		List<Set<Set<String>>> finalNodeSets = finalPartitioning
				.stronglyConnectedSets();
		return new Cut(createPartitionLists(finalNodeSets, partition),partition,
				OperatorType.SEQUENCE);
	}

	private LinkedList<Partition> createPartitionLists(
			List<Set<Set<String>>> finalNodeSets, Partition partition) {
		List<Set<String>> flattenedNodeSet = Lists.newArrayList();

		for (Set<Set<String>> partition2 : finalNodeSets) {
			for (Set<String> set : partition2) {
				flattenedNodeSet.add(set);
			}
		}
		return getNewPartitionList(flattenedNodeSet, partition);
	}

	/**
	 * Merges the xorComponents of the compressedGraph and returns them
	 * 
	 * @param connectedXorSets
	 * @param compressedGraph
	 * @param dwGraph
	 * @return
	 */
	private Set<Set<String>> getMergedFinalGraphWithCuts(
			List<Set<String>> connectedXorSets,
			DirectedWeightedPseudograph<Set<String>, DefaultWeightedEdge> compressedGraph,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> dwGraph) {
		Set<Set<String>> mergedFlattenXorComponents = Sets.newHashSet();

		// Add Vertices
		DirectedWeightedPseudograph<Set<String>, DefaultWeightedEdge> compressedXorGraph = new DirectedWeightedPseudograph<Set<String>, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		for (Set<String> xor : connectedXorSets) {
			compressedXorGraph.addVertex(xor);
		}
		for (Set<String> stronglyComponents : compressedGraph.vertexSet()) {
			compressedXorGraph.addVertex(stronglyComponents);
		}

		// Add Edges between compressed components
		Set<DefaultWeightedEdge> edges = dwGraph.edgeSet();
		for (DefaultWeightedEdge edge : edges) {
			String src = dwGraph.getEdgeSource(edge);
			String target = dwGraph.getEdgeTarget(edge);
			for (Set<String> composedVertex : compressedXorGraph.vertexSet()) {
				for (Set<String> composedVertex2 : compressedXorGraph
						.vertexSet()) {
					if (composedVertex != composedVertex2) {
						if (composedVertex.contains(src)
								&& composedVertex2.contains(target)) {
							if (!compressedXorGraph.containsEdge(
									composedVertex, composedVertex2)) {
								compressedXorGraph.addEdge(composedVertex,
										composedVertex2);
							}
						}
					}
				}
			}
		}

		// Merge and flatten XorComponents
		Multimap<Set<String>, Set<String>> edgeSourceToTarget = ArrayListMultimap
				.create();
		Multimap<Set<String>, Set<String>> edgeTargetToSource = ArrayListMultimap
				.create();
		for (DefaultWeightedEdge edge : compressedXorGraph.edgeSet()) {
			Set<String> src = compressedXorGraph.getEdgeSource(edge);
			Set<String> target = compressedXorGraph.getEdgeTarget(edge);
			edgeSourceToTarget.put(src, target);
			edgeSourceToTarget.put(target, src);
		}
		// Flattens the xorcomponents to a single one
		for (Set<String> key : edgeSourceToTarget.asMap().keySet()) {
			Set<String> mergedComp = Sets.newHashSet();
			if (edgeSourceToTarget.get(key).size() > 1) {
				for (Set<String> xorComponent : edgeSourceToTarget.get(key)) {
					if (connectedXorSets.contains(xorComponent)) {
						for (String singleNode : xorComponent) {
							mergedComp.add(singleNode);
						}
					}
				}
			}

			if (edgeTargetToSource.get(key).size() > 1) {
				for (Set<String> xorComponent : edgeTargetToSource.get(key)) {
					if (connectedXorSets.contains(xorComponent)) {
						for (String singleNode : xorComponent) {
							mergedComp.add(singleNode);
						}
					}
				}
			}
			mergedFlattenXorComponents.add(mergedComp);
		}

		return mergedFlattenXorComponents;
	}

	/**
	 * Recursivly gets the transitive Nodes of inNode from the reachability Map
	 * 
	 * @param inNode
	 * @param reachability
	 * @return
	 */
	private Set<Set<String>> getTransitivNodes(Set<String> inNode,
			Multimap<Set<String>, Set<String>> reachability) {
		Set<Set<String>> transReachAbleNodes = Sets.newHashSet();
		if (!reachability.get(inNode).isEmpty()) {

			for (Set<String> reachableNode : reachability.get(inNode)) {
				transReachAbleNodes.add(reachableNode);
				transReachAbleNodes.addAll(getTransitivNodes(reachableNode,
						reachability));
			}
		}
		return transReachAbleNodes;
	}

	/**
	 * Creates the transitive closure of the compressedGraph
	 * 
	 * @param compressedGraph
	 * @param strongConnectedComponents
	 * @return
	 */
	private Multimap<Set<String>, Set<String>> getTransitiveClosure(
			DirectedWeightedPseudograph<Set<String>, DefaultWeightedEdge> compressedGraph) {

		// Create transitive closure
		Multimap<Set<String>, Set<String>> reachabilityFromToMap = HashMultimap
				.create();
		Multimap<Set<String>, Set<String>> reachabilityToMap = HashMultimap
				.create();

		// Create Directly Follower Multimap
		for (Set<String> cgNode : compressedGraph.vertexSet()) {
			for (DefaultWeightedEdge edge : compressedGraph
					.outgoingEdgesOf(cgNode)) {
				if (edge != null) {
					Set<String> followerNode = compressedGraph
							.getEdgeTarget(edge);
					reachabilityToMap.put(cgNode, followerNode);
				}
			}
		}

		// Create transitive closure
		for (Set<String> node : compressedGraph.vertexSet()) {
			reachabilityFromToMap.putAll(node,
					getTransitivNodes(node, reachabilityToMap));
		}
		return reachabilityFromToMap;
	}

	/**
	 * Creates the Set of pairwise unreachable nodes for further xorComponents
	 * determining
	 * 
	 * @param reachabilityFromToMap
	 * @param strongConnectedComponents
	 * @return
	 */
	private Set<Set<String>> getPairwiseUnreachable(
			Multimap<Set<String>, Set<String>> reachabilityFromToMap,
			List<Set<String>> strongConnectedComponents) {
		// pairwise comparison
		Set<Set<String>> pairWiseUnreachable = Sets.newHashSet();

		for (Set<String> s1 : strongConnectedComponents) {
			for (Set<String> s2 : strongConnectedComponents) {
				if (s1 != s2) {
					if (!(reachabilityFromToMap.get(s1).contains(s2))
							&& !(reachabilityFromToMap.get(s2).contains(s1))) {
						pairWiseUnreachable.add(s1);
						pairWiseUnreachable.add(s2);
					}
				}

			}
		}
		return pairWiseUnreachable;
	}

	/**
	 * Gets the XOR Components of the Sequence Cut
	 * 
	 * @param pairWiseUnreachable
	 * @param dwGraph
	 * @return
	 */
	private List<Set<String>> getXorComponents(
			Set<Set<String>> pairWiseUnreachable,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> dwGraph) {
		Set<String> mergedSet = Sets.newHashSet();

		for (Set<String> s : pairWiseUnreachable) {
			mergedSet.addAll(s);
		}
		if (mergedSet.size() == 1) {
			List<Set<String>> singleNode = Lists.newArrayList();
			singleNode.add(mergedSet);
			return singleNode;

		} else {
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> mergedPartitionGraph = PartitionUtils.createPartitionedGraph(
					mergedSet, dwGraph);
			// Check Single Activities without Edges
			ConnectivityInspector<String, DefaultWeightedEdge> ci = new ConnectivityInspector<String, DefaultWeightedEdge>(
					mergedPartitionGraph);
			return ci.connectedSets();
		}
	}
}