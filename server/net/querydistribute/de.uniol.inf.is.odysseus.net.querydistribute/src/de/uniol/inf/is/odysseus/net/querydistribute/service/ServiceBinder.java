package de.uniol.inf.is.odysseus.net.querydistribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.net.querydistribute.IDistributionChecker;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPreProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartitioner;

public class ServiceBinder {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceBinder.class);
	
	// called by OSGi-DS
	public static void bindDistributionChecker(IDistributionChecker serv) {
		DistributionCheckerRegistry.getInstance().add(serv);
		
		LOG.debug("Bound distribution checker {}", serv);
	}

	// called by OSGi-DS
	public static void unbindDistributionChecker(IDistributionChecker serv) {
		DistributionCheckerRegistry.getInstance().remove(serv);
		
		LOG.debug("Unbound distribution checker {}", serv);
	}

	// called by OSGi-DS
	public static void bindQueryDistributionPreProcessor(IQueryDistributionPreProcessor serv) {
		QueryDistributionPreProcessorRegistry.getInstance().add(serv);

		LOG.debug("Bound query distribution preprocessor {}", serv);
	}

	// called by OSGi-DS
	public static void unbindQueryDistributionPreProcessor(IQueryDistributionPreProcessor serv) {
		QueryDistributionPreProcessorRegistry.getInstance().remove(serv);

		LOG.debug("Unbound query distribution preprocessor {}", serv);
	}

	// called by OSGi-DS
	public static void bindQueryPartitioner(IQueryPartitioner serv) {
		QueryPartitionerRegistry.getInstance().add(serv);

		LOG.debug("Bound query partitioner {}", serv);
	}

	// called by OSGi-DS
	public static void unbindQueryPartitioner(IQueryPartitioner serv) {
		QueryPartitionerRegistry.getInstance().remove(serv);

		LOG.debug("Unbound query partitioner {}", serv);
	}
	
	// called by OSGi-DS
	public static void bindQueryPartModificator(IQueryPartModificator serv) {
		QueryPartModificatorRegistry.getInstance().add(serv);

		LOG.debug("Bound query part modificator {}", serv);
	}

	// called by OSGi-DS
	public static void unbindQueryPartModificator(IQueryPartModificator serv) {
		QueryPartModificatorRegistry.getInstance().remove(serv);

		LOG.debug("Unbound query part modificator {}", serv);
	}
	
	// called by OSGi-DS
	public static void bindQueryPartAllocator(IQueryPartAllocator serv) {
		QueryPartAllocatorRegistry.getInstance().add(serv);

		LOG.debug("Bound query part allocator {}", serv);
	}

	// called by OSGi-DS
	public static void unbindQueryPartAllocator(IQueryPartAllocator serv) {
		QueryPartAllocatorRegistry.getInstance().remove(serv);

		LOG.debug("Unbound query part allocator {}", serv);
	}

	// called by OSGi-DS
	public static void bindQueryDistributionPostProcessor(IQueryDistributionPostProcessor serv) {
		QueryDistributionPostProcessorRegistry.getInstance().add(serv);

		LOG.debug("Bound query distribution post processor {}", serv);
	}

	// called by OSGi-DS
	public static void unbindQueryDistributionPostProcessor(IQueryDistributionPostProcessor serv) {
		QueryDistributionPostProcessorRegistry.getInstance().remove(serv);

		LOG.debug("Unbound query distribution post processor {}", serv);
	}
}
