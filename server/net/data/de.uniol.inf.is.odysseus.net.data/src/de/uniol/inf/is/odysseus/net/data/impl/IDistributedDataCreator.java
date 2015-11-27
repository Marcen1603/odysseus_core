package de.uniol.inf.is.odysseus.net.data.impl;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;

public interface IDistributedDataCreator {

	public IDistributedData create(JSONObject data, String name, boolean persistent, long lifetime);
	public IDistributedData create(JSONObject data, String name, boolean persistent);
	
	public Optional<IDistributedData> destroy( UUID uuid );
	public Collection<IDistributedData> destory( String name );
	
}
