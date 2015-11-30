package de.uniol.inf.is.odysseus.net.data.impl.container.message;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.AbstractDistributedDataMessage;

public class RemoveDistributedDataMessage extends AbstractDistributedDataMessage {

	public RemoveDistributedDataMessage() {
	}
	
	public RemoveDistributedDataMessage( IDistributedData data ) {
		super(data);
	}
}
