package de.uniol.inf.is.odysseus.pubsub.test.broker.topology;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.mep.impl.ParseException;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.BrokerTopologyRegistry;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.BusBrokerTopology;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.IBrokerTopology;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.SingleBrokerTopology;

public class BrokerTopologyRegistryTest<T extends IStreamObject<?>> {

	@BeforeTest
	public void initializeRegistry() {
		BrokerTopologyRegistry
				.registerBrokertopologies(new SingleBrokerTopology<T>());
		BrokerTopologyRegistry
				.registerBrokertopologies(new BusBrokerTopology<T>());
	}

	@Test
	public void testCreateMultipleDomains() {
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology2 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain2", "");
		Assert.assertEquals("domain1", topology1.getDomain());
		Assert.assertEquals("domain2", topology2.getDomain());

	}

	@Test
	public void testCreateMultipleDomains2() {
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology2 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("BusTopology", "domain2", "");
		Assert.assertEquals("domain1", topology1.getDomain());
		Assert.assertEquals("domain2", topology2.getDomain());
	}
	
	@Test
	public void testEqualDomainEqualType(){
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology2 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		Assert.assertNotNull(topology1);
		Assert.assertNotNull(topology2);
	}
	
	@Test
	public void testCreateAndGetTopology() {
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology2 = BrokerTopologyRegistry
				.getTopologyByDomain("domain1");
		Assert.assertEquals("domain1", topology1.getDomain());
		Assert.assertEquals("domain1", topology2.getDomain());
	}
	
	@Test
	public void testTopologyNotExists() {
		BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByDomain("domain2");
		Assert.assertNull(topology1);
	}
	
	@Test(expectedExceptions = ParseException.class)
	public void testEqualDomainDifferentType() {
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology2 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("BusTopology", "domain1", "");
		Assert.assertNotNull(topology1);
		Assert.assertNull(topology2);
	}

	@AfterMethod
	public void cleanRegistry() {
		// Topologies need to be removed
		BrokerTopologyRegistry.unregister("domain1");
		BrokerTopologyRegistry.unregister("domain2");
	}

}
