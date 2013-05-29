package de.uniol.inf.is.odysseus.core.server.physicaloperator.sa;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataComparator;


public abstract class AbstractTimeIntervalSweepArea<T extends IStreamObject<? extends ITimeInterval>> extends
		AbstractSweepArea<T> implements ITimeIntervalSweepArea<T>{


	public AbstractTimeIntervalSweepArea() {
		super();
	}

	
	public AbstractTimeIntervalSweepArea(FastArrayList<T> fastArrayList,
			MetadataComparator<ITimeInterval> metadataComparator) {
		super(fastArrayList, metadataComparator);
	}

	public AbstractTimeIntervalSweepArea(IFastList<T> list,
			MetadataComparator<ITimeInterval> metadataComparator) {
		super(list, metadataComparator);
	}

	public AbstractTimeIntervalSweepArea(
			AbstractTimeIntervalSweepArea<T> defaultTISweepArea) throws InstantiationException, IllegalAccessException {
		super(defaultTISweepArea);
	}

	@Override
	abstract public AbstractTimeIntervalSweepArea<T> clone();
	
}
