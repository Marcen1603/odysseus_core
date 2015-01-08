package de.uniol.inf.is.odysseus.processmining.inductiveMiner.miner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.processmining.common.AbstractLCTuple;
import de.uniol.inf.is.odysseus.processmining.common.DFRTuple;
import de.uniol.inf.is.odysseus.processmining.common.InductiveMinerTransferTupleHelper;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.GeneratingStrategy;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.ProcessTreeModel;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Partition;

public abstract class AbstractInductiveMiner<T extends IMetaAttribute> implements IInductiveMiner<T>{

	protected InductiveMinerTransferTupleHelper<T> transferHelper = new InductiveMinerTransferTupleHelper<T>();
	protected Partition currentsFirstPartition;
	protected ProcessTreeModel mostRecentTreeModel;
	private GeneratingStrategy type;
	
	protected AbstractInductiveMiner(GeneratingStrategy type) {
		this.type = type;
	}
	
	protected void updateData(ProcessTreeModel mostRecentModell,
			Tuple<T> currentTuple) {
		currentsFirstPartition = null;
		currentsFirstPartition = createPartition(currentTuple);
		this.mostRecentTreeModel = mostRecentModell;
	}

	protected Partition createPartition(Tuple<T> currentTuple) {
		HashMap<Object, AbstractLCTuple<T>> relations = transferHelper
				.getDirectlyFollowRelations(currentTuple);
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentDirectlyFollowGraph = getGraph(relations);
		Set<String> shortLoops = convertShortLoops(transferHelper
				.getShortLoops(currentTuple));
		HashMap<String, Integer> startActivities = convertStartActivities(transferHelper
				.getStartActivites(currentTuple));
		HashMap<String, Integer> endActivities = (HashMap<String, Integer>) calculateStartPartitionEndNodes(
				currentDirectlyFollowGraph,
				transferHelper.getEndActivities(currentTuple));

		return new Partition(currentDirectlyFollowGraph, startActivities, endActivities, shortLoops);
	}

	private DirectedWeightedPseudograph<String, DefaultWeightedEdge> getGraph(
			HashMap<Object, AbstractLCTuple<T>> dfrel) {

		DirectedWeightedPseudograph<String, DefaultWeightedEdge> dfg = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		for (Map.Entry<Object, AbstractLCTuple<T>> entry : dfrel.entrySet()) {

			DFRTuple<T> t = (DFRTuple<T>) entry.getValue();
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

	private Set<String> convertShortLoops(HashMap<Object, AbstractLCTuple<T>> sl) {
		Set<String> shortLoopSet = Sets.newHashSet();
		Set<Object> loops = sl.keySet();
		for (Object l : loops) {
			shortLoopSet.add((String) l);
		}
		return shortLoopSet;
	}

	private HashMap<String, Integer> convertStartActivities(
			HashMap<Object, AbstractLCTuple<T>> startActivities) {
		HashMap<String, Integer> startNodes = Maps.newHashMap();
		for (Map.Entry<Object, AbstractLCTuple<T>> e : startActivities
				.entrySet()) {
			startNodes.put(((AbstractLCTuple<T>) e.getValue()).getActivity(),
					((AbstractLCTuple<T>) e.getValue()).getFrequency());
		}
		return startNodes;
	}

	private Map<String, Integer> calculateStartPartitionEndNodes(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> dfg,
			HashMap<Object, AbstractLCTuple<T>> endActivities) {

		Map<String, Integer> endings = Maps.newHashMap();
		for (String node : dfg.vertexSet()) {
			if (dfg.outDegreeOf(node) == 0) {
				endings.put(node, 1);
			}
		}
		double highestFrequence=0;
		for (Object key : endActivities.keySet()) {
			highestFrequence = endActivities.get(key).getFrequency() > highestFrequence ? endActivities.get(key).getFrequency() : highestFrequence;
		}
		highestFrequence = highestFrequence*0.07;
		for (Object key : endActivities.keySet()) {
			if (endActivities.get(key).getFrequency() > highestFrequence) {
				endings.put(endActivities.get(key).getActivity(), endActivities
						.get(key).getFrequency());
			}
		}

		if (endings.isEmpty()) {
			StrongConnectivityInspector<String, DefaultWeightedEdge> sci = new StrongConnectivityInspector<String, DefaultWeightedEdge>(
					dfg);
			List<Set<String>> sccs = sci.stronglyConnectedSets();
			Set<String> alternativeEndings = Sets.newHashSet();

			for (Set<String> scc : sccs) {
				boolean lastScc = true;
				for (String node : scc) {
					for (DefaultWeightedEdge edge : dfg.edgesOf(node)) {
						if (!scc.contains(dfg.getEdgeTarget(edge))) {
							lastScc = false;
						}
					}
				}
				if (lastScc) {
					alternativeEndings.addAll(scc);
				}
			}
			if (!alternativeEndings.isEmpty()) {
				for (String altEndNode : alternativeEndings) {
					endings.put(altEndNode, 1);
				}

				return endings;
			} else {

			}

		}
		return endings;
	}

	public GeneratingStrategy getType() {
		return type;
	}

}
