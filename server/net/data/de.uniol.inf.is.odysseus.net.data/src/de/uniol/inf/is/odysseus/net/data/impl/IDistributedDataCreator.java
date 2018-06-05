package de.uniol.inf.is.odysseus.net.data.impl;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;

public interface IDistributedDataCreator {

	public IDistributedData create(OdysseusNodeID creator, JSONObject data, String name, boolean persistent, long lifetime) throws DistributedDataException;
	public IDistributedData create(OdysseusNodeID creator, JSONObject data, String name, boolean persistent) throws DistributedDataException;
	
	public Optional<IDistributedData> update(OdysseusNodeID creator, UUID uuid, JSONObject data) throws DistributedDataException;
	
	public Optional<IDistributedData> destroy(OdysseusNodeID creator, UUID uuid ) throws DistributedDataException;
	public Collection<IDistributedData> destroy(OdysseusNodeID creator, String name ) throws DistributedDataException;
	public Collection<IDistributedData> destroy(OdysseusNodeID creator, OdysseusNodeID id) throws DistributedDataException;
	
	public void dispose();
}
