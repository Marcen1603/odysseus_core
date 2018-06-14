package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.nio.ByteBuffer;
import java.util.UUID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;

public class DestroyDistributedDataWithUUIDMessage implements IMessage {

	private UUID uuid;
	
	public DestroyDistributedDataWithUUIDMessage() {
	}
	
	public DestroyDistributedDataWithUUIDMessage(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");
		
		this.uuid = uuid;
	}
	
	@Override
	public byte[] toBytes() {
		String uuidStr = uuid.toString();
		int uuidStrLength = uuidStr.length();
		
		ByteBuffer bb = ByteBuffer.allocate(4 + uuidStrLength);
		MessageUtils.putString(bb, uuidStr);
		
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		String uuidStr = MessageUtils.getString(bb);
		uuid = UUID.fromString(uuidStr);
	}
	
	public UUID getUUID() {
		return uuid;
	}
}
