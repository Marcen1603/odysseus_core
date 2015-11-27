package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class CreateDistributedDataMessage implements IMessage {

	private static final Logger LOG = LoggerFactory.getLogger(CreateDistributedDataMessage.class);
	
	private JSONObject data;
	private String name;
	private boolean persistent;
	private long lifetime;
	
	private int messageID;
	
	public CreateDistributedDataMessage() {
		
	}
	
	public CreateDistributedDataMessage(JSONObject data, String name, boolean persistent, long lifetime, int messageID ) {
		Preconditions.checkNotNull(data, "data must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");
		 
		this.data = data;
		this.name = name;
		this.persistent = persistent;
		this.lifetime = lifetime;
		this.messageID = messageID;
	}
	
	@Override
	public byte[] toBytes() {
		String jsonString = data.toString();
		int jsonStringLength = jsonString.length();
		int nameLength = name.length();
		
		ByteBuffer bb = ByteBuffer.allocate(4 + jsonStringLength + 4 + nameLength + 1 + 8 + 4);
		
		bb.putInt(jsonStringLength);
		bb.put(jsonString.getBytes());
		
		bb.putInt(nameLength);
		bb.put(name.getBytes());
		
		bb.put(persistent ? (byte)1 : (byte)0);
		bb.putLong(lifetime);
		
		bb.putInt(messageID);
		
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] msg) {
		ByteBuffer bb = ByteBuffer.wrap(msg);
		
		int jsonStringLength = bb.getInt();
		byte[] jsonStringByteArray = new byte[jsonStringLength];
		bb.get(jsonStringByteArray);
		String jsonString = new String(jsonStringByteArray);
		try {
			data = new JSONObject(jsonString);
		} catch (JSONException e) { 
			LOG.error("Could not get JSONObject from message", e);
			data = null;
		}
		
		int nameLength = bb.getInt();
		byte[] nameByteArray = new byte[nameLength];
		bb.get(nameByteArray);
		name = new String(nameByteArray);
		
		persistent = bb.get() == (byte)1 ? true : false;
		lifetime = bb.getLong();
		
		messageID = bb.getInt();
	}

	public JSONObject getData() {
		return data;
	}

	public String getName() {
		return name;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public long getLifetime() {
		return lifetime;
	}

	public int getMessageID() {
		return messageID;
	}
}
