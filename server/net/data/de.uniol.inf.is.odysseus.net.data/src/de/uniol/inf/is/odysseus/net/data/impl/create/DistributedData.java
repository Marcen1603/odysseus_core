package de.uniol.inf.is.odysseus.net.data.impl.create;

import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;

public final class DistributedData implements IDistributedData {

	private final String name;
	private final UUID uuid;
	private final JSONObject data;
	private final boolean isPersistent;
	private final long lifetime;
	private final long timestamp;
	private final OdysseusNodeID creator;
	
	public DistributedData(UUID uuid, String name, JSONObject data, boolean isPersistent, long lifetime, long timestamp, OdysseusNodeID creator) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");
		Preconditions.checkNotNull(data, "data must not be null!");
		Preconditions.checkArgument(timestamp > 0, "timestemp must be positive!");
		Preconditions.checkNotNull(creator, "creator must not be null!");

		this.name = name;
		this.uuid = uuid;
		this.data = data;
		this.isPersistent = isPersistent;
		this.lifetime = lifetime;
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
	public long getLifetime() {
		return lifetime;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public OdysseusNodeID getCreator() {
		return creator;
	}
}
