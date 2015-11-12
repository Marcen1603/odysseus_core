package de.uniol.inf.is.odysseus.net.querydistribute.service;

import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.net.util.NamedInterfaceRegistry;

public final class QueryPartModificatorRegistry extends NamedInterfaceRegistry<IQueryPartModificator> {

	private static QueryPartModificatorRegistry instance;

	private QueryPartModificatorRegistry() {
		
	}
	
	public static QueryPartModificatorRegistry getInstance() {
		if( instance == null ) {
			instance = new QueryPartModificatorRegistry();
		}
		return instance;
	}
}
