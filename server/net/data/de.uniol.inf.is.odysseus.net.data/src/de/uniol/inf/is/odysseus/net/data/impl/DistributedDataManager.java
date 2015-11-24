package de.uniol.inf.is.odysseus.net.data.impl;

import org.json.JSONObject;

import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;

public class DistributedDataManager implements IDistributedDataManager {

	@Override
	public IDistributedData create(JSONObject data, String name, boolean persistent, long lifetime) {
		return null;
	}

	@Override
	public IDistributedData create(JSONObject data, String name, boolean persistent) {
		return null;
	}

}
