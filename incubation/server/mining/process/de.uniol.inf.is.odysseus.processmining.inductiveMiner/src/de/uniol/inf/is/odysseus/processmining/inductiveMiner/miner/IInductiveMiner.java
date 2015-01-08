package de.uniol.inf.is.odysseus.processmining.inductiveMiner.miner;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.ProcessTreeModel;

public interface IInductiveMiner<T extends IMetaAttribute>{
	public ProcessTreeModel generate();
	public void updateData(ProcessTreeModel mostRecentModell,
			Tuple<T> mostRecentTuple, Tuple<T> currentTuple);
}
