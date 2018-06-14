package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;

public class CreateDistributedDataMessage implements IMessage {

	private static final Logger LOG = LoggerFactory.getLogger(CreateDistributedDataMessage.class);
	
	private JSONObject data;
	private String name;
	private boolean persistent;
	private long lifetime;
	
	public CreateDistributedDataMessage() {
		
	}
	
	public CreateDistributedDataMessage(JSONObject data, String name, boolean persistent, long lifetime ) {
		Preconditions.checkNotNull(data, "data must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");
		 
		this.data = data;
		this.name = name;
		this.persistent = persistent;
		this.lifetime = lifetime;
	}
	
	@Override
	public byte[] toBytes() {
		String jsonString = data.toString();
		int jsonStringLength = jsonString.length();
		int nameLength = name.length();
		
		ByteBuffer bb = ByteBuffer.allocate(4 + jsonStringLength + 4 + nameLength + 1 + 8);
		
		MessageUtils.putString(bb, jsonString);
		MessageUtils.putString(bb, name);
		
		bb.put(persistent ? (byte)1 : (byte)0);
		bb.putLong(lifetime);
		
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] msg) {
		ByteBuffer bb = ByteBuffer.wrap(msg);
		
		String jsonString = MessageUtils.getString(bb);
		try {
			data = new JSONObject(jsonString);
		} catch (JSONException e) { 
			LOG.error("Could not get JSONObject from message", e);
			data = null;
		}
		
		name = MessageUtils.getString(bb);
		
		persistent = bb.get() == (byte)1 ? true : false;
		lifetime = bb.getLong();
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

}
