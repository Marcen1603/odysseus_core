package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.util.UUID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class GetUUIDMessage implements IMessage {

	private UUID uuid;

	public GetUUIDMessage() {
	}

	public GetUUIDMessage(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		this.uuid = uuid;
	}

	@Override
	public byte[] toBytes() {
		return uuid.toString().getBytes();
	}

	@Override
	public void fromBytes(byte[] data) {
		uuid = UUID.fromString(new String(data));
	}

	public UUID getUUID() {
		return uuid;
	}
}
