package de.uniol.inf.is.odysseus.net.data.impl;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.create.DistributedData;

public abstract class AbstractDistributedDataMessage implements IMessage {

	private IDistributedData data;

	public AbstractDistributedDataMessage() {

	}

	public AbstractDistributedDataMessage(IDistributedData data) {
		Preconditions.checkNotNull(data, "data must not be null!");

		this.data = data;
	}

	@Override
	public byte[] toBytes() {
		return ((DistributedData)data).toBytes();
	}

	@Override
	public void fromBytes(byte[] dd) {
		data = DistributedData.fromBytes(dd);
	}

	public final IDistributedData getDistributedData() {
		return data;
	}
}
