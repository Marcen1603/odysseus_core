package de.uniol.inf.is.odysseus.peer.ddc.distribute.message;

import java.nio.ByteBuffer;
import java.util.UUID;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class DDCAckMessage implements IMessage {

	private UUID advId;

	public UUID getAdvertisementId() {
		return this.advId;
	}

	public void setAdvertisementId(UUID id) {
		this.advId = id;
	}

	public DDCAckMessage() {
	}

	@Override
	public byte[] toBytes() {
		byte[] id_bytes = this.advId.toString().getBytes();
		int bufferSize = 4 + id_bytes.length;
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		buffer.putInt(id_bytes.length);
		buffer.put(id_bytes);
		buffer.flip();
		return buffer.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		byte[] id_bytes = new byte[buffer.getInt()];
		buffer.get(id_bytes);
		this.advId = UUID.fromString(new String(id_bytes));
	}

}