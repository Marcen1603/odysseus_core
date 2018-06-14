package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.nio.ByteBuffer;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;

public class DestroyDistributedDataWithNameMessage implements IMessage {

	private String name;
	
	public DestroyDistributedDataWithNameMessage() {
	}
	
	public DestroyDistributedDataWithNameMessage(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		this.name = name;
	}
	
	@Override
	public byte[] toBytes() {
		int nameLength = name.length();
		
		ByteBuffer bb = ByteBuffer.allocate(4 + nameLength);
		MessageUtils.putString(bb, name);
		
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		name = MessageUtils.getString(bb);
	}
	
	public String getName() {
		return name;
	}
}
