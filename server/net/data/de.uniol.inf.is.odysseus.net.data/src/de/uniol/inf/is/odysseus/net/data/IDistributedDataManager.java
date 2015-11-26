package de.uniol.inf.is.odysseus.net.data;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;

public interface IDistributedDataManager {
	
	// creator
	public IDistributedData create(JSONObject data, String name, boolean persistent, long lifetime);
	public IDistributedData create(JSONObject data, String name, boolean persistent);
	
	public Optional<IDistributedData> destroy( UUID uuid );
	public Collection<IDistributedData> destory( String name );

	// container
	public Collection<UUID> getUUIDs();
	public Collection<String> getNames();
	
	public Optional<IDistributedData> get(UUID uuid);
	public Collection<IDistributedData> get(String name);
	
	public boolean containsUUID( UUID uuid );
	public boolean containsName( String name );

}
