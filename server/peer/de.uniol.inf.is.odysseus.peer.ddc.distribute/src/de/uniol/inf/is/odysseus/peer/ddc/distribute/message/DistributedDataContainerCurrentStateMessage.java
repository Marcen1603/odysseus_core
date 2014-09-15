package de.uniol.inf.is.odysseus.peer.ddc.distribute.message;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;


public class DistributedDataContainerCurrentStateMessage implements IMessage {
	private static final Logger LOG = LoggerFactory
			.getLogger(DistributedDataContainerCurrentStateMessage.class);
	
	private List<DDCEntry> ddcEntries = new ArrayList<DDCEntry>();
	private UUID ddcAdvertisementUUID;
	private PeerID volunteerPeerID;

	/**
	 * Default constructor.
	 */
	public DistributedDataContainerCurrentStateMessage() {
	}
	
	public static DistributedDataContainerCurrentStateMessage createMessage(PeerID volunteerPeerID, UUID ddcAdvertisementUUID, List<DDCEntry> ddcEntries) {
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
		
		int bbsize = 4 + 4 + ddcAdvertisementUUIDBytes.length + volunteerPeerIDBytes.length;
		ByteBuffer bb = ByteBuffer.allocate(bbsize);
		
		bb.putInt(ddcAdvertisementUUIDBytes.length);
		bb.putInt(volunteerPeerIDBytes.length);
		
		bb.put(ddcAdvertisementUUIDBytes);
		bb.put(volunteerPeerIDBytes);
		
		// TODO add list of ddc
		
		bb.flip();
		return bb.array();
	}

	@Override
	/**
	 * Parses message from byte array.
	 */
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		int sizeOfDDCAdvertisementUUID = bb.getInt();
		int sizeOfvolunteerPeerID = bb.getInt();
		
		byte[] ddcAdvertisementUUIDBytes = new byte[sizeOfDDCAdvertisementUUID];
		byte[] volunteerPeerIDBytes = new byte[sizeOfvolunteerPeerID];
		
		bb.get(ddcAdvertisementUUIDBytes);
		bb.get(volunteerPeerIDBytes);
		
		this.ddcAdvertisementUUID = UUID.fromString(new String(ddcAdvertisementUUIDBytes));
		this.volunteerPeerID = convertToPeerID(new String(volunteerPeerIDBytes));
		
		// TODO add list of ddc
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
