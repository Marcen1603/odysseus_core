package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.util.Collection;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;

public class MultipleDistributedDataDestroyedMessage  extends AbstractMultipleDistributedDataMessage {

	public MultipleDistributedDataDestroyedMessage() {
	}

	public MultipleDistributedDataDestroyedMessage(Collection<IDistributedData> data) {
		super(data);
	}
}
