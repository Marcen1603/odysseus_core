package de.uniol.inf.is.odysseus.peer.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.distribute.IDistributionChecker;
import de.uniol.inf.is.odysseus.peer.distribute.registry.DistributionCheckerRegistry;

public class DistributionCheckerService {

	private static final Logger LOG = LoggerFactory.getLogger(DistributionCheckerService.class);
	
	public void bindDistributionChecker( IDistributionChecker checker ) {
		LOG.debug("Bound distribution checker {}", checker.getClass());
		
		DistributionCheckerRegistry.getInstance().add(checker);
	}
	
	public void unbindDistributionChecker( IDistributionChecker checker ) {
		LOG.debug("Unbound distribution checker {}", checker.getClass());
		
		DistributionCheckerRegistry.getInstance().remove(checker);
	}
}
