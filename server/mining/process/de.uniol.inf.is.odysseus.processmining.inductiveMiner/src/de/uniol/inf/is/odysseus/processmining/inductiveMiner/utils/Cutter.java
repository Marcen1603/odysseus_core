package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.OperatorType;

public class Cutter {

	private static ExclusiveChoiceCutter xorCutter = new ExclusiveChoiceCutter();
	private static SequenceCutter seqCutter = new SequenceCutter();
	private static LoopCutter2 loopCutter = new LoopCutter2();
	private static ParallelCutter pCutter = new ParallelCutter();
	private static Multimap<Cut, Cut> cutMap;

	public static Multimap<Cut, Cut> getCutMapOf(Partition p) {
		cutMap = HashMultimap.create();
		createTree(p, cutMap);
		return cutMap;
	}

	public static Cut createTree(Partition p, Multimap<Cut, Cut> cutMap) {
		Cut currentCut;
		if (p.getGraph().vertexSet().size() <= 1) {
			currentCut = Cut.createLeaf(p);
			cutMap.put(currentCut, Cut.getEmptyCut());
			return currentCut;
		} else {
			// Exclusive Choice Cut

			currentCut = xorCutter.getCut(p);
			if (!(currentCut.getCutPartitions().size() <= 1)
					&& !currentCut.getOperator().equals(OperatorType.FLOWER)) {
				for (Partition cutPartition : currentCut.getCutPartitions()) {
					cutMap.put(currentCut, createTree(cutPartition, cutMap));
				}
				return currentCut;
			}
			// Sequence Cut
			currentCut = seqCutter.getCut(p);
			if (!(currentCut.getCutPartitions().size() <= 1)
					&& !currentCut.getOperator().equals(OperatorType.FLOWER)) {
				for (Partition cutPartition : currentCut.getCutPartitions()) {
					cutMap.put(currentCut, createTree(cutPartition, cutMap));
				}
				return currentCut;
			}

			// Parallel Cut
			currentCut = pCutter.getCut(p);
			if (!(currentCut.getCutPartitions().size() <= 1)
					&& !currentCut.getOperator().equals(OperatorType.FLOWER)) {
				for (Partition cutPartition : currentCut.getCutPartitions()) {
					cutMap.put(currentCut, createTree(cutPartition, cutMap));
				}
				return currentCut;
			}

			// Loop Cut
			currentCut = loopCutter.getCut(p);
			if (!(currentCut.getCutPartitions().size() <= 1)
					&& !currentCut.getOperator().equals(OperatorType.FLOWER)) {
				for (Partition cutPartition : currentCut.getCutPartitions()) {
					cutMap.put(currentCut, createTree(cutPartition, cutMap));
				}
				return currentCut;
			}
		}
		cutMap.put(currentCut, Cut.getEmptyCut());
		return currentCut;
	}
}
