package de.uniol.inf.is.odysseus.broker.physicaloperator.association;

import de.uniol.inf.is.odysseus.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;

/**
 * The BrokerMetadataMergeFunction merges two {@link ITimeInterval}. 
 * The new ITimeInterval will be the ITimeInterval of the right input.
 * 
 * @author Dennis Geesen
 */
public class BrokerMetadataMergeFunction implements IMetadataMergeFunction<ITimeInterval>{
			
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.metadata.IMetadataMergeFunction#init()
	 */
	@Override
	public void init() {		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.metadata.IMetadataMergeFunction#mergeMetadata(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ITimeInterval mergeMetadata(ITimeInterval left, ITimeInterval right) {		
			return right.clone();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public BrokerMetadataMergeFunction clone() {
		return new BrokerMetadataMergeFunction();
	}

}
