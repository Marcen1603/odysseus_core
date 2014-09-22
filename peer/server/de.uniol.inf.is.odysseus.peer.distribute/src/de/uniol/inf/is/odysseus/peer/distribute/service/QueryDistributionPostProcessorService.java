package de.uniol.inf.is.odysseus.peer.distribute.service;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryDistributionPostProcessorRegistry;

public class QueryDistributionPostProcessorService {

	// called by OSGi-DS
	public void bindQueryDistributionPostProcessor( IQueryDistributionPostProcessor proc ) {
		QueryDistributionPostProcessorRegistry.getInstance().add(proc);
	}
	
	// called by OSGi-DS
	public void unbindQueryDistributionPostProcessor( IQueryDistributionPostProcessor proc ) {
		QueryDistributionPostProcessorRegistry.getInstance().remove(proc);
	}
}
