package de.uniol.inf.is.odysseus.broker.physicaloperator.association;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataMergeFunction;

public class BrokerMetadataMergeFunction implements IMetadataMergeFunction<ITimeInterval>{
			
	@Override
	public void init() {		
	}

	@Override
	public ITimeInterval mergeMetadata(ITimeInterval left, ITimeInterval right) {		
		return right.clone();		
	}

	@Override
	public BrokerMetadataMergeFunction clone() throws CloneNotSupportedException{
		return new BrokerMetadataMergeFunction();
	}

}
