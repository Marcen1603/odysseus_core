package de.uniol.inf.is.odysseus.net.data;

import org.json.JSONObject;

public interface IDistributedDataManager {

	public IDistributedData create(JSONObject data, String name, boolean persistent, long lifetime);
	public IDistributedData create(JSONObject data, String name, boolean persistent);
	
}
