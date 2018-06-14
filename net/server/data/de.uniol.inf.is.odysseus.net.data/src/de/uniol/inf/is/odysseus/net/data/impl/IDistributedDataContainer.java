package de.uniol.inf.is.odysseus.net.data.impl;

import java.util.Collection;
import java.util.UUID;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;

public interface IDistributedDataContainer {

	public void add(IDistributedData data);

	public Optional<IDistributedData> remove(IDistributedData data);
	public Optional<IDistributedData> remove(UUID uuid);
	public Collection<IDistributedData> remove(String name);
	public Collection<IDistributedData> remove(OdysseusNodeID nodeID);

	public Collection<UUID> getUUIDs() throws DistributedDataException;
	public Collection<String> getNames() throws DistributedDataException;
	public Collection<OdysseusNodeID> getOdysseusNodeIDs() throws DistributedDataException;

	public Optional<IDistributedData> get(UUID uuid) throws DistributedDataException;
	public Collection<IDistributedData> get(String name) throws DistributedDataException;
	public Collection<IDistributedData> get(OdysseusNodeID nodeID) throws DistributedDataException;

	public boolean containsUUID(UUID uuid) throws DistributedDataException;
	public boolean containsName(String name) throws DistributedDataException;
	public boolean containsOdysseusNodeID( OdysseusNodeID nodeID ) throws DistributedDataException;

	public void dispose();
	
	public void addListener(IDistributedDataListener listener);
	public void removeListener(IDistributedDataListener listener);

}
