package de.uniol.inf.is.odysseus.processmining.inductiveMiner.miner;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.GeneratingStrategy;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.ProcessTreeModel;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Cutter;

public class StandardInductiveMiner<T extends IMetaAttribute>
extends AbstractInductiveMiner<T>{
	
	private Multimap<Cut, Cut> newProcessTreeMap = HashMultimap.create();
	public StandardInductiveMiner() {
		super(GeneratingStrategy.FULL_REBUILD);
	}
	
	@Override
	public ProcessTreeModel generate() {
		newProcessTreeMap = Cutter.getCutMapOf(currentsFirstPartition);
		return new ProcessTreeModel(newProcessTreeMap, this.currentsFirstPartition.getGraph());
	}
	@Override
	public void updateData(ProcessTreeModel mostRecentModell,
			Tuple<T> mostRecentTuple, Tuple<T> currentTuple) {
		updateData(mostRecentModell, currentTuple);
	}

}
