package de.uniol.inf.is.odysseus.processmining.inductiveMiner.physicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.processmining.common.AbstractLCTuple;
import de.uniol.inf.is.odysseus.processmining.common.DFRTuple;
import de.uniol.inf.is.odysseus.processmining.common.InductiveMinerTransferTupleHelper;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Cutter;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Partition;

public class InductiveMinerPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	HashMap<Object, AbstractLCTuple<T>> relations;
	Set<String> shortLoops;
	HashMap<String, Integer> startActivities;
	HashMap<String, Integer> endActivities;
	Multimap<String, String> transitiveClosure = HashMultimap.create();
	private Multimap<Cut, Cut> cutMap = HashMultimap.create();
//	InvariantStrategyTypOne ist = new InvariantStrategyTypOne();
	Tuple<T> mostRecentTuple;
	InductiveMinerTransferTupleHelper<T> transferHelper = new InductiveMinerTransferTupleHelper<T>();
	public InductiveMinerPO() {
		super();
	}

	@Override
	protected void process_next(Tuple<T> object, int port) {
		startActivities = convertStartActivities(transferHelper
				.getStartActivites(object));
		shortLoops = convertShortLoops(transferHelper.getShortLoops(object));
		relations = transferHelper.getDirectlyFollowRelations(object);
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph = getGraph(relations);
		endActivities = (HashMap<String, Integer>) calculateStartPartitionEndNodes(
				graph, transferHelper.getEndActivities(object));

		// ReachabilityMatrix tClosure = new ReachabilityMatrix(graph);

		Partition startPartition = new Partition(graph, startActivities,
				endActivities, shortLoops);

		cutMap = Cutter.getCutMapOf(startPartition);
		System.out.println("TREESTART");
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> startgraph = startPartition
				.getGraph();
		System.out.println("Start Graph" + startgraph);
		for (DefaultWeightedEdge edge : startgraph.edgeSet()) {
			System.out.print("(" + startgraph.getEdgeSource(edge) + ":"
					+ startgraph.getEdgeTarget(edge) + ":"
					+ startgraph.getEdgeWeight(edge) + ")" + " ; ");
		}
		System.out.println();
		for (Cut e : cutMap.keySet()) {
			System.out.print(e.print());
			for (Cut fol : cutMap.get(e)) {
				System.out.print("followedby: " + fol.print());
			}
		}
		System.out.println("TREEEND");
		mostRecentTuple = object;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	private HashMap<String, Integer> convertStartActivities(
			HashMap<Object, AbstractLCTuple<T>> startActivities) {
		HashMap<String, Integer> startNodes = Maps.newHashMap();
		for (Map.Entry<Object, AbstractLCTuple<T>>e : startActivities.entrySet()) {
			startNodes.put(((AbstractLCTuple<T>) e.getValue()).getActivity(),
					((AbstractLCTuple<T>) e.getValue()).getFrequency());
		}
		return startNodes;
	}

	private Set<String> convertShortLoops(HashMap<Object, AbstractLCTuple<T>> sl) {
		Set<String> shortLoopSet = Sets.newHashSet();
		Set<Object> loops = sl.keySet();
		for (Object l : loops) {
			shortLoopSet.add((String) l);
		}
		return shortLoopSet;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (this == ipo)
			return true;
		if (!super.equals(ipo))
			return false;
		if (getClass() != ipo.getClass())
			return false;
		return true;
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

	private Map<String, Integer> calculateStartPartitionEndNodes(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> dfg,
			HashMap<Object, AbstractLCTuple<T>> endActivities) {
		System.out.println(endActivities);
		Map<String, Integer> endings = Maps.newHashMap();
		for (String node : dfg.vertexSet()) {
			if (dfg.outDegreeOf(node) == 0) {
				endings.put(node, 1);
			}
		}
		for (Object key : endActivities.keySet()) {
			if (endActivities.get(key).getFrequency() > 5) {
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
}
