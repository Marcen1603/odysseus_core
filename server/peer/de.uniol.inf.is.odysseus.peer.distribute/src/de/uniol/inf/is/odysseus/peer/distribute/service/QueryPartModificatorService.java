package de.uniol.inf.is.odysseus.peer.distribute.service;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartModificatorRegistry;

public class QueryPartModificatorService {

	// called by OSGi-DS
	public void bindQueryPartModificator( IQueryPartModificator modificator ) {
		QueryPartModificatorRegistry.getInstance().add(modificator);
	}
	
	// called by OSGi-DS
	public void unbindQueryPartModificator( IQueryPartModificator modificator ) {
		QueryPartModificatorRegistry.getInstance().remove(modificator);
	}
}
