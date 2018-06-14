package de.uniol.inf.is.odysseus.net.data.impl.message;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.create.DistributedData;

public class OptionalDistributedDataMessage implements IMessage {

	private Optional<IDistributedData> optDistributedData;
	
	public OptionalDistributedDataMessage() {
	}
	
	public OptionalDistributedDataMessage(Optional<IDistributedData> optDistributedData) {
		Preconditions.checkNotNull(optDistributedData, "optDistributedData must not be null!");

		this.optDistributedData = optDistributedData;
	}

	@Override
	public byte[] toBytes() {
		if( optDistributedData.isPresent() ) {
			IDistributedData distributedData = optDistributedData.get();
			byte[] bytes = ((DistributedData)distributedData).toBytes();
			byte[] result = new byte[bytes.length + 1];
			result[0] = 1;
			System.arraycopy(bytes, 0, result, 1, bytes.length);
			return result;
		}
		
		byte[] result = new byte[1];
		result[0] = 0;
		return result;
	}

	@Override
	public void fromBytes(byte[] data) {
		boolean isPresent = (data[0] == 1);
		if( !isPresent ) {
			optDistributedData = Optional.absent();
		} else {
			byte[] bytes = new byte[data.length - 1];
			System.arraycopy(data, 1, bytes, 0, bytes.length);
			IDistributedData distributedData = DistributedData.fromBytes(bytes);
			optDistributedData = Optional.of(distributedData);
		}
	}
	
	public Optional<IDistributedData> getDistributedData() {
		return optDistributedData;
	}
	
}
