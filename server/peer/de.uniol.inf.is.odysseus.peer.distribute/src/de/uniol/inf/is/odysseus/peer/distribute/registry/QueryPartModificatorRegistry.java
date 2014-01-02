package de.uniol.inf.is.odysseus.peer.distribute.registry;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.util.NamedInterfaceRegistry;

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
