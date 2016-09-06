package de.uniol.inf.is.odysseus.processmining.inductiveMiner.physicaloperator;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.processmining.common.AbstractLCTuple;
import de.uniol.inf.is.odysseus.processmining.common.DFRTuple;
import de.uniol.inf.is.odysseus.processmining.common.InductiveMinerTransferTupleHelper;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.IInvariantResult;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.strategies.IInvariantStrategy;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.strategies.InvariantStrategyComponentAnalysis;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.strategies.InvariantStrategyTypOne;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.miner.IInductiveMiner;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.miner.SearchAndReplaceInductiveMiner;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.miner.TraverselInductiveMiner;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.miner.StandardInductiveMiner;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.GeneratingStrategy;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Invarianttype;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.ProcessTreeModel;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Cutter;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Partition;

public class InductiveMinerPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	// Modells
	private Multimap<Cut, Cut> cutMap = HashMultimap.create();
	private Tuple<T> mostRecentTuple;
	private ProcessTreeModel mostRecentModel;

	// Evaluations stuff
//	private List<Integer> knotendifferenz = Lists.newArrayList();
//	private List<Integer> kantendifferenz = Lists.newArrayList();
//	private Set<ProcessTreeModel> unterschiedlicheModells = Sets.newHashSet();
	private Map<String, Integer> difmap = Maps.newConcurrentMap();
	private List<Integer> reihenfolge = Lists.newArrayList();
	int mCount =0;
	// Utils
	private InductiveMinerTransferTupleHelper<T> transferHelper = new InductiveMinerTransferTupleHelper<T>();
	IInductiveMiner<T> inductiveMiner;
	IInvariantStrategy<T> invariantAnalyser;
	boolean performInvariantAnalysis = false;
	Invarianttype invariantstrategy;
	GeneratingStrategy generatingstrategy;
	int iterationCounter = 0;

	public InductiveMinerPO(Invarianttype invarianttype,
			GeneratingStrategy generatingStrategy) {
		super();
		this.invariantstrategy = invarianttype;
		this.generatingstrategy = generatingStrategy;
		initStrategies();
	}

	public InductiveMinerPO(InductiveMinerPO<T> inductiveMiner) {
		super(inductiveMiner);
		this.invariantstrategy = inductiveMiner.invariantstrategy;
		this.generatingstrategy = inductiveMiner.generatingstrategy;
		initStrategies();
	}

	private void initStrategies() {
		switch (invariantstrategy) {
		case NODE_INVARIANT:
			invariantAnalyser = new InvariantStrategyTypOne<T>();
			performInvariantAnalysis = true;
			break;
		case COMPONENT_INVARIANT:
			invariantAnalyser = new InvariantStrategyComponentAnalysis<T>();
			performInvariantAnalysis = true;
			break;
		case NONE:
			performInvariantAnalysis = false;
			break;
		default:
			performInvariantAnalysis = false;
			break;
		}

		switch (generatingstrategy) {
		case FULL_REBUILD:
			inductiveMiner = new StandardInductiveMiner<T>();
			break;
		case TRAVERSAL:
			inductiveMiner = new TraverselInductiveMiner<T>();
			break;
		case PARTITION_COMPARISON:
			inductiveMiner = new SearchAndReplaceInductiveMiner<T>();
			break;
		default:
			inductiveMiner = new StandardInductiveMiner<T>();
			break;
		}
	}


	@Override
	protected void process_next(Tuple<T> object, int port) {
		System.out.println("ENUM: " + invariantstrategy);
		System.out.println("ITERATION: " + iterationCounter);

		// Check if bucketWidth sufficent
		if (!transferHelper.getDirectlyFollowRelations(object).isEmpty()
				&& transferHelper.getDirectlyFollowRelations(object) != null) {

			// System.out.println("ANZAHL RELATIONEN: "
			// + transferHelper.getDirectlyFollowRelations(object)
			// .keySet().size());
			if (mostRecentTuple == null) {
				// First Process Tree
				createFullNewModell(object);
			} else {
				// Next Process Tree
				if (performInvariantAnalysis) {
					// Check Invariants
					IInvariantResult result = invariantAnalyser
							.calculateStrategy(mostRecentTuple, object);
					result.print();
					switch (result.getStrategy()) {
					case REBUILD:
						System.out.println("REBUILD");
						createFullNewModell(object);
						break;
					case UPDATE:
						System.out.println("UPDATE");
						inductiveMiner.updateData(mostRecentModel,
								mostRecentTuple, object);
						mostRecentModel = inductiveMiner.generate();
						break;
					case DELAY:
						System.out.println("DELAY");
						// Do nothing
						break;
					default:
						System.out.println("DEFAULT");
						createFullNewModell(object);
						break;
					}
					System.out.println(mostRecentModel.toString());
				} else {
					inductiveMiner.updateData(mostRecentModel, mostRecentTuple,
							object);
					mostRecentModel = inductiveMiner.generate();
					getDifferentModells();
					System.out.println(mostRecentModel.toString());
				}
				// Console Output
				// System.out.println("ProcessTree:");
				// System.out.println(mostRecentModel.toString());
				// System.out.println();
				// System.out.println();
				// System.out.println();

				mostRecentTuple = object;
				getDifferentModellsVerteilung();
				
				
				iterationCounter++;
				
				// Evaluation Stuff
//				if (iterationCounter >= 3000) {
//
//					for (String var : difmap.keySet()) {
//						System.out.println(var);
//						System.out.println("FREQ: " + difmap.get(var));
//						System.out.println();
//					}
//					System.out.println("Unterschiedliche Modelle: "+difmap.keySet().size());
//					listToTxt();
//					while (true)
//						;
//				}
			}

		} else {

			iterationCounter++;
			System.out.println("Lossy Counting - Bucketbreite zu gering");
		}

		Tuple<T> transferTuple = new Tuple<T>(1, true);
		transferTuple.setAttribute(0, mostRecentModel);
		transfer(transferTuple);
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	private void getDifferentModells() {
		if (difmap.containsKey(mostRecentModel.toString())) {

			int freq = difmap.get(mostRecentModel.toString());
			difmap.remove(mostRecentModel.toString());
			difmap.put(mostRecentModel.toString(), freq + 1);

		} else {
			difmap.put(mostRecentModel.toString(), 1);
		}

	}
	
	private void getDifferentModellsVerteilung() {
		if (!difmap.containsKey(mostRecentModel.toString())) {
			difmap.put(mostRecentModel.toString(), mCount);
			reihenfolge.add(mCount);
			mCount++;
		} else {
			reihenfolge.add(difmap.get(mostRecentModel.toString()));
		}
		
	}

	//  Evaluation Stuff
//	private void listToTxt (){
//		Writer fw = null;
//		try {
//			fw = new FileWriter(
//					"/home/phil/Documents/Masterarbeit/docs/ausarbeitung/evaluation/fileWriter.txt");
//			for (int i = 0; i < reihenfolge.size(); i++) {
//				fw.write(i +","+reihenfolge.get(i)+"\n");
//			}
//			reihenfolge.clear();
//		} catch (IOException e) {
//			System.err.println("Konnte Datei nicht erstellen");
//		} finally {
//			if (fw != null)
//				try {
//					fw.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//		}
//		
//	}
	

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	/**
	 * Methode für die Evaluation zur Bestimmung der Kanten und
	 * Knotenunterschiede
	 */
//	private void writeDifferenz() {
//		Writer fw = null;
//
//		try {
//			fw = new FileWriter(
//					"/home/phil/Documents/Masterarbeit/docs/ausarbeitung/evaluation/fileWriter.txt");
//			for (int i = 0; i < kantendifferenz.size(); i++) {
//				fw.write(kantendifferenz.get(i) + "," + knotendifferenz.get(i)
//						+ "\n");
//			}
//			kantendifferenz.clear();
//			knotendifferenz.clear();
//		} catch (IOException e) {
//			System.err.println("Konnte Datei nicht erstellen");
//		} finally {
//			if (fw != null)
//				try {
//					fw.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//		}
//	}

//	private void getDifferenz(InvariantResultTypOne res) {
//		kantendifferenz.add(Math.abs(res.getCurrentEdgeCount()
//				- res.getMostRecentEdgeCount()));
//		knotendifferenz.add(Math.abs(res.getCurrentNodesCount()
//				- res.getMostRecentNodesCount()));
//	}

	private void createFullNewModell(Tuple<T> object) {
		HashMap<String, Integer> startActivities = convertStartActivities(transferHelper
				.getStartActivites(object));
		Set<String> shortLoops = convertShortLoops(transferHelper
				.getShortLoops(object));
		HashMap<Object, AbstractLCTuple<T>> relations = transferHelper
				.getDirectlyFollowRelations(object);
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph = getGraph(relations);
		HashMap<String, Integer> endActivities = (HashMap<String, Integer>) calculateStartPartitionEndNodes(
				graph, transferHelper.getEndActivities(object));

		Partition startPartition = new Partition(graph, startActivities,
				endActivities, shortLoops);

		cutMap = Cutter.getCutMapOf(startPartition);
		mostRecentModel = new ProcessTreeModel(cutMap, graph);
		System.out.println(mostRecentModel.toString());
		mostRecentTuple = object;
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

		Map<String, Integer> endings = Maps.newHashMap();
		for (String node : dfg.vertexSet()) {
			if (dfg.outDegreeOf(node) == 0) {
				endings.put(node, 1);
			}
		}
		double highestFrequence = 0;
		for (Object key : endActivities.keySet()) {
			highestFrequence = endActivities.get(key).getFrequency() > highestFrequence ? endActivities
					.get(key).getFrequency() : highestFrequence;
		}
		highestFrequence = highestFrequence * 0.07;
		for (Object key : endActivities.keySet()) {
			if (endActivities.get(key).getFrequency() > highestFrequence) {
				endings.put(endActivities.get(key).getActivity(), endActivities
						.get(key).getFrequency());
			}
		}
		// No end activity found
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
