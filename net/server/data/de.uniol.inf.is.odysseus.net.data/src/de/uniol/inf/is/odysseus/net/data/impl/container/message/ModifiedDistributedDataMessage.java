package de.uniol.inf.is.odysseus.net.data.impl.container.message;

import java.nio.ByteBuffer;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.create.DistributedData;

public class ModifiedDistributedDataMessage implements IMessage {

	private IDistributedData oldData;
	private IDistributedData newData;

	public ModifiedDistributedDataMessage() {

	}

	public ModifiedDistributedDataMessage(IDistributedData oldData, IDistributedData newData) {
		Preconditions.checkNotNull(oldData, "oldData must not be null!");
		Preconditions.checkNotNull(newData, "newData must not be null!");

		this.newData = newData;
		this.oldData = oldData;
	}

	@Override
	public byte[] toBytes() {
		byte[] oldDataBytes = ((DistributedData)oldData).toBytes();
		byte[] newDataBytes = ((DistributedData)newData).toBytes();
		
		byte[] byteArray = new byte[oldDataBytes.length + newDataBytes.length + 4];
		MessageUtils.insertInt(byteArray, 0, oldDataBytes.length);
		System.arraycopy(oldDataBytes, 0, byteArray, 4, oldDataBytes.length);
		System.arraycopy(newDataBytes, 0, byteArray, 4 + oldDataBytes.length, newDataBytes.length);
		
		return byteArray;
	}

	@Override
	public void fromBytes(byte[] dd) {
		ByteBuffer bb = ByteBuffer.wrap(dd);
		int oldDataLength = bb.getInt();
		
		byte[] oldDataBytes = new byte[oldDataLength];
		byte[] newDataBytes = new byte[dd.length - 4 - oldDataLength];
		
		bb.get(oldDataBytes);
		bb.get(newDataBytes);
		
		oldData = DistributedData.fromBytes(oldDataBytes);
		newData = DistributedData.fromBytes(newDataBytes);
	}

	public IDistributedData getNewData() {
		return newData;
	}
	
	public IDistributedData getOldData() {
		return oldData;
	}
}
