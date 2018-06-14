package de.uniol.inf.is.odysseus.net.data.impl.message;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.AbstractDistributedDataMessage;

public class DistributedDataUpdatedMessage extends AbstractDistributedDataMessage {

	public DistributedDataUpdatedMessage() {
		super();
	}

	public DistributedDataUpdatedMessage(IDistributedData data) {
		super(data);
	}

}