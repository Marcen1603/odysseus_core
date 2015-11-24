package de.uniol.inf.is.odysseus.net.data.container;

import java.util.Collection;
import java.util.UUID;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;

public interface IDistributedDataContainer {

	public void add(IDistributedData data);

	public Optional<IDistributedData> remove(IDistributedData data);
	public Optional<IDistributedData> remove(UUID uuid);
	public Collection<IDistributedData> remove(String name);
	
	public Collection<UUID> getUUIDs();
	public Collection<String> getNames();
	
	public Optional<IDistributedData> get(UUID uuid);
	public Collection<IDistributedData> get(String name);
	
	public boolean containsUUID( UUID uuid );
	public boolean containsName( String name );
	
}
