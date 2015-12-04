package de.uniol.inf.is.odysseus.net.data;

import java.util.UUID;

import org.json.JSONObject;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;

public interface IDistributedData {

	public String getName();
	public UUID getUUID();
	
	public JSONObject getData();
	public boolean isPersistent();
	public long getLifetime();
	
	public long getTimestamp();
	public OdysseusNodeID getCreator();
	public boolean isCreatorLocal();
	
}
