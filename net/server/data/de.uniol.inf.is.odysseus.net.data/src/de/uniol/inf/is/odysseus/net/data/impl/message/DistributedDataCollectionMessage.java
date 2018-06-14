package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.util.Collection;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;

public class DistributedDataCollectionMessage extends AbstractMultipleDistributedDataMessage {

	public DistributedDataCollectionMessage() {
	}
	
	public DistributedDataCollectionMessage( Collection<IDistributedData> data ) {
		super(data);
	}
}
