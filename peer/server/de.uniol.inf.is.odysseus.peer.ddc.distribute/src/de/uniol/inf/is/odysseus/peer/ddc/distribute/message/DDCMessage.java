package de.uniol.inf.is.odysseus.peer.ddc.distribute.message;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;

public class DDCMessage implements IMessage {

	private UUID advId;

	public UUID getAdvertisementId() {
		return this.advId;
	}

	public void setAdvertisementId(UUID id) {
		this.advId = id;
	}

	private List<DDCEntry> entries_added = Lists.newArrayList();

	public List<DDCEntry> getEntriesAdded() {
		return this.entries_added;
	}

	public void setEntriesAdded(List<DDCEntry> entries) {
		this.entries_added = entries;
	}

	private List<DDCEntry> entries_removed = Lists.newArrayList();

	public List<DDCEntry> getEntriesRemoved() {
		return this.entries_removed;
	}

	public void setEntriesRemoved(List<DDCEntry> entries) {
		this.entries_removed = entries;
	}

	public DDCMessage() {
	}

	@Override
	public byte[] toBytes() {
		byte[] id_bytes = this.advId.toString().getBytes();
		int bufferSize = 4 + id_bytes.length + 4;
		byte[][] entries_bytes_added = new byte[this.entries_added.size()][];
		for (int i = 0; i < this.entries_added.size(); i++) {
			byte[] entry_bytes = this.entries_added.get(i).toString()
					.getBytes();
			bufferSize += 4 + entry_bytes.length;
			entries_bytes_added[i] = entry_bytes;
		}
		bufferSize += 4;
		byte[][] entries_bytes_removed = new byte[this.entries_removed.size()][];
		for (int i = 0; i < this.entries_removed.size(); i++) {
			byte[] entry_bytes = this.entries_removed.get(i).toString()
					.getBytes();
			bufferSize += 4 + entry_bytes.length;
			entries_bytes_removed[i] = entry_bytes;
		}
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		buffer.putInt(id_bytes.length);
		buffer.put(id_bytes);
		buffer.putInt(this.entries_added.size());
		for (int i = 0; i < this.entries_added.size(); i++) {
			buffer.putInt(entries_bytes_added[i].length);
			buffer.put(entries_bytes_added[i]);
		}
		buffer.putInt(this.entries_removed.size());
		for (int i = 0; i < this.entries_removed.size(); i++) {
			buffer.putInt(entries_bytes_removed[i].length);
			buffer.put(entries_bytes_removed[i]);
		}
		buffer.flip();
		return buffer.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		byte[] id_bytes = new byte[buffer.getInt()];
		buffer.get(id_bytes);
		this.advId = UUID.fromString(new String(id_bytes));
		this.entries_added = Lists.newArrayList();
		int numEntries = buffer.getInt();
		for (int i = 0; i < numEntries; i++) {
			byte[] entry_bytes = new byte[buffer.getInt()];
			buffer.get(entry_bytes);
			this.entries_added.add(toDDCEntry(new String(entry_bytes)));
		}
		this.entries_removed = Lists.newArrayList();
		numEntries = buffer.getInt();
		for (int i = 0; i < numEntries; i++) {
			byte[] entry_bytes = new byte[buffer.getInt()];
			buffer.get(entry_bytes);
			this.entries_removed.add(toDDCEntry(new String(entry_bytes)));
		}
	}

	private static DDCEntry toDDCEntry(String str) {
		// pattern: key = value [ts: ts], persistent: persistent
		String[] key_value = str.trim().split(" = ");
		DDCKey key = toDDCKey(key_value[0].trim());
		String[] value_metadata = key_value[1].trim().split("\\[ts:");
		String value = value_metadata[0].trim();
		String[] ts_persistent = value_metadata[1].trim().split(
				"\\], persistent:");
		long ts = Long.parseLong(ts_persistent[0].trim());
		boolean persistent = Boolean.parseBoolean(ts_persistent[1].trim());
		return new DDCEntry(key, value, ts, persistent);
	}

	private static DDCKey toDDCKey(String str) {
		return new DDCKey(str.split(","));
	}

}