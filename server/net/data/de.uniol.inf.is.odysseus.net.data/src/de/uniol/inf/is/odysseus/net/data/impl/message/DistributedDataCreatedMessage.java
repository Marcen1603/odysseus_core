package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.create.DistributedData;

public class DistributedDataCreatedMessage implements IMessage {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedDataCreatedMessage.class);
	
	private IDistributedData data;
	private int requestMessageID;
	
	public DistributedDataCreatedMessage() {
		
	}
	
	public DistributedDataCreatedMessage( IDistributedData data, int requestMessageID ) {
		Preconditions.checkNotNull(data, "data must not be null!");

		this.data = data;
		this.requestMessageID = requestMessageID;
	}
	
	@Override
	public byte[] toBytes() {
		OdysseusNodeID creator = data.getCreator();
		JSONObject jsonData = data.getData();
		long lifetime = data.getLifetime();
		String name = data.getName();
		long timestamp = data.getTimestamp();
		UUID uuid = data.getUUID();
		
		String creatorStr = creator.toString();
		String jsonStr = jsonData.toString();
		String uuidStr = uuid.toString();
		
		int creatorStrLength = creatorStr.length();
		int jsonStrLength = jsonStr.length();
		int uuidStrLength = uuidStr.length();
		int nameLength = name.length();
		
		ByteBuffer bb = ByteBuffer.allocate( 4 + creatorStrLength
										   + 4 + jsonStrLength
										   + 8
										   + 4 + nameLength
										   + 8
										   + 4 + uuidStrLength
										   + 1
										   + 4 );
		
		bb.putInt(creatorStrLength);
		bb.put(creatorStr.getBytes());
		
		bb.putInt(jsonStrLength);
		bb.put(jsonStr.getBytes());
		
		bb.putLong(lifetime);
		
		bb.putInt(nameLength);
		bb.put(name.getBytes());
		
		bb.putLong(timestamp);
		
		bb.putInt(uuidStrLength);
		bb.put(uuidStr.getBytes());
		
		bb.put(data.isPersistent() ? (byte)1 : (byte)0);
		
		bb.putInt(requestMessageID);
		
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] dd) {
		ByteBuffer bb = ByteBuffer.wrap(dd);
		
		int creatorStrLength = bb.getInt();
		byte[] creatorStrByteArray = new byte[creatorStrLength];
		bb.get(creatorStrByteArray);
		String creatorStr = new String(creatorStrByteArray);
		OdysseusNodeID nodeID = OdysseusNodeID.fromString(creatorStr);
		
		int jsonStrLength = bb.getInt();
		byte[] jsonStrByteArray = new byte[jsonStrLength];
		bb.get(jsonStrByteArray);
		String jsonStr = new String(jsonStrByteArray);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonStr);
		} catch (JSONException e) {
			LOG.error("Could not create JSON Object from bytes", e);
			jsonObject = new JSONObject();
		}
		
		long lifetime = bb.getLong();
		
		int nameLength = bb.getInt();
		byte[] nameByteArray = new byte[nameLength];
		bb.get(nameByteArray);
		String name = new String(nameByteArray);
		
		long timestamp = bb.getLong();
		
		int uuidStrLength = bb.getInt();
		byte[] uuidStrByteArray = new byte[uuidStrLength];
		bb.get(uuidStrByteArray);
		String uuidStr = new String(uuidStrByteArray);
		UUID uuid = UUID.fromString(uuidStr);
		
		boolean isPersistent = (bb.get() == (byte)1 ? true : false);
		
		requestMessageID = bb.getInt();		
		data = new DistributedData(uuid, name, jsonObject, isPersistent, lifetime, timestamp, nodeID);
	}

	public IDistributedData getDistributedData() {
		return data;
	}
	
	public int getRequestMessageID() {
		return requestMessageID;
	}
}
