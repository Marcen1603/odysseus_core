package de.uniol.inf.is.odysseus.net.data;

import java.util.UUID;

import org.json.JSONObject;

public interface IDistributedData {

	public String getName();
	public UUID getUUID();
	
	public JSONObject getData();
	public boolean isPersistent();
	public long getLifetime();
	
	public long getTimestamp();
}
