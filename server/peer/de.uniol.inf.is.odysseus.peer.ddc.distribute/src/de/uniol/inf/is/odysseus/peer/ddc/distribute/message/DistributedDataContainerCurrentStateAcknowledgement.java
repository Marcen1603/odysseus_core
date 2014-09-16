package de.uniol.inf.is.odysseus.peer.ddc.distribute.message;

import java.nio.ByteBuffer;
import java.util.UUID;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class DistributedDataContainerCurrentStateAcknowledgement implements
		IMessage {

	private boolean initiatingSuccessful;
	private UUID ddcAdvertisementUUID;

	/**
	 * Default constructor.
	 */
	public DistributedDataContainerCurrentStateAcknowledgement() {
	}

	public static DistributedDataContainerCurrentStateAcknowledgement createMessage(
			boolean initiatingSuccessful, UUID ddcAdvertisementUUID) {
		DistributedDataContainerCurrentStateAcknowledgement message = new DistributedDataContainerCurrentStateAcknowledgement();
		message.setDdcAdvertisementUUID(ddcAdvertisementUUID);
		message.initiatingSuccessful = initiatingSuccessful;
		return message;
	}

	@Override
	public byte[] toBytes() {
		byte[] ddcAdvertisementUUIDBytes = ddcAdvertisementUUID.toString()
				.getBytes();

		ByteBuffer bb = null;
		int bbsize = 4 + 4 + ddcAdvertisementUUIDBytes.length;
		bb = ByteBuffer.allocate(bbsize);

		bb.putInt(initiatingSuccessful ? 1 : 0);

		bb.putInt(ddcAdvertisementUUIDBytes.length);
		bb.put(ddcAdvertisementUUIDBytes);

		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		initiatingSuccessful = bb.getInt() == 1 ? true : false;
		
		int sizeOfDDCAdvertisementUUID = bb.getInt();
		byte[] ddcAdvertisementUUIDBytes = new byte[sizeOfDDCAdvertisementUUID];
		bb.get(ddcAdvertisementUUIDBytes);
		this.ddcAdvertisementUUID = UUID.fromString(new String(ddcAdvertisementUUIDBytes));
	}

	public boolean isInitiatingSuccessful() {
		return initiatingSuccessful;
	}

	public void setInitiatingSuccessful(boolean initiatingSuccessful) {
		this.initiatingSuccessful = initiatingSuccessful;
	}

	public UUID getDdcAdvertisementUUID() {
		return ddcAdvertisementUUID;
	}

	public void setDdcAdvertisementUUID(UUID ddcAdvertisementUUID) {
		this.ddcAdvertisementUUID = ddcAdvertisementUUID;
	}
}
