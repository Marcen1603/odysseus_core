package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.nio.ByteBuffer;
import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.create.DistributedData;

public abstract class AbstractMultipleDistributedDataMessage implements IMessage {

	private Collection<IDistributedData> datas;
	
	public AbstractMultipleDistributedDataMessage() {
	}

	public AbstractMultipleDistributedDataMessage(Collection<IDistributedData> data) {
		Preconditions.checkNotNull(data, "data must not be null!");

		this.datas = data;
	}

	@Override
	public byte[] toBytes() {
		Collection<byte[]> byteArrays = Lists.newArrayListWithExpectedSize(datas.size());
		int byteCount = 0;
		for( IDistributedData data : datas ) {
			byte[] bytes = ((DistributedData)data).toBytes();
			byteArrays.add(bytes);
			byteCount += bytes.length;
		}
		
		byte[] allBytes = new byte[byteCount + byteArrays.size() * 4];
		int index = 0;
		for( byte[] byteArray : byteArrays ) {
			MessageUtils.insertInt(allBytes, index, byteArray.length);
			index += 4;
			System.arraycopy(byteArray, 0, allBytes, index, byteArray.length);
			index += byteArray.length;
		}
		
		return allBytes;
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		datas = Lists.newArrayList();
		
		while(bb.remaining() > 0 ) {
			int sizeOfData = bb.getInt();
			byte[] dataBytes = new byte[sizeOfData];
			bb.get(dataBytes);
			datas.add( DistributedData.fromBytes(dataBytes));
		}
	}

	public Collection<IDistributedData> getDistributedData() {
		return datas;
	}
	
}
