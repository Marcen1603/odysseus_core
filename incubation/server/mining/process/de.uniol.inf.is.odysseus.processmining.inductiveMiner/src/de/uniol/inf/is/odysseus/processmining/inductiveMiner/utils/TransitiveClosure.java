package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class TransitiveClosure {

	private static TransitiveClosure instance;

	private TransitiveClosure() {
	};

	public static synchronized TransitiveClosure getInstance() {
		if (TransitiveClosure.instance == null) {
			TransitiveClosure.instance = new TransitiveClosure();
		}
		return TransitiveClosure.instance;
	}

	/**
	 * Creates the transitive closure of the compressedGraph
	 * 
	 * @param graph
	 * @param strongConnectedComponents
	 * @return
	 */
	public static Multimap<String, String> getTransitiveClosureOf(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph) {
		// Create transitive closure
		Multimap<String, String> reachabilityFromToMap = HashMultimap.create();
		Multimap<String, String> reachabilityToMap = HashMultimap.create();

		// Create Directly Follower Multimap
		for (String graphNode : graph.vertexSet()) {
			for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(graphNode)) {
				if (edge != null) {
					String followerNode = graph.getEdgeTarget(edge);
					reachabilityToMap.put(graphNode, followerNode);
				}
			}
		}

		// Create transitive closure
		for (String node : graph.vertexSet()) {
			reachabilityFromToMap.putAll(node,
					getTransitivNodes(node, reachabilityToMap));
		}

		for (String e : reachabilityFromToMap.keySet()) {
			System.out.print(e);
			for (String fol : reachabilityFromToMap.get(e)) {
				System.out.print("followedby: " + fol);
			}
		}
		return reachabilityFromToMap;
	}

	/**
	 * Recursivly gets the transitive Nodes of inNode from the reachability Map
	 * 
	 * @param inNode
	 * @param reachability
	 * @return
	 */
	private static Set<String> getTransitivNodes(String inNode,
			Multimap<String, String> reachability) {
		Set<String> transReachAbleNodes = Sets.newHashSet();
		if (!reachability.get(inNode).isEmpty()) {

			for (String reachableNode : reachability.get(inNode)) {
				transReachAbleNodes.add(reachableNode);
				transReachAbleNodes.addAll(getTransitivNodes(reachableNode,
						reachability));
			}
		}

		return transReachAbleNodes;
	}
}
