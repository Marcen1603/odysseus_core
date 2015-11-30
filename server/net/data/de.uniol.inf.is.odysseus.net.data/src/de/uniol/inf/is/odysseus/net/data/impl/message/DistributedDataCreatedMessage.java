package de.uniol.inf.is.odysseus.net.data.impl.message;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.AbstractDistributedDataMessage;

public class DistributedDataCreatedMessage extends AbstractDistributedDataMessage {

	public DistributedDataCreatedMessage() {
		super();
	}
	
	public DistributedDataCreatedMessage( IDistributedData data) {
		super(data);
	}

}
