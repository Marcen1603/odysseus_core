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
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.create.DistributedData;

public abstract class AbstractDistributedDataMessage implements IMessage {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDistributedDataMessage.class);
	
	private IDistributedData data;
	
	public AbstractDistributedDataMessage() {
		
	}
	
	public AbstractDistributedDataMessage( IDistributedData data) {
		Preconditions.checkNotNull(data, "data must not be null!");

		this.data = data;
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
										   + 1 );
		
		MessageUtils.putString(bb, creatorStr);
		MessageUtils.putString(bb, jsonStr);
		
		bb.putLong(lifetime);
		
		MessageUtils.putString(bb, name);
		
		bb.putLong(timestamp);
		
		MessageUtils.putString(bb, uuidStr);
		
		bb.put(data.isPersistent() ? (byte)1 : (byte)0);
		
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] dd) {
		ByteBuffer bb = ByteBuffer.wrap(dd);
		
		String creatorStr = MessageUtils.getString(bb);
		OdysseusNodeID nodeID = OdysseusNodeID.fromString(creatorStr);

		String jsonStr = MessageUtils.getString(bb);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonStr);
		} catch (JSONException e) {
			LOG.error("Could not create JSON Object from bytes", e);
			jsonObject = new JSONObject();
		}
		
		long lifetime = bb.getLong();
		
		String name = MessageUtils.getString(bb);
		
		long timestamp = bb.getLong();

		String uuidStr = MessageUtils.getString(bb);
		UUID uuid = UUID.fromString(uuidStr);
		
		boolean isPersistent = (bb.get() == (byte)1 ? true : false);
		
		data = new DistributedData(uuid, name, jsonObject, isPersistent, lifetime, timestamp, nodeID);
	}

	public final IDistributedData getDistributedData() {
		return data;
	}
}
