package de.uniol.inf.is.odysseus.peer.ddc.distribute.message;

import java.nio.ByteBuffer;
import java.util.UUID;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class DDCRequest implements IMessage {

	private UUID advId;

	public UUID getAdvertisementId() {
		return this.advId;
	}

	public void setAdvertisementId(UUID id) {
		this.advId = id;
	}

	private boolean change = false;

	public boolean isChangeRequest() {
		return this.change;
	}

	public void setChangeRequest(boolean change) {
		this.change = change;
	}

	public DDCRequest() {
	}

	@Override
	public byte[] toBytes() {
		byte[] id_bytes = this.advId.toString().getBytes();
		int change_int = 0;
		if (this.change) {
			change_int = 1;
		}
		int bufferSize = 4 + id_bytes.length + 4;
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		buffer.putInt(id_bytes.length);
		buffer.put(id_bytes);
		buffer.putInt(change_int);
		buffer.flip();
		return buffer.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		byte[] id_bytes = new byte[buffer.getInt()];
		buffer.get(id_bytes);
		this.advId = UUID.fromString(new String(id_bytes));
		if (buffer.getInt() == 1) {
			this.change = true;
		}
	}

}