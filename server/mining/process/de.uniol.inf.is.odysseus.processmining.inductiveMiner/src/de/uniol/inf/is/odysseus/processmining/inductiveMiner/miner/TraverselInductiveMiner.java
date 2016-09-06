package de.uniol.inf.is.odysseus.processmining.inductiveMiner.miner;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.GeneratingStrategy;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.OperatorType;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.ProcessTreeModel;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.AbstractCutter;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Cutter;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.ExclusiveChoiceCutter;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.LoopCutter2;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.ParallelCutter;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Partition;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.SequenceCutter;

public class TraverselInductiveMiner<T extends IMetaAttribute>
		extends AbstractInductiveMiner<T> {

	private ExclusiveChoiceCutter xorCutter = new ExclusiveChoiceCutter();
	private SequenceCutter seqCutter = new SequenceCutter();
	private LoopCutter2 loopCutter = new LoopCutter2();
	private ParallelCutter pCutter = new ParallelCutter();
	private Multimap<Cut, Cut> newProcessTreeMap = HashMultimap.create();

	public TraverselInductiveMiner() {
		super(GeneratingStrategy.TRAVERSAL);
	}

	@Override
	public ProcessTreeModel generate() {
		
		newProcessTreeMap = HashMultimap.create();
		updateTree(mostRecentTreeModel.getRootCut(), this.currentsFirstPartition);
		// if the updated failed full rebuild
		if(newProcessTreeMap.isEmpty() || newProcessTreeMap == null){
			Cutter.createTree(currentsFirstPartition, newProcessTreeMap);
		}
		return new ProcessTreeModel(newProcessTreeMap,
				this.currentsFirstPartition.getGraph());
	}

	private Cut updateTree(Cut processTreeMapCut, Partition partitionToCut) {
		if (partitionToCut.getGraph().vertexSet().size() <= 1) {
			return Cut.createLeaf(partitionToCut);
		} else if(processTreeMapCut
				.getOperator().equals(OperatorType.FLOWER) || partitionToCut.getStartNodes().equals(partitionToCut.getEndNodes())){
			
			Cut partitionCut = Cutter.createTree(partitionToCut,
					newProcessTreeMap);
			return partitionCut;
			
		} else {
			AbstractCutter cutter = selectCutType(processTreeMapCut
					.getOperator());
			Cut partitionCut = cutter.getCut(partitionToCut);
			// Kein Blattknoten
			
			if (processTreeMapCut.equals(partitionCut)) {
				// Partitionen und Operator sind vollständig gleich
				for (Partition partition : partitionCut.getCutPartitions()) {
					for (Cut cut : mostRecentTreeModel.getProcessTreeMap().get(
							processTreeMapCut)) {
						if (cut.getAllNodes().equals(partition.getGraph().vertexSet())) {
							newProcessTreeMap.put(partitionCut,
									updateTree(cut, partition));
						}
					}
				}
				return partitionCut;

				// Gleicher Operator und gültiger Cut mit unteschiedlichen
				// Partitionen
			} else if (partitionCut.getCutPartitions().size() <= 1
					&& processTreeMapCut.getOperator().equals(
							partitionCut.getOperator())
					&& !(processTreeMapCut.getCutPartitions()
							.equals(partitionCut.getCutPartitions()))) {
				// Berechne neu und füge dem neuen Modell hinzu
				partitionCut = Cutter.createTree(partitionToCut,
						newProcessTreeMap);
				return partitionCut;
					
			} else {
				partitionCut = Cutter.createTree(partitionToCut,
						newProcessTreeMap);
				return partitionCut;
			}
		}
	}

	private AbstractCutter selectCutType(OperatorType type) {
		switch (type) {
		case XOR:
			return xorCutter;
		case SEQUENCE:
			return seqCutter;
		case PARALLEL:
			return pCutter;
		case LOOP:
			return loopCutter;
		case FLOWER:
			break;
		case SILENT:
			break;
		case SILENT_ENDING:
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	public void updateData(ProcessTreeModel mostRecentModell,
			Tuple<T> mostRecentTuple, Tuple<T> currentTuple) {
			updateData(mostRecentModell, currentTuple);
	}

}
