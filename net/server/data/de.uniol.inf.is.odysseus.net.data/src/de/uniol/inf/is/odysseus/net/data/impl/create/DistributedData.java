package de.uniol.inf.is.odysseus.net.data.impl.create;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.OdysseusNetDataPlugIn;

public final class DistributedData implements IDistributedData {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedData.class);

	private final String name;
	private final UUID uuid;
	private final JSONObject data;
	private final boolean isPersistent;
	private final long lifetimeMillis;
	private final long timestamp;
	private final OdysseusNodeID creator;

	public DistributedData(UUID uuid, String name, JSONObject data, boolean isPersistent, long lifetimeMillis, long timestamp, OdysseusNodeID creator) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");
		Preconditions.checkNotNull(data, "data must not be null!");
		Preconditions.checkArgument(timestamp > 0, "timestemp must be positive!");
		Preconditions.checkNotNull(creator, "creator must not be null!");

		this.name = name;
		this.uuid = uuid;
		this.data = data;
		this.isPersistent = isPersistent;
		this.lifetimeMillis = lifetimeMillis;
		this.timestamp = timestamp;
		this.creator = creator;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public JSONObject getData() {
		return data;
	}

	@Override
	public boolean isPersistent() {
		return isPersistent;
	}

	@Override
	public long getLifetimeMillis() {
		return lifetimeMillis;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public OdysseusNodeID getCreator() {
		return creator;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DistributedData)) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		return ((DistributedData) obj).getUUID().equals(this.uuid);
	}

	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(uuid).append(":").append(data.toString()).append(">");
		return sb.toString();
	}

	public byte[] toBytes() {
		String creatorStr = creator.toString();
		String jsonStr = data.toString();
		String uuidStr = uuid.toString();

		int creatorStrLength = creatorStr.length();
		int jsonStrLength = jsonStr.length();
		int uuidStrLength = uuidStr.length();
		int nameLength = name.length();

		ByteBuffer bb = ByteBuffer.allocate(4 + creatorStrLength + 4 + jsonStrLength + 8 + 4 + nameLength + 8 + 4 + uuidStrLength + 1);

		MessageUtils.putString(bb, creatorStr);
		MessageUtils.putString(bb, jsonStr);

		bb.putLong(lifetimeMillis);

		MessageUtils.putString(bb, name);

		bb.putLong(timestamp);

		MessageUtils.putString(bb, uuidStr);

		bb.put(isPersistent ? (byte) 1 : (byte) 0);

		return bb.array();
	}

	public static IDistributedData fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);

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

		boolean isPersistent = (bb.get() == (byte) 1 ? true : false);

		return new DistributedData(uuid, name, jsonObject, isPersistent, lifetime, timestamp, nodeID);
	}

	@Override
	public boolean isCreatorLocal() {
		return OdysseusNetDataPlugIn.getNodeManager().isLocalNode(creator);
	}

	@Override
	public boolean isValid() {
		if (lifetimeMillis < 0 || isCreatorLocal() || OdysseusNetDataPlugIn.getNodeManager().existsNode(creator)) {
			return true;
		}

		return (System.currentTimeMillis() - timestamp < lifetimeMillis);
	}
}
