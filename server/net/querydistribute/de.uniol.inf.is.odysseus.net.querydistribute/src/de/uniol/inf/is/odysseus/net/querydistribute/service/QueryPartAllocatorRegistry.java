package de.uniol.inf.is.odysseus.net.querydistribute.service;

import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.net.util.NamedInterfaceRegistry;

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
