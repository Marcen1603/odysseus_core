package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;

public class UUIDsMessage implements IMessage {

	private Collection<UUID> uuids;
	
	public UUIDsMessage() {
		
	}
	
	public UUIDsMessage(Collection<UUID> uuids) {
		Preconditions.checkNotNull(uuids, "uuids must not be null!");

		this.uuids = uuids;
	}
	
	@Override
	public byte[] toBytes() {
		Collection<String> uuidStrs = Lists.newArrayListWithExpectedSize(uuids.size());
		int size = 0;
		for( UUID uuid : uuids ) {
			String uuidStr = uuid.toString();
			uuidStrs.add(uuidStr);
			
			size += uuidStr.length();
		}
		
		ByteBuffer bb = ByteBuffer.allocate(size + uuidStrs.size() * 4);
		for( String uuidStr : uuidStrs ) {
			MessageUtils.putString(bb, uuidStr);
		}
		
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		uuids = Lists.newArrayList();
		while( bb.remaining() > 0 ) {
			uuids.add(UUID.fromString(MessageUtils.getString(bb)));
		}
	}
	
	public Collection<UUID> getUUIDs() {
		return uuids;
	}

}
