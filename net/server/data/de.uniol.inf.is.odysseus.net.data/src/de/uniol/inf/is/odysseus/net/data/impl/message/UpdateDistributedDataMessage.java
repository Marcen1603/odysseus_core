package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;

public class UpdateDistributedDataMessage implements IMessage {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateDistributedDataMessage.class);

	private UUID uuid;
	private JSONObject data;

	public UpdateDistributedDataMessage() {

	}

	public UpdateDistributedDataMessage(UUID uuid, JSONObject data) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");
		Preconditions.checkNotNull(data, "data must not be null!");

		this.uuid = uuid;
		this.data = data;
	}

	@Override
	public byte[] toBytes() {
		String uuidString = uuid.toString();
		int uuidStringLength = uuidString.length();
		String jsonString = data.toString();
		int jsonStringLength = jsonString.length();

		ByteBuffer bb = ByteBuffer.allocate(4 + uuidStringLength + 4 + jsonStringLength);

		MessageUtils.putString(bb, uuidString);
		MessageUtils.putString(bb, jsonString);

		return bb.array();
	}

	@Override
	public void fromBytes(byte[] msg) {
		ByteBuffer bb = ByteBuffer.wrap(msg);

		String uuidString = MessageUtils.getString(bb);
		uuid = UUID.fromString(uuidString);

		String jsonString = MessageUtils.getString(bb);
		try {
			data = new JSONObject(jsonString);
		} catch (JSONException e) {
			LOG.error("Could not get JSONObject from message", e);
			data = null;
		}
	}

	public UUID getUUID() {
		return uuid;
	}

	public JSONObject getData() {
		return data;
	}

}