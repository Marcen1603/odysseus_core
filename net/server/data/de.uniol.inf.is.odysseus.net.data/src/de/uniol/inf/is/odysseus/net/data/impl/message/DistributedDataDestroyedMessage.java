package de.uniol.inf.is.odysseus.net.data.impl.message;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.AbstractDistributedDataMessage;

public class DistributedDataDestroyedMessage extends AbstractDistributedDataMessage {

	public DistributedDataDestroyedMessage() {
	}

	public DistributedDataDestroyedMessage(IDistributedData data) {
		super(data);
	}

}
