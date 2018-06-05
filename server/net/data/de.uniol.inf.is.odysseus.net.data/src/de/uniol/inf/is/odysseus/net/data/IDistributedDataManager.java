package de.uniol.inf.is.odysseus.net.data;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;

public interface IDistributedDataManager {
	
	// creator
	public IDistributedData create(JSONObject data, String name, boolean persistent, long lifetime) throws DistributedDataException;
	public IDistributedData create(JSONObject data, String name, boolean persistent) throws DistributedDataException;
	
	public Optional<IDistributedData> update(UUID uuid, JSONObject data) throws DistributedDataException;
	
	public Optional<IDistributedData> destroy( UUID uuid ) throws DistributedDataException;
	public Collection<IDistributedData> destroy( String name ) throws DistributedDataException;
	public Collection<IDistributedData> destroyOwn() throws DistributedDataException;

	// container
	public Collection<UUID> getUUIDs() throws DistributedDataException;
	public Collection<String> getNames() throws DistributedDataException;
	public Collection<OdysseusNodeID> getOdysseusNodeIDs() throws DistributedDataException;
	
	public Optional<IDistributedData> get(UUID uuid) throws DistributedDataException;
	public Collection<IDistributedData> get(String name) throws DistributedDataException;
	public Collection<IDistributedData> get(OdysseusNodeID nodeID) throws DistributedDataException;
	public Collection<IDistributedData> getOwn() throws DistributedDataException;
	
	public boolean containsUUID( UUID uuid ) throws DistributedDataException;
	public boolean containsName( String name ) throws DistributedDataException;
	public boolean containsOdysseusNodeID( OdysseusNodeID nodeID ) throws DistributedDataException;
	
	// control
	public boolean isLocal();

	// listeners
	public void addListener(IDistributedDataListener listener);
	public void removeListener(IDistributedDataListener listener);
	
	public boolean isStarted();
}
