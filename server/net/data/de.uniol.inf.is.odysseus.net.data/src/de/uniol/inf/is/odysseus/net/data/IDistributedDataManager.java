package de.uniol.inf.is.odysseus.net.data;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;

public interface IDistributedDataManager {
	
	// creator
	public IDistributedData create(JSONObject data, String name, boolean persistent, long lifetime) throws DistributedDataException;
	public IDistributedData create(JSONObject data, String name, boolean persistent) throws DistributedDataException;
	
	public Optional<IDistributedData> destroy( UUID uuid ) throws DistributedDataException;
	public Collection<IDistributedData> destroy( String name ) throws DistributedDataException;

	// container
	public Collection<UUID> getUUIDs();
	public Collection<String> getNames();
	
	public Optional<IDistributedData> get(UUID uuid);
	public Collection<IDistributedData> get(String name);
	
	public boolean containsUUID( UUID uuid );
	public boolean containsName( String name );
	
	// control
	public boolean isLocal();

}
