package de.uniol.inf.is.odysseus.peer.distribute.service;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPreProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryDistributionPreProcessorRegistry;

public class QueryDistributionPreProcessorService {

	// called by OSGi-DS
	public void bindQueryDistributionPreProcessor( IQueryDistributionPreProcessor proc ) {
		QueryDistributionPreProcessorRegistry.getInstance().add(proc);
	}
	
	// called by OSGi-DS
	public void unbindQueryDistributionPreProcessor( IQueryDistributionPreProcessor proc ) {
		QueryDistributionPreProcessorRegistry.getInstance().remove(proc);
	}
}
