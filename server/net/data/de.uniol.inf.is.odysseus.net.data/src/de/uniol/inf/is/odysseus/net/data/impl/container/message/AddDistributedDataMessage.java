package de.uniol.inf.is.odysseus.net.data.impl.container.message;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.AbstractDistributedDataMessage;

public class AddDistributedDataMessage extends AbstractDistributedDataMessage {

	public AddDistributedDataMessage() {
	}

	public AddDistributedDataMessage(IDistributedData data ) {
		super(data);
	}
}
