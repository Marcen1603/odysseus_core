package de.uniol.inf.is.odysseus.peer.ddc.distribute.message;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;

public class DistributedDataContainerCurrentStateMessage implements IMessage {
	private static final Logger LOG = LoggerFactory.getLogger(DistributedDataContainerCurrentStateMessage.class);

	private List<DDCEntry> ddcEntries = new ArrayList<DDCEntry>();
	private UUID ddcAdvertisementUUID;
	private PeerID volunteerPeerID;

	/**
	 * Default constructor.
	 */
	public DistributedDataContainerCurrentStateMessage() {
	}

	public static DistributedDataContainerCurrentStateMessage createMessage(PeerID volunteerPeerID,
			UUID ddcAdvertisementUUID, List<DDCEntry> ddcEntries) {
		DistributedDataContainerCurrentStateMessage message = new DistributedDataContainerCurrentStateMessage();
		message.setVolunteerPeerID(volunteerPeerID);
		message.setDdcAdvertisementUUID(ddcAdvertisementUUID);
		message.setDdcEntries(ddcEntries);
		return message;
	}

	@Override
	/**
	 * Returns message as byte array.
	 */
	public byte[] toBytes() {
		byte[] ddcAdvertisementUUIDBytes = ddcAdvertisementUUID.toString().getBytes();
		byte[] volunteerPeerIDBytes = volunteerPeerID.toString().getBytes();

		int bbsize = 4 + ddcAdvertisementUUIDBytes.length + 4 + volunteerPeerIDBytes.length
				+ calculateSizeOfDDCEntries();
		ByteBuffer bb = ByteBuffer.allocate(bbsize);

		// advertisment uuid
		bb.putInt(ddcAdvertisementUUIDBytes.length);
		bb.put(ddcAdvertisementUUIDBytes);

		// volunteer peerId
		bb.putInt(volunteerPeerIDBytes.length);
		bb.put(volunteerPeerIDBytes);

		// ddc entries
		bb.putInt(ddcEntries.size());
		for (DDCEntry ddcEntry : ddcEntries) {
			// key
			byte[] keyBytes = StringUtils.join(ddcEntry.getKey().get(), ",").getBytes();
			bb.putInt(keyBytes.length);
			bb.put(keyBytes);

			// value
			byte[] valueBytes = ddcEntry.getValue().getBytes();
			bb.putInt(valueBytes.length);
			bb.put(valueBytes);

			// isPersistent
			if (ddcEntry.isPersistent())
				bb.putInt(1);
			else
				bb.putInt(0);

			// ts
			bb.putLong(ddcEntry.getTimeStamp());
		}

		bb.flip();
		return bb.array();
	}

	private int calculateSizeOfDDCEntries() {
		// number of entries
		int size = 4;

		for (DDCEntry ddcEntry : ddcEntries) {
			// size for key
			size += 4;
			size += StringUtils.join(ddcEntry.getKey().get(), ",").getBytes().length;

			// size for value
			size += 4;
			size += ddcEntry.getValue().getBytes().length;
			
			// size for persistent-flag
			size += 4;

			// timestamp as long
			size += 8;
		}
		return size;
	}

	@Override
	/**
	 * Parses message from byte array.
	 */
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);

		// advertisement id
		int sizeOfDDCAdvertisementUUID = bb.getInt();
		byte[] ddcAdvertisementUUIDBytes = new byte[sizeOfDDCAdvertisementUUID];
		bb.get(ddcAdvertisementUUIDBytes);
		this.ddcAdvertisementUUID = UUID.fromString(new String(ddcAdvertisementUUIDBytes));

		// volunteer peerID
		int sizeOfvolunteerPeerID = bb.getInt();
		byte[] volunteerPeerIDBytes = new byte[sizeOfvolunteerPeerID];
		bb.get(volunteerPeerIDBytes);
		this.volunteerPeerID = convertToPeerID(new String(volunteerPeerIDBytes));

		// DDC entries
		int numberOfDDCEntries = bb.getInt();
		ddcEntries.clear();
		for (int i = 0; i < numberOfDDCEntries; i++) {
			try {
				// key
				int sizeOfkey = bb.getInt();
				byte[] keyBytes = new byte[sizeOfkey];
				bb.get(keyBytes);
				String keyString = new String(keyBytes);
				DDCKey ddcKey = new DDCKey(keyString.split(","));

				// value
				int sizeOfValue = bb.getInt();
				byte[] valueBytes = new byte[sizeOfValue];
				bb.get(valueBytes);
				String value = new String(valueBytes);
				
				// persistence
				int isPersistent = bb.getInt();
				boolean persistent = isPersistent == 1;

				// timestamp
				long ts = bb.getLong();

				// create and add DDCEntry
				ddcEntries.add(new DDCEntry(ddcKey, value, ts, persistent));
			} catch (BufferUnderflowException bue) {
				LOG.debug("More DDCEntries in byte-stream expected");
			}
		}
	}

	/**
	 * Converts a given @String to @PeerID
	 * 
	 * @param text
	 *            - String containing a peerId
	 * @return the peerID if exists
	 */
	private static PeerID convertToPeerID(String text) {
		try {
			final URI id = new URI(text);
			return PeerID.create(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not transform to pipeid: {}", text, ex);
			return null;
		}
	}

	public List<DDCEntry> getDdcEntries() {
		return ddcEntries;
	}

	public void setDdcEntries(List<DDCEntry> ddcEntries) {
		this.ddcEntries = ddcEntries;
	}

	public UUID getDdcAdvertisementUUID() {
		return ddcAdvertisementUUID;
	}

	public void setDdcAdvertisementUUID(UUID ddcAdvertisementUUID) {
		this.ddcAdvertisementUUID = ddcAdvertisementUUID;
	}

	public PeerID getVolunteerPeerID() {
		return volunteerPeerID;
	}

	public void setVolunteerPeerID(PeerID volunteerPeerID) {
		this.volunteerPeerID = volunteerPeerID;
	}

}
