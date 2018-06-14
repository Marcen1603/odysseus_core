package de.uniol.inf.is.odysseus.net.querydistribute.service;

import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.net.util.NamedInterfaceRegistry;

public final class QueryPartitionerRegistry extends NamedInterfaceRegistry<IQueryPartitioner> {

	private static QueryPartitionerRegistry instance;

	private QueryPartitionerRegistry() {
		
	}
	
	public static QueryPartitionerRegistry getInstance() {
		if( instance == null ) {
			instance = new QueryPartitionerRegistry();
		}
		return instance;
	}

}
