package de.uniol.inf.is.odysseus.peer.distribute.service;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartAllocatorRegistry;

public class QueryPartAllocatorService {

	// called by OSGi-DS
	public void bindQueryPartAllocator( IQueryPartAllocator allocator ) {
		QueryPartAllocatorRegistry.getInstance().add(allocator);
	}
	
	// called by OSGi-DS
	public void unbindQueryPartAllocator( IQueryPartAllocator allocator ) {
		QueryPartAllocatorRegistry.getInstance().remove(allocator);
	}
}
