package de.uniol.inf.is.odysseus.peer.distribute.registry;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.util.NamedInterfaceRegistry;

public final class QueryPartAllocatorRegistry extends NamedInterfaceRegistry<IQueryPartAllocator> {

	private static QueryPartAllocatorRegistry instance;

	private QueryPartAllocatorRegistry() {
		
	}
	
	public static QueryPartAllocatorRegistry getInstance() {
		if( instance == null ) {
			instance = new QueryPartAllocatorRegistry();
		}
		return instance;
	}
}
